package com.payrollpro.ui.panels;

import com.payrollpro.dao.*;
import com.payrollpro.model.*;
import com.payrollpro.service.*;
import com.payrollpro.ui.MainFrame;
import com.payrollpro.util.*;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

public class PengaturanPanel extends JPanel {

    private final MainFrame    mainFrame;
    private final UserDAO      userDAO      = new UserDAO();
    private final HariLiburDAO hariLiburDAO = new HariLiburDAO();
    private final UMPDAO       umpDAO       = new UMPDAO();
    private final KaryawanDAO  karyawanDAO  = new KaryawanDAO();
    private final CutiService  cutiService  = new CutiService();

    private JTabbedPane tabs;

    // Tab User
    private JTable             tblUser;
    private DefaultTableModel  modelUser;
    private JTextField         txtUsername, txtNama;
    private JPasswordField     txtPassword;
    private JComboBox<String>  cbRole;
    private JButton            btnTambahUser, btnUpdateRole, btnResetUser;

    // Tab Hari Libur
    private JTable             tblLibur;
    private DefaultTableModel  modelLibur;
    private JDateChooser       dcLibur;
    private JTextField         txtKetLibur;
    private JButton            btnTambahLibur, btnHapusLibur;

    // Tab UMP
    private JTable             tblUmp;
    private DefaultTableModel  modelUmp;
    private JTextField         txtWilayah, txtNilaiUmp;
    private JSpinner           spnTahunUmp;
    private JButton            btnSimpanUmp, btnHapusUmp;

    // Tab Saldo Cuti - BARU
    private JTable             tblSaldoCuti;
    private DefaultTableModel  modelSaldoCuti;
    private JSpinner           spnTahunReset;
    private JLabel             lblResetInfo;
    private JButton            btnResetSaldo, btnPreviewSaldo;

