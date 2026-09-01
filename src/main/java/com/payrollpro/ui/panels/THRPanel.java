package com.payrollpro.ui.panels;

import com.payrollpro.dao.KaryawanDAO;
import com.payrollpro.dao.THRDAO;
import com.payrollpro.model.Karyawan;
import com.payrollpro.model.THR;
import com.payrollpro.service.AuditService;
import com.payrollpro.service.THRCalculator;
import com.payrollpro.ui.MainFrame;
import com.payrollpro.util.RoleManager;
import com.payrollpro.util.ValidationUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class THRPanel extends JPanel {

    private final MainFrame    mainFrame;
    private final THRDAO       dao          = new THRDAO();
    private final KaryawanDAO  karyawanDAO  = new KaryawanDAO();

    private JComboBox<String>  cbJenisHariRaya;
    private JSpinner           spnTahun;
    private JLabel             lblInfo;
    private JButton            btnGenerate, btnLunas, btnExport;
    private JTable             table;
    private DefaultTableModel  tableModel;

    public THRPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout(0, 12));
        setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));
        build();
    }

    private void build() {
        // --- Filter bar ---
        JPanel filterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        filterBar.setBorder(BorderFactory.createTitledBorder("Generate THR"));

        cbJenisHariRaya = new JComboBox<>(new String[]{
            "Idul Fitri", "Natal", "Tahun Baru"
        });
        spnTahun = new JSpinner(new SpinnerNumberModel(LocalDate.now().getYear(), 2020, 2100, 1));
        btnGenerate = new JButton("Generate Semua THR");
        btnLunas    = new JButton("Tandai Lunas");
        btnExport   = new JButton("Export Excel");

        styleBtn(btnGenerate, new Color(30, 100, 180), Color.WHITE);
        styleBtn(btnLunas,    new Color(30, 140, 80),  Color.WHITE);
        styleBtn(btnExport,   new Color(100, 60, 160), Color.WHITE);

        filterBar.add(new JLabel("Jenis:"));
        filterBar.add(cbJenisHariRaya);
        filterBar.add(new JLabel("Tahun:"));
        filterBar.add(spnTahun);
        filterBar.add(btnGenerate);
        filterBar.add(btnLunas);
        filterBar.add(btnExport);

        // --- Info box ---
        lblInfo = new JLabel(
            "<html><b>Dasar Hukum: PP No.36/2021</b><br>" +
            "Masa kerja &ge; 12 bulan = 1x gaji sebulan (gaji pokok + tunjangan tetap)<br>" +
            "Masa kerja 1-11 bulan = prorata (masa_kerja/12) x gaji sebulan<br>" +
            "Masa kerja &lt; 1 bulan = belum berhak THR</html>"
        );
        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblInfo.setOpaque(true);
        lblInfo.setBackground(new Color(230, 240, 255));
        lblInfo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 200, 240)),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        // --- Tabel ---
        String[] cols = {
            "ID", "Karyawan", "Masa Kerja", "Gaji Pokok",
            "Faktor", "Nominal THR", "Status"
        };
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));
        table.setRowHeight(24);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Warna baris berdasarkan status
        table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean isSelected, boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, col);
                if (!isSelected) {
                    String status = (String) tableModel.getValueAt(row, 6);
                    if ("Lunas".equals(status)) {
                        c.setBackground(new Color(235, 248, 235));
                    } else {
                        c.setBackground(table.getBackground());
                    }
                }
                return c;
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createTitledBorder("Daftar THR Karyawan"));

        // --- Layout ---
        JPanel topSection = new JPanel(new BorderLayout(0, 8));
        topSection.add(filterBar, BorderLayout.NORTH);
        topSection.add(lblInfo, BorderLayout.CENTER);

        add(topSection, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        // --- Events ---
        btnGenerate.addActionListener(e -> generateTHR());
        btnLunas.addActionListener(e -> tandaiLunas());
        btnExport.addActionListener(e -> exportExcel());
        cbJenisHariRaya.addActionListener(e -> loadTable());
        spnTahun.addChangeListener(e -> loadTable());

        boolean admin = RoleManager.getInstance().isAdmin();
        btnGenerate.setEnabled(admin);
        btnLunas.setEnabled(admin);
    }

    public void refresh() {
        loadTable();
    }

    private void loadTable() {
        String jenis = (String) cbJenisHariRaya.getSelectedItem();
        int tahun    = (int) spnTahun.getValue();

        new SwingWorker<List<THR>, Void>() {
            @Override protected List<THR> doInBackground() throws Exception {
                return dao.getByTahunJenis(tahun, jenis);
            }
            @Override protected void done() {
                try {
                    tableModel.setRowCount(0);
                    for (THR t : get()) {
                        String faktorStr = t.getMasaKerjaBulan() >= 12
                            ? "1x (penuh)"
                            : t.getMasaKerjaBulan() + "/12 (prorata)";
                        tableModel.addRow(new Object[]{
                            t.getIdThr(),
                            t.getNamaKaryawan(),
                            t.getMasaKerjaBulan() + " bulan",
                            ValidationUtil.formatRupiah(t.getGajiPokok()),
                            faktorStr,
                            ValidationUtil.formatRupiah(t.getNominalThr()),
                            t.getStatusBayar()
                        });
                    }
                } catch (Exception e) { showErr(e); }
            }
        }.execute();
    }

    private void generateTHR() {
        String jenis = (String) cbJenisHariRaya.getSelectedItem();
        int tahun    = (int) spnTahun.getValue();

        int confirm = JOptionPane.showConfirmDialog(this,
            "Generate THR " + jenis + " " + tahun + " untuk semua karyawan aktif?\n" +
            "Data yang sudah ada akan diperbarui.",
            "Konfirmasi Generate THR", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        new SwingWorker<Integer, Void>() {
            @Override protected Integer doInBackground() throws Exception {
                List<Karyawan> aktif = karyawanDAO.getAllAktif();
                int count = 0;
                for (Karyawan k : aktif) {
                    if (k.getGolongan() == null) continue;
                    int masaKerja = k.getMasaKerjaBulan();
                    if (masaKerja < 1) continue; // belum berhak

                    double tunjanganTetap = 0;
                    if (!k.isProba() && k.isMenikah()) {
                        tunjanganTetap += k.getGolongan().getTunjanganIstri();
                        tunjanganTetap += Math.min(k.getJumlahAnak(), 3) * k.getGolongan().getTunjanganAnak();
                    }

                    THRCalculator.Result r = THRCalculator.hitung(
                        k.getGolongan().getGajiPokok(), tunjanganTetap, masaKerja
                    );

                    THR thr = new THR();
                    thr.setIdKaryawan(k.getIdKaryawan());
                    thr.setTahun(tahun);
                    thr.setJenisHariRaya(jenis);
                    thr.setMasaKerjaBulan(masaKerja);
                    thr.setGajiPokok(k.getGolongan().getGajiPokok());
                    thr.setFaktor(r.faktor);
                    thr.setNominalThr(r.nominalThr);
                    dao.insertOrUpdate(thr);
                    count++;
                }
                AuditService.log("INSERT", "thr", jenis + "_" + tahun,
                    "Generate THR " + jenis + " " + tahun + " (" + count + " karyawan)");
                return count;
            }
            @Override protected void done() {
                try {
                    int count = get();
                    JOptionPane.showMessageDialog(THRPanel.this,
                        "Berhasil generate THR untuk " + count + " karyawan.");
                    loadTable();
                } catch (Exception e) { showErr(e); }
            }
        }.execute();
    }

    private void tandaiLunas() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Pilih karyawan dahulu."); return;
        }
        int idThr = (int) tableModel.getValueAt(row, 0);
        String nama = (String) tableModel.getValueAt(row, 1);
        int ok = JOptionPane.showConfirmDialog(this,
            "Tandai THR " + nama + " sebagai Lunas?",
            "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (ok != JOptionPane.YES_OPTION) return;

        new SwingWorker<Void, Void>() {
            @Override protected Void doInBackground() throws Exception {
                dao.tandaiLunas(idThr);
                AuditService.logUpdate("thr", String.valueOf(idThr));
                return null;
            }
            @Override protected void done() {
                try { get(); loadTable(); } catch (Exception e) { showErr(e); }
            }
        }.execute();
    }

    private void exportExcel() {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setDialogTitle("Pilih folder output");
        if (fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;

        String jenis = (String) cbJenisHariRaya.getSelectedItem();
        int tahun    = (int) spnTahun.getValue();

        new SwingWorker<java.io.File, Void>() {
            @Override protected java.io.File doInBackground() throws Exception {
                // Simple CSV export sebagai fallback jika POI belum ready
                List<THR> list = dao.getByTahunJenis(tahun, jenis);
                String filename = "thr_" + jenis.replace(" ","_") + "_" + tahun + ".csv";
                java.io.File f  = new java.io.File(fc.getSelectedFile(), filename);
                try (java.io.PrintWriter pw = new java.io.PrintWriter(f, "UTF-8")) {
                    pw.println("ID,Karyawan,Masa Kerja (Bln),Gaji Pokok,Faktor,Nominal THR,Status");
                    for (THR t : list) {
                        pw.printf("%d,%s,%d,%.0f,%.4f,%.0f,%s%n",
                            t.getIdThr(), t.getNamaKaryawan(), t.getMasaKerjaBulan(),
                            t.getGajiPokok(), t.getFaktor(), t.getNominalThr(), t.getStatusBayar());
                    }
                }
                AuditService.logExport("THR " + jenis + " " + tahun);
                return f;
            }
            @Override protected void done() {
                try {
                    java.io.File f = get();
                    JOptionPane.showMessageDialog(THRPanel.this,
                        "Export berhasil:\n" + f.getAbsolutePath());
                } catch (Exception e) { showErr(e); }
            }
        }.execute();
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
