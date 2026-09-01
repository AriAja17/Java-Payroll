package com.payrollpro.ui.panels;

import com.payrollpro.dao.GolonganDAO;
import com.payrollpro.dao.KaryawanDAO;
import com.payrollpro.model.Golongan;
import com.payrollpro.model.Karyawan;
import com.payrollpro.service.AuditService;
import com.payrollpro.ui.MainFrame;
import com.payrollpro.util.RoleManager;
import com.payrollpro.util.ValidationUtil;

import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

public class KaryawanPanel extends javax.swing.JPanel {

    private final MainFrame    mainFrame;
    private final KaryawanDAO  dao         = new KaryawanDAO();
    private final GolonganDAO  golonganDAO = new GolonganDAO();
    private DefaultTableModel  tableModel;
    private Karyawan           selected;

    public KaryawanPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initComponents();
        afterInit();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        pnlForm          = new javax.swing.JPanel();
        lblId            = new javax.swing.JLabel();
        txtId            = new javax.swing.JTextField();
        lblGolongan      = new javax.swing.JLabel();
        cmbGolongan      = new javax.swing.JComboBox<>();
        lblNama          = new javax.swing.JLabel();
        txtNama          = new javax.swing.JTextField();
        lblJK            = new javax.swing.JLabel();
        cmbJK            = new javax.swing.JComboBox<>();
        lblTempatLahir   = new javax.swing.JLabel();
        txtTempatLahir   = new javax.swing.JTextField();
        lblTglLahir      = new javax.swing.JLabel();
        dcTglLahir       = new com.toedter.calendar.JDateChooser();
        lblStatusNikah   = new javax.swing.JLabel();
        cmbStatusNikah   = new javax.swing.JComboBox<>();
        lblAnak          = new javax.swing.JLabel();
        spnAnak          = new javax.swing.JSpinner();
        lblTglMasuk      = new javax.swing.JLabel();
        dcTglMasuk       = new com.toedter.calendar.JDateChooser();
        lblMasaKerja     = new javax.swing.JLabel();
        txtMasaKerja     = new javax.swing.JTextField();
        lblStatusKaryawan= new javax.swing.JLabel();
        cmbStatusKaryawan= new javax.swing.JComboBox<>();
        lblNpwp          = new javax.swing.JLabel();
        txtNpwp          = new javax.swing.JTextField();
        lblAlamat        = new javax.swing.JLabel();
        txtAlamat        = new javax.swing.JTextField();
        pnlButtons       = new javax.swing.JPanel();
        btnSimpan        = new javax.swing.JButton();
        btnUpdate        = new javax.swing.JButton();
        btnHapus         = new javax.swing.JButton();
        btnReset         = new javax.swing.JButton();
        scrlTable        = new javax.swing.JScrollPane();
        tblKaryawan      = new javax.swing.JTable();

        pnlForm.setBorder(javax.swing.BorderFactory.createTitledBorder("Data Karyawan"));

        lblId.setText("ID Karyawan:");
        txtId.setEditable(false);

        lblGolongan.setText("Golongan:");

        lblNama.setText("Nama Lengkap:");
        lblJK.setText("Jenis Kelamin:");
        cmbJK.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Laki-laki", "Perempuan"}));

        lblTempatLahir.setText("Tempat Lahir:");
        lblTglLahir.setText("Tanggal Lahir:");

        lblStatusNikah.setText("Status Nikah:");
        cmbStatusNikah.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Belum Menikah", "Menikah"}));

        lblAnak.setText("Jumlah Anak:");
        spnAnak.setModel(new javax.swing.SpinnerNumberModel(0, 0, 10, 1));

        lblTglMasuk.setText("Tanggal Masuk:");
        dcTglMasuk.addPropertyChangeListener("date", evt -> dcTglMasukPropertyChange(evt));

        lblMasaKerja.setText("Masa Kerja:");
        txtMasaKerja.setEditable(false);

