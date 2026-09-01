package com.payrollpro.ui.panels;

import com.payrollpro.dao.*;
import com.payrollpro.model.*;
import com.payrollpro.service.AuditService;
import com.payrollpro.service.LemburCalculator;
import com.payrollpro.ui.MainFrame;
import com.payrollpro.util.RoleManager;
import com.payrollpro.util.ValidationUtil;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class LemburPanel extends JPanel {

    private final MainFrame mainFrame;
    private final LemburDAO dao           = new LemburDAO();
    private final KaryawanDAO karyawanDAO = new KaryawanDAO();
    private final HariLiburDAO hariLiburDAO = new HariLiburDAO();

    private JComboBox<Karyawan> cbKaryawan;
    private JTextField txtGolongan, txtJenisHari, txtMultiplier, txtTarifJam, txtTotalUpah, txtKeterangan;
    private JSpinner spnJamLembur;
    private JDateChooser dcTanggal;
    private JButton btnSimpan, btnUpdate, btnHapus, btnReset;
    private JTable table;
    private DefaultTableModel tableModel;
    private Lembur selected;
    private boolean isHariLibur = false;
    private Karyawan karyawanDipilih;

    public LemburPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout(0, 10));
        setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));
        build();
    }

    private void build() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Data Lembur"));
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(4, 6, 4, 6);
        gc.fill = GridBagConstraints.HORIZONTAL;

        cbKaryawan   = new JComboBox<>();
        txtGolongan  = roField("-");
        dcTanggal    = new JDateChooser(); dcTanggal.setDateFormatString("dd-MM-yyyy");
        txtJenisHari = roField("-");
        spnJamLembur = new JSpinner(new SpinnerNumberModel(1, 1, 14, 1));
        txtMultiplier= roField("-");
        txtTarifJam  = roField("-");
        txtTotalUpah = roField("-");
        txtKeterangan= new JTextField(20);

        // Events
        cbKaryawan.addActionListener(e -> onKaryawanChange());
        dcTanggal.addPropertyChangeListener("date", e -> onTanggalChange());
        spnJamLembur.addChangeListener(e -> hitungLembur());

        addRow(form, gc, 0, "Karyawan:", cbKaryawan, "Golongan:", txtGolongan);
        addRow(form, gc, 1, "Tanggal Lembur:", dcTanggal, "Jenis Hari:", txtJenisHari);
        addRow(form, gc, 2, "Jam Lembur:", spnJamLembur, "Multiplier (UU):", txtMultiplier);
        addRow(form, gc, 3, "Tarif/Jam Efektif:", txtTarifJam, "Total Upah:", txtTotalUpah);

        gc.gridy = 4; gc.gridx = 0; gc.weightx = 0;
        form.add(new JLabel("Keterangan:"), gc);
        gc.gridx = 1; gc.weightx = 1; gc.gridwidth = 3;
        form.add(txtKeterangan, gc);
        gc.gridwidth = 1;

        btnSimpan = new JButton("Simpan"); btnUpdate = new JButton("Update");
        btnHapus  = new JButton("Hapus");  btnReset  = new JButton("Reset");
        styleBtn(btnSimpan, new Color(30, 100, 180), Color.WHITE);
        styleBtn(btnUpdate, new Color(243, 156, 18), Color.WHITE);
        styleBtn(btnHapus, new Color(180, 40, 40), Color.WHITE);
        styleBtn(btnReset, new Color(108, 117, 125), Color.WHITE);

        gc.gridy = 5; gc.gridx = 0; gc.gridwidth = 4;
        JPanel bp = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        bp.add(btnSimpan); bp.add(btnUpdate); bp.add(btnHapus); bp.add(btnReset);
        form.add(bp, gc);

        String[] cols = {"ID", "Karyawan", "Tanggal", "Jam", "Jenis Hari", "Total Upah"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = buildTable(tableModel);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) onSelectRow();
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createTitledBorder("Riwayat Lembur Bulan Ini"));

        add(form, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        btnSimpan.addActionListener(e -> simpan());
        btnUpdate.addActionListener(e -> update());
        btnHapus.addActionListener(e -> hapus());
        btnReset.addActionListener(e -> reset());

        boolean admin = RoleManager.getInstance().isAdmin();
        btnSimpan.setEnabled(admin); btnUpdate.setEnabled(admin); btnHapus.setEnabled(admin);
    }

    public void refresh() {
        loadKaryawan();
        loadTable();
    }

    private void loadKaryawan() {
        new SwingWorker<List<Karyawan>, Void>() {
            @Override protected List<Karyawan> doInBackground() throws Exception { return karyawanDAO.getAllAktif(); }
            @Override protected void done() {
                try { cbKaryawan.removeAllItems();
                    for (Karyawan k : get()) cbKaryawan.addItem(k); }
                catch (Exception e) { showErr(e); }
            }
        }.execute();
    }

    private void loadTable() {
        LocalDate now = LocalDate.now();
        new SwingWorker<List<Lembur>, Void>() {
            @Override protected List<Lembur> doInBackground() throws Exception {
                return dao.getAllByBulan(now.getMonthValue(), now.getYear());
            }
            @Override protected void done() {
                try {
                    tableModel.setRowCount(0);
                    for (Lembur l : get()) {
                        tableModel.addRow(new Object[]{
                            l.getIdLembur(), l.getNamaKaryawan(),
                            l.getTanggalLembur(), l.getJamLembur() + " jam",
                            l.isHariLibur() ? "Hari Libur" : "Hari Kerja",
                            ValidationUtil.formatRupiah(l.getTotalUpah())
                        });
                    }
                } catch (Exception e) { showErr(e); }
            }
        }.execute();
    }

    private void onKaryawanChange() {
        karyawanDipilih = (Karyawan) cbKaryawan.getSelectedItem();
        if (karyawanDipilih != null && karyawanDipilih.getGolongan() != null)
            txtGolongan.setText(karyawanDipilih.getGolongan().toString());
        hitungLembur();
    }

    private void onTanggalChange() {
        Date d = dcTanggal.getDate();
        if (d == null) return;
        LocalDate tgl = d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        new SwingWorker<Boolean, Void>() {
            @Override protected Boolean doInBackground() throws Exception { return hariLiburDAO.isHariLibur(tgl); }
            @Override protected void done() {
                try {
                    isHariLibur = get();
                    txtJenisHari.setText(isHariLibur ? "Hari Libur Nasional" : "Hari Kerja Biasa");
                    txtJenisHari.setForeground(isHariLibur ? new Color(180, 40, 40) : UIManager.getColor("TextField.foreground"));
                    hitungLembur();
                } catch (Exception e) { showErr(e); }
            }
        }.execute();
    }

    private void hitungLembur() {
        if (karyawanDipilih == null || karyawanDipilih.getGolongan() == null) return;
        double gp   = karyawanDipilih.getGolongan().getGajiPokok();
        int    jam  = (Integer) spnJamLembur.getValue();
        double total= LemburCalculator.hitungTotalLembur(gp, jam, isHariLibur);
        String mult = LemburCalculator.getKeteranganMultiplier(jam, isHariLibur);
        double tarifEfektif = jam > 0 ? total / jam : 0;

        txtMultiplier.setText(mult);
        txtTarifJam.setText(ValidationUtil.formatRupiah(tarifEfektif));
        txtTotalUpah.setText(ValidationUtil.formatRupiah(total));
    }

    private void simpan() {
        if (cbKaryawan.getSelectedItem() == null || dcTanggal.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Karyawan dan tanggal wajib dipilih."); return;
        }
        Lembur l = buildFromForm();
        new SwingWorker<Void, Void>() {
            @Override protected Void doInBackground() throws Exception {
                dao.insert(l); AuditService.logInsert("lembur", String.valueOf(l.getIdLembur())); return null;
            }
            @Override protected void done() {
                try { get(); JOptionPane.showMessageDialog(LemburPanel.this, "Data lembur disimpan."); reset(); loadTable(); }
                catch (Exception e) { showErr(e); }
            }
        }.execute();
    }

    private void update() {
        if (selected == null) { JOptionPane.showMessageDialog(this, "Pilih data lembur dahulu."); return; }
        Lembur l = buildFromForm(); l.setIdLembur(selected.getIdLembur());
        new SwingWorker<Void, Void>() {
            @Override protected Void doInBackground() throws Exception {
                dao.update(l); AuditService.logUpdate("lembur", String.valueOf(l.getIdLembur())); return null;
            }
            @Override protected void done() {
                try { get(); JOptionPane.showMessageDialog(LemburPanel.this, "Data diperbarui."); reset(); loadTable(); }
                catch (Exception e) { showErr(e); }
            }
        }.execute();
    }

    private void hapus() {
        if (selected == null) { JOptionPane.showMessageDialog(this, "Pilih data lembur dahulu."); return; }
        int ok = JOptionPane.showConfirmDialog(this, "Hapus data lembur ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (ok != JOptionPane.YES_OPTION) return;
        new SwingWorker<Void, Void>() {
            @Override protected Void doInBackground() throws Exception {
                dao.delete(selected.getIdLembur()); AuditService.logDelete("lembur", String.valueOf(selected.getIdLembur())); return null;
            }
            @Override protected void done() {
                try { get(); JOptionPane.showMessageDialog(LemburPanel.this, "Data dihapus."); reset(); loadTable(); }
                catch (Exception e) { showErr(e); }
            }
        }.execute();
    }

    private void reset() {
        selected = null; karyawanDipilih = null; isHariLibur = false;
        dcTanggal.setDate(null); spnJamLembur.setValue(1);
        txtGolongan.setText("-"); txtJenisHari.setText("-"); txtMultiplier.setText("-");
        txtTarifJam.setText("-"); txtTotalUpah.setText("-"); txtKeterangan.setText("");
        table.clearSelection();
    }

    private void onSelectRow() {
        int row = table.getSelectedRow(); if (row < 0) return;
        int id = (int) tableModel.getValueAt(row, 0);
        // Set selected - simplified, load from table values
        selected = new Lembur();
        selected.setIdLembur(id);
    }

    private Lembur buildFromForm() {
        Lembur l = new Lembur();
        Karyawan k = (Karyawan) cbKaryawan.getSelectedItem();
        if (k != null) l.setIdKaryawan(k.getIdKaryawan());
        l.setTanggalLembur(dcTanggal.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        l.setJamLembur((Integer) spnJamLembur.getValue());
        l.setHariLibur(isHariLibur);
        if (k != null && k.getGolongan() != null) {
            double gp   = k.getGolongan().getGajiPokok();
            int    jam  = l.getJamLembur();
            double total= LemburCalculator.hitungTotalLembur(gp, jam, isHariLibur);
            l.setTarifPerJam(gp / 173.0);
            l.setTotalUpah(total);
        }
        l.setKeterangan(txtKeterangan.getText());
        return l;
    }

    private JTextField roField(String v) { JTextField tf = new JTextField(v, 16); tf.setEditable(false); tf.setFont(new Font("Segoe UI", Font.PLAIN, 11)); tf.setBackground(UIManager.getColor("TextField.inactiveBackground")); return tf; }
    private void addRow(JPanel p, GridBagConstraints gc, int row, String l1, Component c1, String l2, Component c2) {
        gc.gridy = row; gc.gridx = 0; gc.weightx = 0; p.add(new JLabel(l1), gc);
        gc.gridx = 1; gc.weightx = 1; p.add(c1, gc); gc.gridx = 2; gc.weightx = 0; p.add(new JLabel(l2), gc);
        gc.gridx = 3; gc.weightx = 1; p.add(c2, gc);
    }
    private JTable buildTable(DefaultTableModel m) { JTable t = new JTable(m); t.setFont(new Font("Segoe UI", Font.PLAIN, 11)); t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11)); t.setRowHeight(24); t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); return t; }
    private void styleBtn(JButton b, Color bg, Color fg) {
    b.setBackground(bg);
    b.setForeground(fg);
    b.setFocusPainted(false);
    b.setBorderPainted(false);
}
    private void showErr(Exception e) { JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Kesalahan", JOptionPane.ERROR_MESSAGE); }
}
