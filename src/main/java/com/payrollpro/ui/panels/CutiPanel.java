package com.payrollpro.ui.panels;

import com.payrollpro.dao.*;
import com.payrollpro.model.*;
import com.payrollpro.service.*;
import com.payrollpro.ui.MainFrame;
import com.payrollpro.util.*;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.*;
import java.time.ZoneId;
import java.util.List;

public class CutiPanel extends JPanel {

    private final MainFrame      mainFrame;
    private final CutiDAO        dao         = new CutiDAO();
    private final KaryawanDAO    karyawanDAO = new KaryawanDAO();
    private final SaldoCutiDAO   saldoDAO    = new SaldoCutiDAO();
    private final CutiService    service     = new CutiService();

    // Tab Pengajuan
    private JComboBox<Karyawan>  cbKaryawan;
    private JComboBox<JenisCuti> cbJenis;
    private JDateChooser         dcMulai, dcSelesai;
    private JTextField           txtHari, txtSaldo, txtAlasan;
    private JLabel               lblProbationWarn, lblJenisInfo;
    private JButton              btnSimpan, btnSetujui, btnTolak, btnReset;
    private JTable               tblPengajuan, tblSaldo, tblRiwayat;
    private DefaultTableModel    modelPengajuan, modelSaldo, modelRiwayat;
    private JTabbedPane          tabs;

    // Data pengajuan terpilih
    private PengajuanCuti selectedPengajuan;