        lblStatusKaryawan.setText("Status:");
        cmbStatusKaryawan.setModel(new javax.swing.DefaultComboBoxModel<>(
            new String[]{"Probation", "Aktif", "Nonaktif", "Resign", "PHK"}));

        lblNpwp.setText("NPWP:");
        lblAlamat.setText("Alamat:");

        btnSimpan.setText("Simpan");
        btnSimpan.addActionListener(evt -> btnSimpanActionPerformed(evt));
        btnUpdate.setText("Update");
        btnUpdate.addActionListener(evt -> btnUpdateActionPerformed(evt));
        btnHapus.setText("Hapus");
        btnHapus.addActionListener(evt -> btnHapusActionPerformed(evt));
        btnReset.setText("Reset");
        btnReset.addActionListener(evt -> btnResetActionPerformed(evt));

        javax.swing.GroupLayout pnlButtonsLayout = new javax.swing.GroupLayout(pnlButtons);
        pnlButtons.setLayout(pnlButtonsLayout);
        pnlButtonsLayout.setHorizontalGroup(
            pnlButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlButtonsLayout.createSequentialGroup()
                .addComponent(btnSimpan).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnUpdate).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnHapus).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnReset).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlButtonsLayout.setVerticalGroup(
            pnlButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(btnSimpan).addComponent(btnUpdate)
            .addComponent(btnHapus).addComponent(btnReset)
        );

        javax.swing.GroupLayout pnlFormLayout = new javax.swing.GroupLayout(pnlForm);
        pnlForm.setLayout(pnlFormLayout);
        pnlFormLayout.setHorizontalGroup(
            pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFormLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlButtons)
                    .addGroup(pnlFormLayout.createSequentialGroup()
                        .addGroup(pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblId).addComponent(lblNama).addComponent(lblTempatLahir)
                            .addComponent(lblStatusNikah).addComponent(lblTglMasuk)
                            .addComponent(lblStatusKaryawan).addComponent(lblAlamat))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, 200, Short.MAX_VALUE)
                            .addComponent(txtTempatLahir, javax.swing.GroupLayout.PREFERRED_SIZE, 200, Short.MAX_VALUE)
                            .addComponent(cmbStatusNikah, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dcTglMasuk, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbStatusKaryawan, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtAlamat, javax.swing.GroupLayout.PREFERRED_SIZE, 300, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblGolongan).addComponent(lblJK).addComponent(lblTglLahir)
                            .addComponent(lblAnak).addComponent(lblMasaKerja).addComponent(lblNpwp))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbGolongan, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbJK, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dcTglLahir, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(spnAnak, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtMasaKerja, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNpwp, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        pnlFormLayout.setVerticalGroup(
            pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFormLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblId).addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblGolongan).addComponent(cmbGolongan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNama).addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblJK).addComponent(cmbJK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTempatLahir).addComponent(txtTempatLahir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTglLahir).addComponent(dcTglLahir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblStatusNikah).addComponent(cmbStatusNikah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblAnak).addComponent(spnAnak, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTglMasuk).addComponent(dcTglMasuk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblMasaKerja).addComponent(txtMasaKerja, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblStatusKaryawan).addComponent(cmbStatusKaryawan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNpwp).addComponent(txtNpwp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAlamat).addComponent(txtAlamat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlButtons)
                .addContainerGap())
        );

        tblKaryawan.setModel(new javax.swing.table.DefaultTableModel(
            new Object[][]{},
            new String[]{"ID", "Nama", "Golongan", "Status Nikah", "Anak", "Status"}
        ) { @Override public boolean isCellEditable(int r, int c) { return false; } });
        tblKaryawan.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblKaryawan.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblKaryawanMouseClicked(evt);
            }
        });
        scrlTable.setBorder(javax.swing.BorderFactory.createTitledBorder("Daftar Karyawan"));
        scrlTable.setViewportView(tblKaryawan);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlForm,    javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scrlTable,  javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(14, 14, 14))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(pnlForm,   javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(scrlTable, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                .addGap(14, 14, 14))
        );
    }// </editor-fold>

    private void dcTglMasukPropertyChange(java.beans.PropertyChangeEvent evt) {
        if ("date".equals(evt.getPropertyName())) hitungMasaKerja();
    }
    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) { simpan(); }
    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) { update(); }
    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt)  { hapus();  }
    private void btnResetActionPerformed(java.awt.event.ActionEvent evt)  { reset();  }
    private void tblKaryawanMouseClicked(java.awt.event.MouseEvent evt)   { onSelectRow(); }

    // ===================== CUSTOM LOGIC =====================

    private void afterInit() {
        tableModel = (DefaultTableModel) tblKaryawan.getModel();
        tblKaryawan.setRowHeight(24);
        tblKaryawan.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 11));
        tblKaryawan.getTableHeader().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 11));

        boolean admin = RoleManager.getInstance().isAdmin();
        btnSimpan.setEnabled(admin);
        btnUpdate.setEnabled(admin);
        btnHapus.setEnabled(admin);
        
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

        cmbStatusNikah.addActionListener(e -> syncStatusNikah());
        
        syncStatusNikah();
    }
    
    private void syncStatusNikah() {
    boolean menikah = "Menikah".equals(cmbStatusNikah.getSelectedItem());
    spnAnak.setEnabled(menikah);
    if (!menikah) {
        spnAnak.setValue(0);
        }
    }
    
    public void refresh() {
        loadGolongan();
        loadTable();
    }

    private void loadGolongan() {
        new javax.swing.SwingWorker<List<Golongan>, Void>() {
            @Override protected List<Golongan> doInBackground() throws Exception { return golonganDAO.getAll(); }
            @Override protected void done() {
                try { cmbGolongan.removeAllItems(); for (Golongan g : get()) cmbGolongan.addItem(g); }
                catch (Exception e) { showErr(e); }
            }
        }.execute();
    }

    private void loadTable() {
        new javax.swing.SwingWorker<List<Karyawan>, Void>() {
            @Override protected List<Karyawan> doInBackground() throws Exception { return dao.getAll(); }
            @Override protected void done() {
                try {
                    tableModel.setRowCount(0);
                    for (Karyawan k : get()) {
                        tableModel.addRow(new Object[]{
                            k.getIdKaryawan(), k.getNama(), k.getIdGolongan(),
                            k.getStatusNikah(), k.getJumlahAnak(), k.getStatusKaryawan()
                        });
                    }
                } catch (Exception e) { showErr(e); }
            }
        }.execute();
    }

    private void simpan() {
        if (!validateForm()) return;
        Karyawan k = buildFromForm();
        new javax.swing.SwingWorker<Void, Void>() {
            @Override protected Void doInBackground() throws Exception {
                k.setIdKaryawan(dao.generateId()); dao.insert(k);
                AuditService.logInsert("karyawan", k.getIdKaryawan()); return null;
            }
            @Override protected void done() {
                try { get(); javax.swing.JOptionPane.showMessageDialog(KaryawanPanel.this, "Karyawan disimpan."); reset(); loadTable(); }
                catch (Exception e) { showErr(e); }
            }
        }.execute();
    }

    private void update() {
        if (selected == null) { javax.swing.JOptionPane.showMessageDialog(this, "Pilih karyawan dahulu."); return; }
        if (!validateForm()) return;
        Karyawan k = buildFromForm(); k.setIdKaryawan(selected.getIdKaryawan());
        new javax.swing.SwingWorker<Void, Void>() {
            @Override protected Void doInBackground() throws Exception {
                dao.update(k); AuditService.logUpdate("karyawan", k.getIdKaryawan()); return null;
            }
            @Override protected void done() {
                try { get(); javax.swing.JOptionPane.showMessageDialog(KaryawanPanel.this, "Data diperbarui."); reset(); loadTable(); }
                catch (Exception e) { showErr(e); }
            }
        }.execute();
    }

    private void hapus() {
        if (selected == null) { javax.swing.JOptionPane.showMessageDialog(this, "Pilih karyawan dahulu."); return; }
        int ok = javax.swing.JOptionPane.showConfirmDialog(this,
            "Hapus karyawan " + selected.getNama() + "?", "Konfirmasi", javax.swing.JOptionPane.YES_NO_OPTION);
        if (ok != javax.swing.JOptionPane.YES_OPTION) return;
        new javax.swing.SwingWorker<Boolean, Void>() {
            @Override protected Boolean doInBackground() throws Exception { return dao.delete(selected.getIdKaryawan()); }
            @Override protected void done() {
                try {
                    if (get()) {
                        AuditService.logDelete("karyawan", selected.getIdKaryawan());
                        reset(); loadTable();
                    } else {
                        javax.swing.JOptionPane.showMessageDialog(KaryawanPanel.this,
                            "Tidak bisa dihapus: karyawan punya riwayat gaji/lembur.\nGunakan status 'Resign'.",
                            "Proteksi Data", javax.swing.JOptionPane.WARNING_MESSAGE);
                    }
                } catch (Exception e) { showErr(e); }
            }
        }.execute();
    }

    private void reset() {
        selected = null;
        txtId.setText("Auto"); txtNama.setText(""); txtTempatLahir.setText("");
        txtAlamat.setText(""); txtNpwp.setText(""); txtMasaKerja.setText("");
        cmbJK.setSelectedIndex(0); cmbStatusNikah.setSelectedIndex(0);
        cmbStatusKaryawan.setSelectedIndex(0); spnAnak.setValue(0);
        dcTglLahir.setDate(null); dcTglMasuk.setDate(null);
        tblKaryawan.clearSelection();
        
        syncStatusNikah();
    }

    private void onSelectRow() {
        int row = tblKaryawan.getSelectedRow(); if (row < 0) return;
        String id = (String) tableModel.getValueAt(row, 0);
        new javax.swing.SwingWorker<Karyawan, Void>() {
            @Override protected Karyawan doInBackground() throws Exception { return dao.getById(id); }
            @Override protected void done() {
                try {
                    selected = get(); if (selected == null) return;
                    txtId.setText(selected.getIdKaryawan());
                    txtNama.setText(selected.getNama());
                    txtTempatLahir.setText(selected.getTempatLahir() != null ? selected.getTempatLahir() : "");
                    txtAlamat.setText(selected.getAlamat() != null ? selected.getAlamat() : "");
                    txtNpwp.setText(selected.getNpwp() != null ? selected.getNpwp() : "");
                    cmbJK.setSelectedItem(selected.getJenisKelamin());
                    cmbStatusNikah.setSelectedItem(selected.getStatusNikah());
                    cmbStatusKaryawan.setSelectedItem(selected.getStatusKaryawan());
                    spnAnak.setValue(selected.getJumlahAnak());
                    for (int i = 0; i < cmbGolongan.getItemCount(); i++) {
                        if (cmbGolongan.getItemAt(i).getIdGolongan().equals(selected.getIdGolongan())) {
                            cmbGolongan.setSelectedIndex(i); break;
                        }
                    }
                    if (selected.getTanggalLahir() != null)
                        dcTglLahir.setDate(Date.from(selected.getTanggalLahir().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                    if (selected.getTanggalMasuk() != null)
                        dcTglMasuk.setDate(Date.from(selected.getTanggalMasuk().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                    hitungMasaKerja();
                } catch (Exception e) { showErr(e); }
            }
        }.execute();
    }

    private void hitungMasaKerja() {
        Date d = dcTglMasuk.getDate();
        if (d == null) { txtMasaKerja.setText("-"); return; }
        LocalDate masuk = d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        long bln = ChronoUnit.MONTHS.between(masuk, LocalDate.now());
        txtMasaKerja.setText((bln / 12 > 0 ? bln / 12 + " thn " : "") + bln % 12 + " bln");
    }

    private boolean validateForm() {
        if (txtNama.getText().trim().isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Nama tidak boleh kosong."); return false;
        }
        if (dcTglMasuk.getDate() == null) {
            javax.swing.JOptionPane.showMessageDialog(this, "Tanggal masuk wajib diisi."); return false;
        }
        if (!ValidationUtil.isNpwpValid(txtNpwp.getText())) {
            javax.swing.JOptionPane.showMessageDialog(this, "Format NPWP tidak valid.\nGunakan: xx.xxx.xxx.x-xxx.xxx");
            return false;
        }
        return true;
    }

    private Karyawan buildFromForm() {
        Karyawan k = new Karyawan();
        k.setNama(txtNama.getText().trim());
        k.setJenisKelamin((String) cmbJK.getSelectedItem());
        k.setTempatLahir(txtTempatLahir.getText().trim());
        k.setAlamat(txtAlamat.getText().trim());
        k.setStatusNikah((String) cmbStatusNikah.getSelectedItem());
        k.setJumlahAnak("Menikah".equals(cmbStatusNikah.getSelectedItem())
        ? (Integer) spnAnak.getValue()
        : 0);
        k.setNpwp(txtNpwp.getText().trim());
        k.setStatusKaryawan((String) cmbStatusKaryawan.getSelectedItem());
        Golongan g = (Golongan) cmbGolongan.getSelectedItem();
        if (g != null) k.setIdGolongan(g.getIdGolongan());
        if (dcTglLahir.getDate() != null)
            k.setTanggalLahir(dcTglLahir.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        if (dcTglMasuk.getDate() != null)
            k.setTanggalMasuk(dcTglMasuk.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        return k;
    }
    
    private void styleBtn(javax.swing.JButton b,
                      java.awt.Color bg,
                      java.awt.Color fg) {

    b.setBackground(bg);
    b.setForeground(fg);

    b.setFocusPainted(false);
    b.setBorderPainted(false);

    b.setOpaque(true);
    b.setContentAreaFilled(true);
}

    private void showErr(Exception e) {
        javax.swing.JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Kesalahan", javax.swing.JOptionPane.ERROR_MESSAGE);
    }

    // Variables declaration - do not modify
    private javax.swing.JButton  btnHapus;
    private javax.swing.JButton  btnReset;
    private javax.swing.JButton  btnSimpan;
    private javax.swing.JButton  btnUpdate;
    private javax.swing.JComboBox<com.payrollpro.model.Golongan> cmbGolongan;
    private javax.swing.JComboBox<String> cmbJK;
    private javax.swing.JComboBox<String> cmbStatusKaryawan;
    private javax.swing.JComboBox<String> cmbStatusNikah;
    private com.toedter.calendar.JDateChooser dcTglLahir;
    private com.toedter.calendar.JDateChooser dcTglMasuk;
    private javax.swing.JLabel   lblAlamat;
    private javax.swing.JLabel   lblAnak;
    private javax.swing.JLabel   lblGolongan;
    private javax.swing.JLabel   lblId;
    private javax.swing.JLabel   lblJK;
    private javax.swing.JLabel   lblMasaKerja;
    private javax.swing.JLabel   lblNama;
    private javax.swing.JLabel   lblNpwp;
    private javax.swing.JLabel   lblStatusKaryawan;
    private javax.swing.JLabel   lblStatusNikah;
    private javax.swing.JLabel   lblTempatLahir;
    private javax.swing.JLabel   lblTglLahir;
    private javax.swing.JLabel   lblTglMasuk;
    private javax.swing.JPanel   pnlButtons;
    private javax.swing.JPanel   pnlForm;
    private javax.swing.JScrollPane scrlTable;
    private javax.swing.JSpinner spnAnak;
    private javax.swing.JTable   tblKaryawan;
    private javax.swing.JTextField txtAlamat;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtMasaKerja;
    private javax.swing.JTextField txtNama;
    private javax.swing.JTextField txtNpwp;
    private javax.swing.JTextField txtTempatLahir;
    // End of variables declaration
}
