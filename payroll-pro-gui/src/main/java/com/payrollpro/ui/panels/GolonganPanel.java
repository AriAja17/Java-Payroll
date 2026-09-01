package com.payrollpro.ui.panels;

import com.payrollpro.dao.GolonganDAO;
import com.payrollpro.dao.UMPDAO;
import com.payrollpro.model.Golongan;
import com.payrollpro.service.AuditService;
import com.payrollpro.ui.MainFrame;
import com.payrollpro.util.RoleManager;
import com.payrollpro.util.ValidationUtil;

import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.util.List;

public class GolonganPanel extends javax.swing.JPanel {

    private final MainFrame   mainFrame;
    private final GolonganDAO dao    = new GolonganDAO();
    private final UMPDAO      umpDAO = new UMPDAO();
    private DefaultTableModel tableModel;
    private Golongan          selected;

    public GolonganPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initComponents();
        afterInit();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {
        pnlForm       = new javax.swing.JPanel();
        lblId         = new javax.swing.JLabel();
        txtId         = new javax.swing.JTextField();
        lblNama       = new javax.swing.JLabel();
        txtNama       = new javax.swing.JTextField();
        lblGajiPokok  = new javax.swing.JLabel();
        txtGajiPokok  = new javax.swing.JTextField();
        lblTjIstri    = new javax.swing.JLabel();
        txtTjIstri    = new javax.swing.JTextField();
        lblTjAnak     = new javax.swing.JLabel();
        txtTjAnak     = new javax.swing.JTextField();
        lblTransport  = new javax.swing.JLabel();
        txtTransport  = new javax.swing.JTextField();
        lblMakan      = new javax.swing.JLabel();
        txtMakan      = new javax.swing.JTextField();
        lblTarifLembur= new javax.swing.JLabel();
        txtTarifLembur= new javax.swing.JTextField();
        lblTarifLibur = new javax.swing.JLabel();
        txtTarifLibur = new javax.swing.JTextField();
        lblGajiHarian = new javax.swing.JLabel();
        txtGajiHarian = new javax.swing.JTextField();
        lblUmpWarn    = new javax.swing.JLabel();
        lblUmpWarnMsg = new javax.swing.JLabel();
        pnlButtons    = new javax.swing.JPanel();
        btnSimpan     = new javax.swing.JButton();
        btnUpdate     = new javax.swing.JButton();
        btnHapus      = new javax.swing.JButton();
        btnReset      = new javax.swing.JButton();
        scrlTable     = new javax.swing.JScrollPane();
        tblGolongan   = new javax.swing.JTable();

        pnlForm.setBorder(javax.swing.BorderFactory.createTitledBorder("Data Golongan"));

        lblId.setText("ID Golongan:");
        txtId.setEditable(false);
        lblNama.setText("Nama Golongan:");
        lblGajiPokok.setText("Gaji Pokok:");
        txtGajiPokok.setText("0");
        txtGajiPokok.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override public void keyReleased(java.awt.event.KeyEvent evt) { txtGajiPokokKeyReleased(evt); }
        });
        lblTjIstri.setText("Tunjangan Istri:");
        txtTjIstri.setText("0");
        lblTjAnak.setText("Tunjangan Anak/anak:");
        txtTjAnak.setText("0");
        lblTransport.setText("Transport:");
        txtTransport.setText("0");
        lblMakan.setText("Uang Makan:");
        txtMakan.setText("0");

        lblTarifLembur.setText("Tarif Lembur/Jam:");
        txtTarifLembur.setEditable(false); txtTarifLembur.setText("-");
        lblTarifLibur.setText("Tarif Lembur Libur:");
        txtTarifLibur.setEditable(false); txtTarifLibur.setText("-");
        lblGajiHarian.setText("Gaji Harian (/25):");
        txtGajiHarian.setEditable(false); txtGajiHarian.setText("-");

        lblUmpWarn.setText("Info UMP:");
        lblUmpWarnMsg.setForeground(new java.awt.Color(200, 100, 0));
        lblUmpWarnMsg.setText(" ");

        btnSimpan.setText("Simpan"); btnSimpan.addActionListener(e -> btnSimpanActionPerformed(e));
        btnUpdate.setText("Update"); btnUpdate.addActionListener(e -> btnUpdateActionPerformed(e));
        btnHapus.setText("Hapus");   btnHapus.addActionListener(e -> btnHapusActionPerformed(e));
        btnReset.setText("Reset");   btnReset.addActionListener(e -> btnResetActionPerformed(e));

        javax.swing.GroupLayout pnlButtonsLayout = new javax.swing.GroupLayout(pnlButtons);
        pnlButtons.setLayout(pnlButtonsLayout);
        pnlButtonsLayout.setHorizontalGroup(
            pnlButtonsLayout.createSequentialGroup()
                .addComponent(btnSimpan).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnUpdate).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnHapus).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnReset).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlButtonsLayout.setVerticalGroup(
            pnlButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(btnSimpan).addComponent(btnUpdate).addComponent(btnHapus).addComponent(btnReset)
        );

        javax.swing.GroupLayout pnlFormLayout = new javax.swing.GroupLayout(pnlForm);
        pnlForm.setLayout(pnlFormLayout);
        pnlFormLayout.setHorizontalGroup(
            pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFormLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblId).addComponent(lblNama).addComponent(lblGajiPokok)
                    .addComponent(lblTjIstri).addComponent(lblTjAnak).addComponent(lblTransport)
                    .addComponent(lblMakan).addComponent(lblUmpWarn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, 200, Short.MAX_VALUE)
                    .addComponent(txtGajiPokok, javax.swing.GroupLayout.PREFERRED_SIZE, 160, Short.MAX_VALUE)
                    .addComponent(txtTjIstri, javax.swing.GroupLayout.PREFERRED_SIZE, 160, Short.MAX_VALUE)
                    .addComponent(txtTjAnak, javax.swing.GroupLayout.PREFERRED_SIZE, 160, Short.MAX_VALUE)
                    .addComponent(txtTransport, javax.swing.GroupLayout.PREFERRED_SIZE, 160, Short.MAX_VALUE)
                    .addComponent(txtMakan, javax.swing.GroupLayout.PREFERRED_SIZE, 160, Short.MAX_VALUE)
                    .addComponent(lblUmpWarnMsg, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblTarifLembur).addComponent(lblTarifLibur).addComponent(lblGajiHarian))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTarifLembur, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTarifLibur,  javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtGajiHarian,  javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .addGroup(pnlFormLayout.createSequentialGroup()
                .addContainerGap().addComponent(pnlButtons)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlFormLayout.setVerticalGroup(
            pnlFormLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblId).addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTarifLembur).addComponent(txtTarifLembur, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNama).addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTarifLibur).addComponent(txtTarifLibur, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblGajiPokok).addComponent(txtGajiPokok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblGajiHarian).addComponent(txtGajiHarian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTjIstri).addComponent(txtTjIstri, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTjAnak).addComponent(txtTjAnak, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTransport).addComponent(txtTransport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMakan).addComponent(txtMakan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblUmpWarn).addComponent(lblUmpWarnMsg))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlButtons).addContainerGap()
        );

        tblGolongan.setModel(new javax.swing.table.DefaultTableModel(
            new Object[][]{}, new String[]{"ID", "Nama Golongan", "Gaji Pokok", "Tj. Istri", "Lembur/Jam"}
        ) { @Override public boolean isCellEditable(int r, int c) { return false; } });
        tblGolongan.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblGolongan.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent evt) { tblGolonganMouseClicked(evt); }
        });
        scrlTable.setBorder(javax.swing.BorderFactory.createTitledBorder("Daftar Golongan"));
        scrlTable.setViewportView(tblGolongan);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createSequentialGroup().addGap(14)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(pnlForm,    javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scrlTable,  javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGap(14)
        );
        layout.setVerticalGroup(
            layout.createSequentialGroup().addGap(14)
            .addComponent(pnlForm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(10)
            .addComponent(scrlTable, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
            .addGap(14)
        );
    }// </editor-fold>

    private void txtGajiPokokKeyReleased(java.awt.event.KeyEvent evt) { hitungOtomatis(); }
    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) { simpan(); }
    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) { update(); }
    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt)  { hapus();  }
    private void btnResetActionPerformed(java.awt.event.ActionEvent evt)  { reset();  }
    private void tblGolonganMouseClicked(java.awt.event.MouseEvent evt)   { onSelectRow(); }

    private void afterInit() {
        tableModel = (DefaultTableModel) tblGolongan.getModel();
        tblGolongan.setRowHeight(24);
        tblGolongan.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 11));
        tblGolongan.getTableHeader().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 11));
        boolean admin = RoleManager.getInstance().isAdmin();
        btnSimpan.setEnabled(admin); btnUpdate.setEnabled(admin); btnHapus.setEnabled(admin);
        styleBtn(btnSimpan,
        new java.awt.Color(30, 100, 180),
        java.awt.Color.WHITE);

        styleBtn(btnUpdate,
        new java.awt.Color(243, 156, 18),
        java.awt.Color.WHITE);

        styleBtn(btnHapus,
        new java.awt.Color(180, 40, 40),
        java.awt.Color.WHITE);

        styleBtn(btnReset,
        new java.awt.Color(108, 117, 125),
        java.awt.Color.WHITE);
    }

    public void refresh() { loadTable(); }

    private void loadTable() {
        new javax.swing.SwingWorker<List<Golongan>, Void>() {
            @Override protected List<Golongan> doInBackground() throws Exception { return dao.getAll(); }
            @Override protected void done() {
                try {
                    tableModel.setRowCount(0);
                    for (Golongan g : get()) {
                        tableModel.addRow(new Object[]{
                            g.getIdGolongan(), g.getNamaGolongan(),
                            ValidationUtil.formatRupiah(g.getGajiPokok()),
                            ValidationUtil.formatRupiah(g.getTunjanganIstri()),
                            ValidationUtil.formatRupiah(g.getTarifLembur())
                        });
                    }
                } catch (Exception e) { showErr(e); }
            }
        }.execute();
    }

    private void hitungOtomatis() {
        try {
            double gp = Double.parseDouble(txtGajiPokok.getText().replaceAll("[^\\d.]", ""));
            txtTarifLembur.setText(ValidationUtil.formatRupiah(gp / 173));
            txtTarifLibur.setText(ValidationUtil.formatRupiah((gp / 173) * 2));
            txtGajiHarian.setText(ValidationUtil.formatRupiah(gp / 25));
            checkUMP(gp);
        } catch (NumberFormatException e) {
            txtTarifLembur.setText("-"); txtTarifLibur.setText("-"); txtGajiHarian.setText("-");
        }
    }

    private void checkUMP(double gp) {
        new javax.swing.SwingWorker<Double, Void>() {
            @Override protected Double doInBackground() throws Exception {
                return umpDAO.getUMP("DKI Jakarta", LocalDate.now().getYear());
            }
            @Override protected void done() {
                try {
                    double ump = get();
                    if (ump > 0 && gp < ump)
                        lblUmpWarnMsg.setText("Peringatan: di bawah UMP DKI " + LocalDate.now().getYear() + " (" + ValidationUtil.formatRupiah(ump) + ")");
                    else lblUmpWarnMsg.setText(" ");
                } catch (Exception ignored) { lblUmpWarnMsg.setText(" "); }
            }
        }.execute();
    }

    private void simpan() {
        if (txtNama.getText().trim().isEmpty()) { javax.swing.JOptionPane.showMessageDialog(this, "Nama golongan wajib diisi."); return; }
        Golongan g = buildFromForm();
        new javax.swing.SwingWorker<Void, Void>() {
            @Override protected Void doInBackground() throws Exception {
                g.setIdGolongan(dao.generateId()); dao.insert(g); AuditService.logInsert("golongan", g.getIdGolongan()); return null;
            }
            @Override protected void done() {
                try { get(); javax.swing.JOptionPane.showMessageDialog(GolonganPanel.this, "Golongan disimpan."); reset(); loadTable(); }
                catch (Exception e) { showErr(e); }
            }
        }.execute();
    }

    private void update() {
        if (selected == null) { javax.swing.JOptionPane.showMessageDialog(this, "Pilih golongan dahulu."); return; }
        Golongan g = buildFromForm(); g.setIdGolongan(selected.getIdGolongan());
        new javax.swing.SwingWorker<Void, Void>() {
            @Override protected Void doInBackground() throws Exception { dao.update(g); AuditService.logUpdate("golongan", g.getIdGolongan()); return null; }
            @Override protected void done() {
                try { get(); javax.swing.JOptionPane.showMessageDialog(GolonganPanel.this, "Data diperbarui."); reset(); loadTable(); }
                catch (Exception e) { showErr(e); }
            }
        }.execute();
    }

    private void hapus() {
        if (selected == null) { javax.swing.JOptionPane.showMessageDialog(this, "Pilih golongan dahulu."); return; }
        new javax.swing.SwingWorker<Boolean, Void>() {
            @Override protected Boolean doInBackground() throws Exception { return !dao.isDipakai(selected.getIdGolongan()); }
            @Override protected void done() {
                try {
                    if (!get()) { javax.swing.JOptionPane.showMessageDialog(GolonganPanel.this, "Golongan masih dipakai karyawan.", "Proteksi", javax.swing.JOptionPane.WARNING_MESSAGE); return; }
                    int ok = javax.swing.JOptionPane.showConfirmDialog(GolonganPanel.this, "Hapus golongan " + selected.getNamaGolongan() + "?", "Konfirmasi", javax.swing.JOptionPane.YES_NO_OPTION);
                    if (ok == javax.swing.JOptionPane.YES_OPTION) { dao.delete(selected.getIdGolongan()); AuditService.logDelete("golongan", selected.getIdGolongan()); reset(); loadTable(); }
                } catch (Exception e) { showErr(e); }
            }
        }.execute();
    }

    private void reset() {
        selected = null; txtId.setText("Auto"); txtNama.setText("");
        txtGajiPokok.setText("0"); txtTjIstri.setText("0"); txtTjAnak.setText("0");
        txtTransport.setText("0"); txtMakan.setText("0");
        txtTarifLembur.setText("-"); txtTarifLibur.setText("-"); txtGajiHarian.setText("-");
        lblUmpWarnMsg.setText(" "); tblGolongan.clearSelection();
    }

    private void onSelectRow() {
        int row = tblGolongan.getSelectedRow(); if (row < 0) return;
        String id = (String) tableModel.getValueAt(row, 0);
        new javax.swing.SwingWorker<Golongan, Void>() {
            @Override protected Golongan doInBackground() throws Exception { return dao.getById(id); }
            @Override protected void done() {
                try {
                    selected = get(); if (selected == null) return;
                    txtId.setText(selected.getIdGolongan()); txtNama.setText(selected.getNamaGolongan());
                    txtGajiPokok.setText(String.valueOf((long) selected.getGajiPokok()));
                    txtTjIstri.setText(String.valueOf((long) selected.getTunjanganIstri()));
                    txtTjAnak.setText(String.valueOf((long) selected.getTunjanganAnak()));
                    txtTransport.setText(String.valueOf((long) selected.getTransport()));
                    txtMakan.setText(String.valueOf((long) selected.getUangMakan()));
                    hitungOtomatis();
                } catch (Exception e) { showErr(e); }
            }
        }.execute();
    }

    private Golongan buildFromForm() {
        return new Golongan(null, txtNama.getText().trim(),
            parseD(txtGajiPokok), parseD(txtTjIstri), parseD(txtTjAnak), parseD(txtTransport), parseD(txtMakan));
    }
    private double parseD(javax.swing.JTextField tf) { try { return Double.parseDouble(tf.getText().replaceAll("[^\\d.]", "")); } catch (NumberFormatException e) { return 0; } }
    
    private void styleBtn(javax.swing.JButton b,
                      java.awt.Color bg,
                      java.awt.Color fg) {

    b.setBackground(bg);
    b.setForeground(fg);
    b.setBorderPainted(false);
    b.setFocusPainted(false);
}
    
    private void showErr(Exception e) { javax.swing.JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Kesalahan", javax.swing.JOptionPane.ERROR_MESSAGE); }

    // Variables declaration - do not modify
    private javax.swing.JButton btnHapus, btnReset, btnSimpan, btnUpdate;
    private javax.swing.JLabel lblGajiHarian, lblGajiPokok, lblId, lblMakan, lblNama;
    private javax.swing.JLabel lblTarifLibur, lblTarifLembur, lblTjAnak, lblTjIstri, lblTransport;
    private javax.swing.JLabel lblUmpWarn, lblUmpWarnMsg;
    private javax.swing.JPanel pnlButtons, pnlForm;
    private javax.swing.JScrollPane scrlTable;
    private javax.swing.JTable tblGolongan;
    private javax.swing.JTextField txtGajiHarian, txtGajiPokok, txtId, txtMakan, txtNama;
    private javax.swing.JTextField txtTarifLibur, txtTarifLembur, txtTjAnak, txtTjIstri, txtTransport;
    // End of variables declaration
}