    public CutiPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout(0, 10));
        setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));
        build();
    }

    private void build() {
        tabs = new JTabbedPane();
        tabs.addTab("Pengajuan Cuti", buildTabPengajuan());
        tabs.addTab("Saldo Cuti",     buildTabSaldo());
        tabs.addTab("Riwayat",        buildTabRiwayat());
        add(tabs, BorderLayout.CENTER);
    }

    // ===================== TAB 1: PENGAJUAN =====================
    private JPanel buildTabPengajuan() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Form Pengajuan Cuti"));
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(4, 6, 4, 6);
        gc.fill   = GridBagConstraints.HORIZONTAL;

        cbKaryawan = new JComboBox<>();
        cbJenis    = new JComboBox<>();
        dcMulai    = new JDateChooser(); dcMulai.setDateFormatString("dd-MM-yyyy");
        dcSelesai  = new JDateChooser(); dcSelesai.setDateFormatString("dd-MM-yyyy");
        txtHari    = ro("-");
        txtSaldo   = ro("-");
        txtAlasan  = new JTextField(20);

        // === PROBATION WARNING - BARU ===
        lblProbationWarn = new JLabel(" ");
        lblProbationWarn.setForeground(new Color(180, 40, 40));
        lblProbationWarn.setFont(new Font("Segoe UI", Font.BOLD, 11));

        // === INFO JENIS CUTI - BARU ===
        lblJenisInfo = new JLabel(" ");
        lblJenisInfo.setForeground(new Color(30, 100, 180));
        lblJenisInfo.setFont(new Font("Segoe UI", Font.ITALIC, 10));

        // Events
        cbKaryawan.addActionListener(e -> {
            cekProbation();   // CEK PROBATION saat karyawan berubah
            loadSaldo();
        });
        cbJenis.addActionListener(e -> showJenisInfo());
        dcMulai.addPropertyChangeListener("date",   e -> hitungHari());
        dcSelesai.addPropertyChangeListener("date",  e -> hitungHari());

        // Layout form
        addR(form, gc, 0, "Karyawan:",   cbKaryawan, "Jenis Cuti:", cbJenis);
        addR(form, gc, 1, "Tgl Mulai:",  dcMulai,    "Tgl Selesai:", dcSelesai);
        addR(form, gc, 2, "Hari Kerja:", txtHari,    "Saldo Tersisa:", txtSaldo);

        // Probation warning - full width
        gc.gridy=3; gc.gridx=0; gc.gridwidth=4;
        form.add(lblProbationWarn, gc); gc.gridwidth=1;

        // Info jenis cuti - full width
        gc.gridy=4; gc.gridx=0; gc.gridwidth=4;
        form.add(lblJenisInfo, gc); gc.gridwidth=1;

        // Alasan
        gc.gridy=5; gc.gridx=0; gc.weightx=0;
        form.add(new JLabel("Alasan:"), gc);
        gc.gridx=1; gc.weightx=1; gc.gridwidth=3;
        form.add(txtAlasan, gc); gc.gridwidth=1;

        // Tombol
        btnSimpan   = new JButton("Simpan Pengajuan");
        btnSetujui  = new JButton("Setujui");
        btnTolak    = new JButton("Tolak");
        btnReset    = new JButton("Reset");

        styleBtn(btnSimpan, new Color(30, 100, 180), Color.WHITE);
        styleBtn(btnSetujui, new Color(30, 140, 80),  Color.WHITE);
        styleBtn(btnTolak, new Color(180, 40, 40),  Color.WHITE);
        styleBtn(btnReset, new Color(108, 117, 125),Color.WHITE);

        gc.gridy=6; gc.gridx=0; gc.gridwidth=4;
        JPanel bp = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        bp.add(btnSimpan); bp.add(btnSetujui); bp.add(btnTolak); bp.add(btnReset);
        form.add(bp, gc);

        // Tabel pengajuan
        String[] cols = {"ID", "Karyawan", "Jenis", "Tgl Mulai", "Tgl Selesai", "Hari", "Status"};
        modelPengajuan = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblPengajuan = buildTable(modelPengajuan);
        tblPengajuan.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) onSelectPengajuan();
        });

        // Warna baris berdasarkan status
        tblPengajuan.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable t, Object val, boolean sel, boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                if (!sel) {
                    String status = String.valueOf(modelPengajuan.getValueAt(row, 6));
                    c.setBackground(switch (status) {
                        case "Disetujui" -> new Color(235, 248, 235);
                        case "Ditolak"   -> new Color(255, 235, 235);
                        default          -> t.getBackground();
                    });
                }
                return c;
            }
        });

        JScrollPane sp = new JScrollPane(tblPengajuan);
        sp.setBorder(BorderFactory.createTitledBorder("Daftar Pengajuan Cuti"));

        panel.add(form, BorderLayout.NORTH);
        panel.add(sp,   BorderLayout.CENTER);

        // Events tombol
        btnSimpan.addActionListener(e  -> simpan());
        btnSetujui.addActionListener(e -> setujui());
        btnTolak.addActionListener(e   -> tolak());
        btnReset.addActionListener(e   -> reset());

        boolean admin = RoleManager.getInstance().isAdmin();
        btnSetujui.setEnabled(admin);
        btnTolak.setEnabled(admin);

        return panel;
    }

    // ===================== TAB 2: SALDO =====================
    private JPanel buildTabSaldo() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));

        JLabel info = infoLabel(
            "Saldo hangus tiap 31 Desember (UU 13/2003). " +
            "Karyawan Probation (< 12 bln masa kerja) TIDAK berhak cuti tahunan.");

        String[] cols = {"Karyawan", "Hak (Hari)", "Terpakai", "Sisa", "Progress", "Keterangan"};
        modelSaldo = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblSaldo = buildTable(modelSaldo);

        // Progress bar renderer untuk kolom Progress
        tblSaldo.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable t, Object val, boolean sel, boolean foc, int row, int col) {
                JProgressBar bar = new JProgressBar(0, 100);
                try {
                    int terpakai = (int) modelSaldo.getValueAt(row, 2);
                    int hak      = (int) modelSaldo.getValueAt(row, 1);
                    int pct      = hak > 0 ? (terpakai * 100 / hak) : 0;
                    bar.setValue(pct);
                    bar.setString(pct + "%");
                    bar.setStringPainted(true);
                    bar.setForeground(pct >= 90 ? new Color(180,40,40) : pct >= 60 ? new Color(180,120,0) : new Color(30,140,80));
                } catch (Exception ignored) {}
                return bar;
            }
        });

        panel.add(info, BorderLayout.NORTH);
        panel.add(new JScrollPane(tblSaldo), BorderLayout.CENTER);
        return panel;
    }

    // ===================== TAB 3: RIWAYAT =====================
    private JPanel buildTabRiwayat() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] cols = {"Karyawan", "Jenis Cuti", "Tgl Mulai", "Tgl Selesai", "Hari", "Dibayar", "Status", "Diproses Oleh"};
        modelRiwayat = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblRiwayat = buildTable(modelRiwayat);
        panel.add(new JScrollPane(tblRiwayat), BorderLayout.CENTER);
        return panel;
    }

    // ===================== REFRESH =====================
    public void refresh() {
        loadKaryawan();
        loadJenis();
        loadTable();
        loadSaldoTable();
        loadRiwayat();
    }

    private void loadKaryawan() {
        new SwingWorker<List<Karyawan>, Void>() {
            @Override protected List<Karyawan> doInBackground() throws Exception {
                return karyawanDAO.getAllAktif();
            }
            @Override protected void done() {
                try {
                    cbKaryawan.removeAllItems();
                    for (Karyawan k : get()) cbKaryawan.addItem(k);
                } catch (Exception e) { showErr(e); }
            }
        }.execute();
    }

    private void loadJenis() {
        new SwingWorker<List<JenisCuti>, Void>() {
            @Override protected List<JenisCuti> doInBackground() throws Exception {
                return dao.getAllJenis();
            }
            @Override protected void done() {
                try {
                    cbJenis.removeAllItems();
                    for (JenisCuti j : get()) cbJenis.addItem(j);
                } catch (Exception e) { showErr(e); }
            }
        }.execute();
    }

    private void loadTable() {
        new SwingWorker<List<PengajuanCuti>, Void>() {
            @Override protected List<PengajuanCuti> doInBackground() throws Exception {
                return dao.getAllPengajuan();
            }
            @Override protected void done() {
                try {
                    modelPengajuan.setRowCount(0);
                    for (PengajuanCuti c : get()) {
                        modelPengajuan.addRow(new Object[]{
                            c.getIdCuti(), c.getNamaKaryawan(), c.getNamaJenis(),
                            c.getTglMulai(), c.getTglSelesai(),
                            c.getJumlahHari() + " hari", c.getStatus()
                        });
                    }
                } catch (Exception e) { showErr(e); }
            }
        }.execute();
    }

    private void loadSaldoTable() {
        new SwingWorker<List<SaldoCuti>, Void>() {
            @Override protected List<SaldoCuti> doInBackground() throws Exception {
                return saldoDAO.getByTahun(LocalDate.now().getYear());
            }
            @Override protected void done() {
                try {
                    modelSaldo.setRowCount(0);
                    for (SaldoCuti s : get()) {
                        String ket = s.getSisa() <= 0 ? "Habis" :
                                     s.getSisa() <= 3 ? "Hampir habis" : "Cukup";
                        modelSaldo.addRow(new Object[]{
                            s.getNamaKaryawan(), s.getKuota(),
                            s.getTerpakai(), s.getSisa(), 0, ket
                        });
                    }
                } catch (Exception e) { showErr(e); }
            }
        }.execute();
    }

    private void loadRiwayat() {
        new SwingWorker<List<PengajuanCuti>, Void>() {
            @Override protected List<PengajuanCuti> doInBackground() throws Exception {
                return dao.getAllPengajuan();
            }
            @Override protected void done() {
                try {
                    modelRiwayat.setRowCount(0);
                    for (PengajuanCuti c : get()) {
                        // cek apakah jenis ini dibayar
                        boolean dibayar = true;
                        for (int i = 0; i < cbJenis.getItemCount(); i++) {
                            if (cbJenis.getItemAt(i).getIdJenis() == c.getIdJenis()) {
                                dibayar = cbJenis.getItemAt(i).isDibayar(); break;
                            }
                        }
                        modelRiwayat.addRow(new Object[]{
                            c.getNamaKaryawan(), c.getNamaJenis(),
                            c.getTglMulai(), c.getTglSelesai(),
                            c.getJumlahHari() + " hari",
                            dibayar ? "Ya" : "Tidak (Dipotong)",
                            c.getStatus(),
                            c.getDiprosesOleh() != null ? c.getDiprosesOleh() : "-"
                        });
                    }
                } catch (Exception e) { showErr(e); }
            }
        }.execute();
    }

    private void loadSaldo() {
        Karyawan k = (Karyawan) cbKaryawan.getSelectedItem();
        if (k == null) return;
        new SwingWorker<SaldoCuti, Void>() {
            @Override protected SaldoCuti doInBackground() throws Exception {
                return saldoDAO.getByKaryawanJenisTahun(k.getIdKaryawan(), 1, LocalDate.now().getYear());
            }
            @Override protected void done() {
                try {
                    SaldoCuti s = get();
                    txtSaldo.setText(s != null ? s.getSisa() + " hari tersisa" : "Belum ada saldo");
                } catch (Exception e) { txtSaldo.setText("-"); }
            }
        }.execute();
    }

    // === CEK PROBATION - fitur baru terhubung ke UI ===
    private void cekProbation() {
        Karyawan k = (Karyawan) cbKaryawan.getSelectedItem();
        if (k == null) { lblProbationWarn.setText(" "); return; }

        if (k.isProba()) {
            lblProbationWarn.setText(
                "PERHATIAN: Karyawan ini masih dalam masa Probation (" +
                k.getMasaKerjaBulan() + " bulan). " +
                "TIDAK berhak mengajukan cuti tahunan. Hanya bisa sakit/alpha.");
            // Nonaktifkan tombol simpan jika jenis cuti tahunan dipilih
            cekBolehCuti();
        } else {
            lblProbationWarn.setText(" ");
            btnSimpan.setEnabled(true);
        }
    }

    private void cekBolehCuti() {
        Karyawan k   = (Karyawan)  cbKaryawan.getSelectedItem();
        JenisCuti j  = (JenisCuti) cbJenis.getSelectedItem();
        if (k == null || j == null) return;

        // Probation tidak boleh cuti tahunan atau cuti melahirkan
        boolean probasi  = k.isProba();
        boolean cutiTahunan = j.getNamaCuti().toLowerCase().contains("tahunan");
        boolean cutiMelahirkan = j.getNamaCuti().toLowerCase().contains("melahirkan");

        if (probasi && (cutiTahunan || cutiMelahirkan)) {
            lblProbationWarn.setText(
                "DITOLAK: Karyawan Probation tidak berhak mengajukan " + j.getNamaCuti() + ".");
            btnSimpan.setEnabled(false);
        } else {
            if (!probasi) lblProbationWarn.setText(" ");
            btnSimpan.setEnabled(true);
        }
    }

    private void hitungHari() {
        if (dcMulai.getDate() == null || dcSelesai.getDate() == null) return;
        new SwingWorker<Integer, Void>() {
            @Override protected Integer doInBackground() throws Exception {
                LocalDate m = dcMulai.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate s = dcSelesai.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                if (m.isAfter(s)) return -1;
                return service.hitungHariKerja(m, s);
            }
            @Override protected void done() {
                try {
                    int h = get();
                    if (h < 0) {
                        txtHari.setText("Tanggal tidak valid");
                        txtHari.setForeground(new Color(180, 40, 40));
                    } else {
                        txtHari.setText(h + " hari kerja");
                        txtHari.setForeground(UIManager.getColor("TextField.foreground"));
                    }
                } catch (Exception e) { txtHari.setText("-"); }
            }
        }.execute();
    }

    private void showJenisInfo() {
        JenisCuti j = (JenisCuti) cbJenis.getSelectedItem();
        if (j == null) return;

        String info = "";
        if (!j.isDibayar()) {
            info = "Jenis cuti ini TIDAK dibayar - akan ada potongan gaji (gaji/25 per hari).";
        } else if (j.isButuhDokumen()) {
            info = "Memerlukan dokumen pendukung (surat dokter / surat resmi).";
        } else if (j.getKuotaHari() > 0) {
            info = "Kuota: " + j.getKuotaHari() + " hari per tahun. " +
                   (j.getKeterangan() != null ? j.getKeterangan() : "");
        } else if (j.getKeterangan() != null) {
            info = j.getKeterangan();
        }
        lblJenisInfo.setText(info.isEmpty() ? " " : info);

        // Cek ulang boleh atau tidak setelah jenis berubah
        cekBolehCuti();
    }

    private void simpan() {
        Karyawan  k = (Karyawan)  cbKaryawan.getSelectedItem();
        JenisCuti j = (JenisCuti) cbJenis.getSelectedItem();

        if (k == null || j == null || dcMulai.getDate() == null || dcSelesai.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Semua field wajib diisi."); return;
        }

        LocalDate m = dcMulai.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate s = dcSelesai.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        if (m.isAfter(s)) {
            JOptionPane.showMessageDialog(this, "Tanggal mulai tidak boleh setelah tanggal selesai."); return;
        }

        // Cek saldo jika cuti tahunan
        if (j.getIdJenis() == 1) {
            new SwingWorker<Boolean, Void>() {
                int hariDiminta;
                @Override protected Boolean doInBackground() throws Exception {
                    hariDiminta = service.hitungHariKerja(m, s);
                    return service.cekSaldoCukup(k.getIdKaryawan(), j.getIdJenis(),
                        LocalDate.now().getYear(), hariDiminta);
                }
                @Override protected void done() {
                    try {
                        if (!get()) {
                            JOptionPane.showMessageDialog(CutiPanel.this,
                                "Saldo cuti tahunan tidak mencukupi.", "Saldo Habis",
                                JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                        doSimpan(k, j, m, s);
                    } catch (Exception e) { showErr(e); }
                }
            }.execute();
        } else {
            doSimpan(k, j, m, s);
        }
    }

    private void doSimpan(Karyawan k, JenisCuti j, LocalDate m, LocalDate s) {
        new SwingWorker<Void, Void>() {
            @Override protected Void doInBackground() throws Exception {
                int hari = service.hitungHariKerja(m, s);
                PengajuanCuti c = new PengajuanCuti();
                c.setIdKaryawan(k.getIdKaryawan());
                c.setIdJenis(j.getIdJenis());
                c.setTglMulai(m);
                c.setTglSelesai(s);
                c.setJumlahHari(hari);
                c.setAlasan(txtAlasan.getText());
                dao.insert(c);
                AuditService.logInsert("pengajuan_cuti", k.getIdKaryawan());
                return null;
            }
            @Override protected void done() {
                try {
                    get();
                    JOptionPane.showMessageDialog(CutiPanel.this, "Pengajuan cuti disimpan.");
                    reset(); loadTable();
                } catch (Exception e) { showErr(e); }
            }
        }.execute();
    }

    private void setujui() {
        if (selectedPengajuan == null) {
            JOptionPane.showMessageDialog(this, "Pilih pengajuan dahulu."); return;
        }
        new SwingWorker<Void, Void>() {
            @Override protected Void doInBackground() throws Exception {
                dao.updateStatus(selectedPengajuan.getIdCuti(), "Disetujui",
                    RoleManager.getInstance().getUsername(), "");
                // Kurangi saldo jika cuti tahunan
                if (selectedPengajuan.getIdJenis() == 1) {
                    service.kurangiSaldo(selectedPengajuan.getIdKaryawan(),
                        selectedPengajuan.getIdJenis(),
                        LocalDate.now().getYear(),
                        selectedPengajuan.getJumlahHari());
                }
                AuditService.log("UPDATE", "cuti",
                    String.valueOf(selectedPengajuan.getIdCuti()), "Disetujui");
                return null;
            }
            @Override protected void done() {
                try {
                    get();
                    JOptionPane.showMessageDialog(CutiPanel.this, "Pengajuan disetujui.");
                    reset(); loadTable(); loadSaldoTable(); loadRiwayat();
                } catch (Exception e) { showErr(e); }
            }
        }.execute();
    }

    private void tolak() {
        if (selectedPengajuan == null) {
            JOptionPane.showMessageDialog(this, "Pilih pengajuan dahulu."); return;
        }
        String catatan = JOptionPane.showInputDialog(this,
            "Alasan penolakan:", "Tolak Pengajuan", JOptionPane.QUESTION_MESSAGE);
        if (catatan == null) return;

        new SwingWorker<Void, Void>() {
            @Override protected Void doInBackground() throws Exception {
                dao.updateStatus(selectedPengajuan.getIdCuti(), "Ditolak",
                    RoleManager.getInstance().getUsername(), catatan);
                AuditService.log("UPDATE", "cuti",
                    String.valueOf(selectedPengajuan.getIdCuti()), "Ditolak: " + catatan);
                return null;
            }
            @Override protected void done() {
                try {
                    get();
                    JOptionPane.showMessageDialog(CutiPanel.this, "Pengajuan ditolak.");
                    reset(); loadTable(); loadRiwayat();
                } catch (Exception e) { showErr(e); }
            }
        }.execute();
    }

    private void reset() {
        selectedPengajuan = null;
        dcMulai.setDate(null); dcSelesai.setDate(null);
        txtHari.setText("-"); txtSaldo.setText("-"); txtAlasan.setText("");
        lblProbationWarn.setText(" "); lblJenisInfo.setText(" ");
        tblPengajuan.clearSelection();
        btnSimpan.setEnabled(true);
    }

    private void onSelectPengajuan() {
        int row = tblPengajuan.getSelectedRow(); if (row < 0) return;
        int id  = (int) modelPengajuan.getValueAt(row, 0);
        selectedPengajuan = new PengajuanCuti();
        selectedPengajuan.setIdCuti(id);
        selectedPengajuan.setIdJenis(1); // default, akan di-refine jika perlu
        // Ambil jumlah hari dari tabel
        String hariStr = (String) modelPengajuan.getValueAt(row, 5);
        try { selectedPengajuan.setJumlahHari(Integer.parseInt(hariStr.split(" ")[0])); }
        catch (Exception ignored) {}
        // Ambil id karyawan - cari dari combobox
        String namaKry = (String) modelPengajuan.getValueAt(row, 1);
        for (int i = 0; i < cbKaryawan.getItemCount(); i++) {
            if (cbKaryawan.getItemAt(i).getNama().equals(namaKry)) {
                selectedPengajuan.setIdKaryawan(cbKaryawan.getItemAt(i).getIdKaryawan());
                break;
            }
        }
    }

    // === HELPER ===
    private JTextField ro(String v) {
        JTextField tf = new JTextField(v, 16);
        tf.setEditable(false);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        tf.setBackground(UIManager.getColor("TextField.inactiveBackground"));
        return tf;
    }

    private JLabel infoLabel(String text) {
        JLabel lbl = new JLabel("<html>" + text + "</html>");
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lbl.setOpaque(true);
        lbl.setBackground(new Color(230, 240, 255));
        lbl.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 200, 240)),
            BorderFactory.createEmptyBorder(6, 8, 6, 8)));
        return lbl;
    }

    private void addR(JPanel p, GridBagConstraints gc, int row,
                       String l1, java.awt.Component c1, String l2, java.awt.Component c2) {
        gc.gridy = row;
        gc.gridx = 0; gc.weightx = 0; p.add(new JLabel(l1), gc);
        gc.gridx = 1; gc.weightx = 1; p.add(c1, gc);
        gc.gridx = 2; gc.weightx = 0; p.add(new JLabel(l2), gc);
        gc.gridx = 3; gc.weightx = 1; p.add(c2, gc);
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
