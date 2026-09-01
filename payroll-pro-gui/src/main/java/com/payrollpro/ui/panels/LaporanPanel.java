package com.payrollpro.ui.panels;

import com.payrollpro.dao.AuditLogDAO;
import com.payrollpro.dao.PenggajianDAO;
import com.payrollpro.model.AuditLog;
import com.payrollpro.model.Penggajian;
import com.payrollpro.service.AuditService;
import com.payrollpro.ui.MainFrame;
import com.payrollpro.util.DatabaseBackup;
import com.payrollpro.util.ExcelExporter;
import com.payrollpro.util.ValidationUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class LaporanPanel extends JPanel {

    private final MainFrame      mainFrame;
    private final PenggajianDAO  penggajianDAO = new PenggajianDAO();
    private final AuditLogDAO    auditDAO      = new AuditLogDAO();

    private JTabbedPane tabs;

    // Tab Rekap
    private JComboBox<Integer> cbBulan;
    private JSpinner spnTahun;
    private JButton btnExcelRekap, btnBackup, btnRefreshRekap;
    private JTable tblRekap;
    private DefaultTableModel modelRekap;
    private JLabel lblTotalBersih;

    // Tab Audit Log
    private JTable tblAudit;
    private DefaultTableModel modelAudit;
    private JButton btnRefreshAudit;

    public LaporanPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));
        build();
    }

    private void build() {
        tabs = new JTabbedPane();

        // =================== TAB 1: REKAP BULANAN ===================
        JPanel panelRekap = new JPanel(new BorderLayout(0, 8));

        // Filter bar
        JPanel filterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        filterBar.setBorder(BorderFactory.createTitledBorder("Filter Periode"));
        cbBulan  = new JComboBox<>(new Integer[]{1,2,3,4,5,6,7,8,9,10,11,12});
        spnTahun = new JSpinner(new SpinnerNumberModel(LocalDate.now().getYear(), 2020, 2100, 1));
        cbBulan.setSelectedItem(LocalDate.now().getMonthValue());

        btnRefreshRekap = new JButton("Tampilkan");
        btnExcelRekap   = new JButton("Export Excel (.xlsx)");
        btnBackup       = new JButton("Backup Database (.sql)");

        styleBtn(btnRefreshRekap, new Color(30, 100, 180), Color.WHITE);
        styleBtn(btnExcelRekap,   new Color(30, 140, 80),  Color.WHITE);
        styleBtn(btnBackup,       new Color(120, 60, 20),  Color.WHITE);

        filterBar.add(new JLabel("Bulan:"));
        filterBar.add(cbBulan);
        filterBar.add(new JLabel("Tahun:"));
        filterBar.add(spnTahun);
        filterBar.add(btnRefreshRekap);
        filterBar.add(btnExcelRekap);
        filterBar.add(btnBackup);

        // Tabel rekap
        String[] colsRekap = {
            "Karyawan", "Gaji Pokok", "Tunjangan", "Lembur",
            "PPh 21", "BPJS Kary.", "Total Bruto", "Potongan", "Bersih", "Status"
        };
        modelRekap = new DefaultTableModel(colsRekap, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblRekap = buildTable(modelRekap);

        lblTotalBersih = new JLabel("Total Gaji Bersih: -");
        lblTotalBersih.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTotalBersih.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));

        JScrollPane spRekap = new JScrollPane(tblRekap);
        spRekap.setBorder(BorderFactory.createLineBorder(UIManager.getColor("Separator.foreground")));

        panelRekap.add(filterBar, BorderLayout.NORTH);
        panelRekap.add(spRekap, BorderLayout.CENTER);
        panelRekap.add(lblTotalBersih, BorderLayout.SOUTH);

        // =================== TAB 2: AUDIT LOG ===================
        JPanel panelAudit = new JPanel(new BorderLayout(0, 8));

        JPanel auditBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        btnRefreshAudit = new JButton("Refresh");
        styleBtn(btnRefreshAudit, new Color(30, 100, 180), Color.WHITE);
        auditBar.add(btnRefreshAudit);
        auditBar.add(new JLabel("  Menampilkan 100 aktivitas terakhir"));

        String[] colsAudit = {"Waktu", "User", "Aksi", "Tabel", "Record", "Keterangan"};
        modelAudit = new DefaultTableModel(colsAudit, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblAudit = buildTable(modelAudit);

        // Warna per jenis aksi
        tblAudit.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean isSelected, boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(t,value,isSelected,hasFocus,row,col);
                if (!isSelected) {
                    String aksi = (String) modelAudit.getValueAt(row, 2);
                    if (aksi == null) aksi = "";
                    switch (aksi) {
                        case "DELETE" -> c.setBackground(new Color(255, 235, 235));
                        case "LOCK"   -> c.setBackground(new Color(255, 245, 225));
                        case "INSERT" -> c.setBackground(new Color(235, 250, 235));
                        case "LOGIN"  -> c.setBackground(new Color(235, 245, 255));
                        default       -> c.setBackground(tblAudit.getBackground());
                    }
                }
                return c;
            }
        });

        JScrollPane spAudit = new JScrollPane(tblAudit);
        spAudit.setBorder(BorderFactory.createLineBorder(UIManager.getColor("Separator.foreground")));

        panelAudit.add(auditBar, BorderLayout.NORTH);
        panelAudit.add(spAudit, BorderLayout.CENTER);

        // =================== ASSEMBLE ===================
        tabs.addTab("Rekap Bulanan", panelRekap);
        tabs.addTab("Audit Log",     panelAudit);
        add(tabs, BorderLayout.CENTER);

        // Events
        btnRefreshRekap.addActionListener(e -> loadRekap());
        btnExcelRekap.addActionListener(e -> exportExcel());
        btnBackup.addActionListener(e -> backupDatabase());
        btnRefreshAudit.addActionListener(e -> loadAudit());
    }

    public void refresh() {
        loadRekap();
        loadAudit();
    }

    private void loadRekap() {
        int bulan = (int) cbBulan.getSelectedItem();
        int tahun = (int) spnTahun.getValue();

        new SwingWorker<List<Penggajian>, Void>() {
            @Override protected List<Penggajian> doInBackground() throws Exception {
                return penggajianDAO.getAllByPeriode(bulan, tahun);
            }
            @Override protected void done() {
                try {
                    List<Penggajian> list = get();
                    modelRekap.setRowCount(0);
                    double totalBersih = 0;

                    for (Penggajian p : list) {
                        double tunjangan = p.getTunjanganIstri() + p.getTunjanganAnak()
                                         + p.getTransport() + p.getUangMakan();
                        double bpjsKary  = p.getBpjsKesKaryawan() + p.getBpjsJhtKaryawan()
                                         + p.getBpjsJpKaryawan();
                        modelRekap.addRow(new Object[]{
                            p.getNamaKaryawan(),
                            ValidationUtil.formatRupiah(p.getGajiPokok()),
                            ValidationUtil.formatRupiah(tunjangan),
                            ValidationUtil.formatRupiah(p.getUpahLembur()),
                            ValidationUtil.formatRupiah(p.getPph21()),
                            ValidationUtil.formatRupiah(bpjsKary),
                            ValidationUtil.formatRupiah(p.getTotalBruto()),
                            ValidationUtil.formatRupiah(p.getTotalPotongan()),
                            ValidationUtil.formatRupiah(p.getGajiBersih()),
                            p.getStatusBayar()
                        });
                        totalBersih += p.getGajiBersih();
                    }

                    lblTotalBersih.setText(
                        "Total Gaji Bersih " + ValidationUtil.getPeriodeLabel(bulan, tahun) +
                        ": " + ValidationUtil.formatRupiah(totalBersih) +
                        "  (" + list.size() + " karyawan)"
                    );
                } catch (Exception e) { showErr(e); }
            }
        }.execute();
    }

    private void loadAudit() {
        new SwingWorker<List<AuditLog>, Void>() {
            @Override protected List<AuditLog> doInBackground() throws Exception {
                return auditDAO.getRecent(100);
            }
            @Override protected void done() {
                try {
                    modelAudit.setRowCount(0);
                    for (AuditLog a : get()) {
                        modelAudit.addRow(new Object[]{
                            a.getCreatedAt() != null
                                ? a.getCreatedAt().toString().replace("T", " ")
                                : "-",
                            a.getUsername(),
                            a.getAksi(),
                            a.getTabel(),
                            a.getIdRecord(),
                            a.getKeterangan()
                        });
                    }
                } catch (Exception e) { showErr(e); }
            }
        }.execute();
    }

    private void exportExcel() {
        int bulan = (int) cbBulan.getSelectedItem();
        int tahun = (int) spnTahun.getValue();

        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setDialogTitle("Pilih folder output Excel");
        if (fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;

        new SwingWorker<java.io.File, Void>() {
            @Override protected java.io.File doInBackground() throws Exception {
                List<Penggajian> list = penggajianDAO.getAllByPeriode(bulan, tahun);
                java.io.File f = ExcelExporter.exportRekapGaji(
                    list, bulan, tahun, fc.getSelectedFile().getAbsolutePath()
                );
                AuditService.logExport("Rekap gaji " + ValidationUtil.getPeriodeLabel(bulan, tahun));
                return f;
            }
            @Override protected void done() {
                try {
                    java.io.File f = get();
                    JOptionPane.showMessageDialog(LaporanPanel.this,
                        "Export Excel berhasil:\n" + f.getAbsolutePath());
                } catch (Exception e) { showErr(e); }
            }
        }.execute();
    }

    private void backupDatabase() {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setDialogTitle("Pilih folder penyimpanan backup");
        if (fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;

        new SwingWorker<java.io.File, Void>() {
            @Override protected java.io.File doInBackground() throws Exception {
                java.io.File f = DatabaseBackup.backup(fc.getSelectedFile().getAbsolutePath());
                AuditService.logBackup();
                return f;
            }
            @Override protected void done() {
                try {
                    java.io.File f = get();
                    JOptionPane.showMessageDialog(LaporanPanel.this,
                        "Backup database berhasil:\n" + f.getAbsolutePath());
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(LaporanPanel.this,
                        "Backup gagal: " + e.getMessage() + "\n\n" +
                        "Pastikan 'mysqldump' tersedia di PATH sistem.\n" +
                        "XAMPP: tambahkan C:\\xampp\\mysql\\bin ke System PATH.",
                        "Backup Gagal", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
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