    public PengaturanPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));

        if (!RoleManager.getInstance().isAdmin()) {
            JLabel lbl = new JLabel("Akses ditolak. Halaman ini hanya untuk Admin.",
                SwingConstants.CENTER);
            lbl.setForeground(new Color(180, 40, 40));
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
            add(lbl, BorderLayout.CENTER);
            return;
        }
        build();
    }

    private void build() {
        tabs = new JTabbedPane();
        tabs.addTab("Role & Akses",     buildTabUser());
        tabs.addTab("Hari Libur",       buildTabHariLibur());
        tabs.addTab("UMP Master",       buildTabUmp());
        tabs.addTab("Reset Saldo Cuti", buildTabSaldoCuti());  // TAB BARU
        add(tabs, BorderLayout.CENTER);
    }

    // =================== TAB 1: USER ===================
    private JPanel buildTabUser() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Tambah / Edit User"));
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(4, 6, 4, 6);
        gc.fill   = GridBagConstraints.HORIZONTAL;

        txtUsername = new JTextField(14);
        txtNama     = new JTextField(14);
        txtPassword = new JPasswordField(14);
        cbRole      = new JComboBox<>(new String[]{"Operator", "Admin"});

        gc.gridy=0; gc.gridx=0; gc.weightx=0; form.add(new JLabel("Username:"), gc);
        gc.gridx=1; gc.weightx=1; form.add(txtUsername, gc);
        gc.gridx=2; gc.weightx=0; form.add(new JLabel("Nama Lengkap:"), gc);
        gc.gridx=3; gc.weightx=1; form.add(txtNama, gc);

        gc.gridy=1; gc.gridx=0; gc.weightx=0; form.add(new JLabel("Password:"), gc);
        gc.gridx=1; gc.weightx=1; form.add(txtPassword, gc);
        gc.gridx=2; gc.weightx=0; form.add(new JLabel("Role:"), gc);
        gc.gridx=3; gc.weightx=1; form.add(cbRole, gc);

        btnTambahUser  = new JButton("Tambah User");
        btnUpdateRole  = new JButton("Update Role");
        btnResetUser   = new JButton("Reset");
        styleBtn(btnTambahUser, new Color(30, 100, 180), Color.WHITE);
        styleBtn(btnResetUser, new Color(108, 117, 125),Color.WHITE);
        styleBtn(btnUpdateRole, new Color(180, 40, 40),Color.WHITE);

        gc.gridy=2; gc.gridx=0; gc.gridwidth=4;
        JPanel bp = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        bp.add(btnTambahUser); bp.add(btnUpdateRole); bp.add(btnResetUser);
        form.add(bp, gc);

        String[] cols = {"Username", "Nama", "Role", "Status", "Last Login"};
        modelUser = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblUser = buildTable(modelUser);
        tblUser.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) onSelectUser();
        });

        JScrollPane sp = new JScrollPane(tblUser);
        sp.setBorder(BorderFactory.createTitledBorder("Daftar User"));

        panel.add(form, BorderLayout.NORTH);
        panel.add(sp,   BorderLayout.CENTER);

        btnTambahUser.addActionListener(e -> tambahUser());
        btnUpdateRole.addActionListener(e -> updateRole());
        btnResetUser.addActionListener(e  -> {
            txtUsername.setText(""); txtNama.setText("");
            txtPassword.setText(""); cbRole.setSelectedIndex(0);
            tblUser.clearSelection();
        });

        return panel;
    }

    // =================== TAB 2: HARI LIBUR ===================
    private JPanel buildTabHariLibur() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        JLabel info = infoLabel("Hari libur dipakai untuk: " +
            "(1) Hitung hari kerja cuti, " +
            "(2) Prorata gaji tengah bulan, " +
            "(3) Multiplier lembur hari libur (2x tarif). Perbarui tiap awal tahun.");

        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        form.setBorder(BorderFactory.createTitledBorder("Tambah Hari Libur"));
        dcLibur     = new JDateChooser();
        dcLibur.setDateFormatString("dd-MM-yyyy");
        dcLibur.setPreferredSize(new Dimension(130, 26));
        txtKetLibur = new JTextField(20);
        btnTambahLibur = new JButton("Tambah");
        btnHapusLibur  = new JButton("Hapus Terpilih");
        styleBtn(btnTambahLibur, new Color(30, 100, 180), Color.WHITE);
        styleBtn(btnHapusLibur,  new Color(180, 40, 40),  Color.WHITE);

        form.add(new JLabel("Tanggal:")); form.add(dcLibur);
        form.add(new JLabel("Keterangan:")); form.add(txtKetLibur);
        form.add(btnTambahLibur); form.add(btnHapusLibur);

        String[] cols = {"ID", "Tanggal", "Keterangan"};
        modelLibur = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblLibur = buildTable(modelLibur);

        JPanel top = new JPanel(new BorderLayout(0, 6));
        top.add(info, BorderLayout.NORTH);
        top.add(form, BorderLayout.CENTER);

        panel.add(top,                       BorderLayout.NORTH);
        panel.add(new JScrollPane(tblLibur), BorderLayout.CENTER);

        btnTambahLibur.addActionListener(e -> tambahLibur());
        btnHapusLibur.addActionListener(e  -> hapusLibur());

        return panel;
    }

    // =================== TAB 3: UMP ===================
    private JPanel buildTabUmp() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        JLabel info = infoLabel("UMP dipakai sebagai batas minimum peringatan saat input golongan. " +
            "Perbarui setiap awal tahun (UMP berubah tiap Januari).");

        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        form.setBorder(BorderFactory.createTitledBorder("Tambah / Update UMP"));
        txtWilayah  = new JTextField(16);
        txtNilaiUmp = new JTextField(12);
        spnTahunUmp = new JSpinner(new SpinnerNumberModel(LocalDate.now().getYear(), 2020, 2100, 1));
        btnSimpanUmp = new JButton("Simpan UMP");
        btnHapusUmp  = new JButton("Hapus Terpilih");
        styleBtn(btnSimpanUmp, new Color(30, 100, 180), Color.WHITE);
        styleBtn(btnHapusUmp,  new Color(180, 40, 40),  Color.WHITE);

        form.add(new JLabel("Wilayah:")); form.add(txtWilayah);
        form.add(new JLabel("Tahun:"));  form.add(spnTahunUmp);
        form.add(new JLabel("Nilai UMP:")); form.add(txtNilaiUmp);
        form.add(btnSimpanUmp); form.add(btnHapusUmp);

        String[] cols = {"ID", "Wilayah", "Tahun", "Nilai UMP"};
        modelUmp = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblUmp = buildTable(modelUmp);
        tblUmp.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) onSelectUmp();
        });

        JPanel top = new JPanel(new BorderLayout(0, 6));
        top.add(info, BorderLayout.NORTH);
        top.add(form, BorderLayout.CENTER);

        panel.add(top,                     BorderLayout.NORTH);
        panel.add(new JScrollPane(tblUmp), BorderLayout.CENTER);

        btnSimpanUmp.addActionListener(e -> simpanUmp());
        btnHapusUmp.addActionListener(e  -> hapusUmp());

        return panel;
    }

    // =================== TAB 4: RESET SALDO CUTI (BARU) ===================
    private JPanel buildTabSaldoCuti() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        JLabel info = infoLabel(
            "<b>Reset Saldo Cuti Tahunan</b><br>" +
            "Lakukan reset ini setiap awal tahun (1 Januari) untuk memberikan hak cuti baru " +
            "kepada semua karyawan aktif yang sudah bekerja >= 12 bulan (UU 13/2003).<br>" +
            "Karyawan Probation (masa kerja < 12 bulan) TIDAK akan mendapat saldo.<br>" +
            "Saldo lama TIDAK carry-over (hangus per 31 Desember).");

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        controlPanel.setBorder(BorderFactory.createTitledBorder("Pengaturan Reset"));

        spnTahunReset = new JSpinner(
            new SpinnerNumberModel(LocalDate.now().getYear(), 2020, 2100, 1));
        btnPreviewSaldo = new JButton("Preview (Cek Dulu)");
        btnResetSaldo   = new JButton("Eksekusi Reset Saldo");

        styleBtn(btnPreviewSaldo, new Color(30, 100, 180), Color.WHITE);
        styleBtn(btnResetSaldo,   new Color(180, 40, 40),  Color.WHITE);

        lblResetInfo = new JLabel(" ");
        lblResetInfo.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        lblResetInfo.setForeground(new Color(30, 100, 180));

        controlPanel.add(new JLabel("Tahun target:"));
        controlPanel.add(spnTahunReset);
        controlPanel.add(btnPreviewSaldo);
        controlPanel.add(btnResetSaldo);

        // Tabel preview
        String[] cols = {"Karyawan", "Masa Kerja (Bln)", "Status", "Berhak Saldo", "Kuota Baru"};
        modelSaldoCuti = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblSaldoCuti = buildTable(modelSaldoCuti);

        // Warna baris
        tblSaldoCuti.setDefaultRenderer(Object.class,
            new javax.swing.table.DefaultTableCellRenderer() {
                @Override public Component getTableCellRendererComponent(
                        JTable t, Object val, boolean sel, boolean foc, int row, int col) {
                    Component c = super.getTableCellRendererComponent(t,val,sel,foc,row,col);
                    if (!sel) {
                        String berhak = String.valueOf(modelSaldoCuti.getValueAt(row, 3));
                        c.setBackground("Ya".equals(berhak)
                            ? new Color(235, 248, 235)
                            : new Color(255, 243, 205));
                    }
                    return c;
                }
            });

        JPanel top = new JPanel(new BorderLayout(0, 6));
        top.add(info,         BorderLayout.NORTH);
        top.add(controlPanel, BorderLayout.CENTER);
        top.add(lblResetInfo, BorderLayout.SOUTH);

        JScrollPane sp = new JScrollPane(tblSaldoCuti);
        sp.setBorder(BorderFactory.createTitledBorder(
            "Preview - Karyawan yang akan mendapat/tidak mendapat saldo cuti"));

        panel.add(top, BorderLayout.NORTH);
        panel.add(sp,  BorderLayout.CENTER);

        btnPreviewSaldo.addActionListener(e -> previewResetSaldo());
        btnResetSaldo.addActionListener(e   -> eksekusiResetSaldo());

        return panel;
    }

    // =================== REFRESH ===================
    public void refresh() {
        if (!RoleManager.getInstance().isAdmin()) return;
        loadUsers();
        loadLibur();
        loadUmp();
        // Saldo cuti hanya di-load saat tab dibuka
    }

    // =================== USER METHODS ===================
    private void loadUsers() {
        new SwingWorker<List<User>, Void>() {
            @Override protected List<User> doInBackground() throws Exception {
                return userDAO.getAll();
            }
            @Override protected void done() {
                try {
                    modelUser.setRowCount(0);
                    for (User u : get()) {
                        modelUser.addRow(new Object[]{
                            u.getUsername(), u.getNamaLengkap(), u.getRole(),
                            u.isActive() ? "Aktif" : "Nonaktif",
                            u.getLastLogin() != null
                                ? u.getLastLogin().toString().substring(0, 16) : "-"
                        });
                    }
                } catch (Exception e) { showErr(e); }
            }
        }.execute();
    }

    private void tambahUser() {
        String username = txtUsername.getText().trim();
        String nama     = txtNama.getText().trim();
        String pass     = new String(txtPassword.getPassword());
        if (username.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username dan password wajib diisi."); return;
        }
        if (pass.length() < 6) {
            JOptionPane.showMessageDialog(this, "Password minimal 6 karakter."); return;
        }
        User u = new User();
        u.setUsername(username); u.setNamaLengkap(nama);
        u.setRole((String) cbRole.getSelectedItem());
        new SwingWorker<Void, Void>() {
            @Override protected Void doInBackground() throws Exception {
                userDAO.insert(u, pass);
                AuditService.logInsert("users", username);
                return null;
            }
            @Override protected void done() {
                try {
                    get();
                    JOptionPane.showMessageDialog(PengaturanPanel.this, "User berhasil ditambahkan.");
                    txtUsername.setText(""); txtNama.setText(""); txtPassword.setText("");
                    loadUsers();
                } catch (Exception e) { showErr(e); }
            }
        }.execute();
    }

    private void updateRole() {
        int row = tblUser.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Pilih user dahulu."); return; }
        String username = (String) modelUser.getValueAt(row, 0);
        String newRole  = (String) cbRole.getSelectedItem();
        new SwingWorker<Void, Void>() {
            @Override protected Void doInBackground() throws Exception {
                User u = new User();
                u.setUsername(username); u.setRole(newRole); u.setActive(true);
                u.setNamaLengkap(username);
                userDAO.update(u);
                AuditService.logUpdate("users", username);
                return null;
            }
            @Override protected void done() {
                try {
                    get();
                    JOptionPane.showMessageDialog(PengaturanPanel.this, "Role diperbarui.");
                    loadUsers();
                } catch (Exception e) { showErr(e); }
            }
        }.execute();
    }

    private void onSelectUser() {
        int row = tblUser.getSelectedRow(); if (row < 0) return;
        txtUsername.setText((String) modelUser.getValueAt(row, 0));
        txtNama.setText((String) modelUser.getValueAt(row, 1));
        cbRole.setSelectedItem(modelUser.getValueAt(row, 2));
    }

    // =================== HARI LIBUR METHODS ===================
    private void loadLibur() {
        new SwingWorker<List<HariLibur>, Void>() {
            @Override protected List<HariLibur> doInBackground() throws Exception {
                return hariLiburDAO.getAll();
            }
            @Override protected void done() {
                try {
                    modelLibur.setRowCount(0);
                    for (HariLibur h : get()) {
                        modelLibur.addRow(new Object[]{h.getId(), h.getTanggal(), h.getKeterangan()});
                    }
                } catch (Exception e) { showErr(e); }
            }
        }.execute();
    }

    private void tambahLibur() {
        if (dcLibur.getDate() == null || txtKetLibur.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tanggal dan keterangan wajib diisi."); return;
        }
        LocalDate tgl = dcLibur.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        HariLibur h   = new HariLibur(tgl, txtKetLibur.getText().trim());
        new SwingWorker<Void, Void>() {
            @Override protected Void doInBackground() throws Exception {
                hariLiburDAO.insert(h);
                AuditService.logInsert("hari_libur", tgl.toString());
                return null;
            }
            @Override protected void done() {
                try {
                    get();
                    dcLibur.setDate(null); txtKetLibur.setText("");
                    loadLibur();
                } catch (Exception e) { showErr(e); }
            }
        }.execute();
    }

    private void hapusLibur() {
        int row = tblLibur.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Pilih hari libur dahulu."); return; }
        int id = (int) modelLibur.getValueAt(row, 0);
        int ok = JOptionPane.showConfirmDialog(this,
            "Hapus hari libur ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (ok != JOptionPane.YES_OPTION) return;
        new SwingWorker<Void, Void>() {
            @Override protected Void doInBackground() throws Exception {
                hariLiburDAO.delete(id);
                AuditService.logDelete("hari_libur", String.valueOf(id));
                return null;
            }
            @Override protected void done() {
                try { get(); loadLibur(); } catch (Exception e) { showErr(e); }
            }
        }.execute();
    }

    // =================== UMP METHODS ===================
    private void loadUmp() {
        new SwingWorker<List<UMP>, Void>() {
            @Override protected List<UMP> doInBackground() throws Exception {
                return umpDAO.getByTahun(LocalDate.now().getYear());
            }
            @Override protected void done() {
                try {
                    modelUmp.setRowCount(0);
                    for (UMP u : get()) {
                        modelUmp.addRow(new Object[]{
                            u.getId(), u.getWilayah(), u.getTahun(),
                            ValidationUtil.formatRupiah(u.getNilaiUmp())
                        });
                    }
                } catch (Exception e) { showErr(e); }
            }
        }.execute();
    }

    private void simpanUmp() {
        if (txtWilayah.getText().trim().isEmpty() || txtNilaiUmp.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Wilayah dan nilai UMP wajib diisi."); return;
        }
        try {
            UMP u = new UMP();
            u.setWilayah(txtWilayah.getText().trim());
            u.setTahun((int) spnTahunUmp.getValue());
            u.setNilaiUmp(Double.parseDouble(txtNilaiUmp.getText().replaceAll("[^\\d.]", "")));
            new SwingWorker<Void, Void>() {
                @Override protected Void doInBackground() throws Exception {
                    umpDAO.upsert(u);
                    AuditService.logInsert("ump_master", u.getWilayah() + "_" + u.getTahun());
                    return null;
                }
                @Override protected void done() {
                    try {
                        get();
                        JOptionPane.showMessageDialog(PengaturanPanel.this, "UMP disimpan.");
                        txtWilayah.setText(""); txtNilaiUmp.setText("");
                        loadUmp();
                    } catch (Exception e) { showErr(e); }
                }
            }.execute();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Nilai UMP harus berupa angka.");
        }
    }

    private void hapusUmp() {
        int row = tblUmp.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Pilih UMP dahulu."); return; }
        int id = (int) modelUmp.getValueAt(row, 0);
        int ok = JOptionPane.showConfirmDialog(this,
            "Hapus data UMP ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (ok != JOptionPane.YES_OPTION) return;
        new SwingWorker<Void, Void>() {
            @Override protected Void doInBackground() throws Exception {
                umpDAO.delete(id); return null;
            }
            @Override protected void done() {
                try { get(); loadUmp(); } catch (Exception e) { showErr(e); }
            }
        }.execute();
    }

    private void onSelectUmp() {
        int row = tblUmp.getSelectedRow(); if (row < 0) return;
        txtWilayah.setText((String) modelUmp.getValueAt(row, 1));
        spnTahunUmp.setValue((int) modelUmp.getValueAt(row, 2));
        // Ambil nilai numerik
        String nilaiStr = String.valueOf(modelUmp.getValueAt(row, 3))
            .replaceAll("[^\\d]", "");
        txtNilaiUmp.setText(nilaiStr);
    }

    // =================== RESET SALDO CUTI METHODS (BARU) ===================
    private void previewResetSaldo() {
        int tahun = (int) spnTahunReset.getValue();
        lblResetInfo.setText("Memuat preview...");

        new SwingWorker<List<Karyawan>, Void>() {
            @Override protected List<Karyawan> doInBackground() throws Exception {
                return karyawanDAO.getAllAktif();
            }
            @Override protected void done() {
                try {
                    List<Karyawan> list = get();
                    modelSaldoCuti.setRowCount(0);
                    int berhak = 0, tidakBerhak = 0;

                    for (Karyawan k : list) {
                        int masa        = k.getMasaKerjaBulan();
                        boolean layak   = masa >= 12;
                        String berhakStr = layak ? "Ya" : "Tidak";
                        String kuota     = layak ? "12 hari" : "0 (Probation/< 12 bln)";
                        String status    = "Probation".equals(k.getStatusKaryawan())
                            ? "Probation" : (layak ? "Aktif" : "Masa kerja < 12 bln");

                        modelSaldoCuti.addRow(new Object[]{
                            k.getNama(), masa, status, berhakStr, kuota
                        });

                        if (layak) berhak++; else tidakBerhak++;
                    }

                    lblResetInfo.setText(
                        "Preview untuk tahun " + tahun + ": " +
                        berhak + " karyawan akan mendapat 12 hari, " +
                        tidakBerhak + " karyawan tidak berhak. " +
                        "Klik 'Eksekusi Reset Saldo' untuk menerapkan.");
                } catch (Exception e) { showErr(e); }
            }
        }.execute();
    }

    private void eksekusiResetSaldo() {
        int tahun = (int) spnTahunReset.getValue();

        int ok = JOptionPane.showConfirmDialog(this,
            "Reset saldo cuti tahunan untuk tahun " + tahun + "?\n\n" +
            "Ini akan:\n" +
            "  - Memberikan 12 hari cuti baru untuk karyawan aktif >= 12 bulan\n" +
            "  - Saldo lama tahun sebelumnya TIDAK carry-over\n" +
            "  - Karyawan Probation TIDAK mendapat saldo\n\n" +
            "Pastikan sudah klik Preview terlebih dahulu!",
            "Konfirmasi Reset Saldo", JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (ok != JOptionPane.YES_OPTION) return;

        new SwingWorker<Integer, Void>() {
            List<Karyawan> aktif;
            @Override protected Integer doInBackground() throws Exception {
                aktif = karyawanDAO.getAllAktif();
                cutiService.resetSaldoTahunan(aktif, tahun);
                // Hitung yang berhak
                return (int) aktif.stream()
                    .filter(k -> k.getMasaKerjaBulan() >= 12).count();
            }
            @Override protected void done() {
                try {
                    int jumlah = get();
                    AuditService.log("UPDATE", "saldo_cuti", "tahun_" + tahun,
                        "Reset saldo cuti tahunan " + tahun +
                        " untuk " + jumlah + " karyawan");
                    JOptionPane.showMessageDialog(PengaturanPanel.this,
                        "Reset saldo cuti tahun " + tahun + " berhasil!\n" +
                        jumlah + " karyawan mendapat saldo 12 hari.",
                        "Reset Berhasil", JOptionPane.INFORMATION_MESSAGE);
                    lblResetInfo.setText(
                        "Reset berhasil untuk tahun " + tahun + ". " + jumlah +
                        " karyawan sudah mendapat saldo cuti baru.");
                    previewResetSaldo(); // Refresh preview
                } catch (Exception e) { showErr(e); }
            }
        }.execute();
    }

    // =================== HELPERS ===================
    private JLabel infoLabel(String text) {
        JLabel lbl = new JLabel("<html>" + text + "</html>");
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lbl.setOpaque(true);
        lbl.setBackground(new Color(230, 240, 255));
        lbl.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 200, 240)),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        return lbl;
    }

    private JTable buildTable(DefaultTableModel m) {
        JTable t = new JTable(m);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));
        t.setRowHeight(24);
        t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return t;
    }

    private void styleBtn(JButton b, Color bg, Color fg) {
        b.setBackground(bg); b.setForeground(fg);
        b.setFocusPainted(false); b.setBorderPainted(false);
    }

    private void showErr(Exception e) {
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(),
            "Kesalahan", JOptionPane.ERROR_MESSAGE);
    }
}
