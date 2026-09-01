package com.payrollpro.ui.panels;

import com.payrollpro.dao.*;
import com.payrollpro.model.*;
import com.payrollpro.ui.MainFrame;
import com.payrollpro.util.ValidationUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

public class DashboardPanel extends JPanel {

    private final MainFrame mainFrame;

    // Stat cards
    private JLabel lblKaryawan, lblCutiPending, lblGajiProses, lblTHRPending;

    // Notifikasi panel
    private JPanel  pnlNotif;
    private JLabel  lblNotifTHR;
    private JLabel  lblNotifCuti;

    // Tabel
    private JTable             tblGaji, tblCuti;
    private DefaultTableModel  modelGaji, modelCuti;

    public DashboardPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout(0, 12));
        setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));
        build();
    }

    private void build() {
        // === Stat cards ===
        lblKaryawan   = new JLabel("0");
        lblCutiPending= new JLabel("0");
        lblGajiProses = new JLabel("0");
        lblTHRPending = new JLabel("0");

        JPanel cardRow = new JPanel(new GridLayout(1, 4, 10, 0));
        cardRow.setOpaque(false);
        cardRow.add(statCard("Karyawan Aktif",  lblKaryawan,    new Color(30, 100, 180)));
        cardRow.add(statCard("Cuti Pending",     lblCutiPending, new Color(180, 100, 20)));
        cardRow.add(statCard("Gaji Diproses",    lblGajiProses,  new Color(30, 140, 80)));
        cardRow.add(statCard("THR Belum Lunas",  lblTHRPending,  new Color(140, 30, 140)));

        // === Panel notifikasi THR + Cuti (BARU) ===
        pnlNotif = new JPanel();
        pnlNotif.setLayout(new BoxLayout(pnlNotif, BoxLayout.Y_AXIS));
        pnlNotif.setOpaque(false);

        lblNotifTHR  = new JLabel(" ");
        lblNotifCuti = new JLabel(" ");
        lblNotifTHR.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblNotifCuti.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblNotifTHR.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
        lblNotifCuti.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));

        pnlNotif.add(lblNotifTHR);
        pnlNotif.add(lblNotifCuti);

        // === Tabel bawah ===
        String[] colsGaji = {"Nama", "Golongan", "Periode", "Total Bersih", "Status"};
        modelGaji = new DefaultTableModel(colsGaji, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblGaji = buildTable(modelGaji);

        String[] colsCuti = {"Karyawan", "Jenis Cuti", "Tanggal Mulai", "Hari", "Status"};
        modelCuti = new DefaultTableModel(colsCuti, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblCuti = buildTable(modelCuti);

        JPanel botRow = new JPanel(new GridLayout(1, 2, 12, 0));
        botRow.setOpaque(false);
        botRow.add(section("Ringkasan Gaji Bulan Ini", tblGaji));
        botRow.add(section("Cuti Pending Persetujuan", tblCuti));

        // === Top section: cards + notif ===
        JPanel topSection = new JPanel(new BorderLayout(0, 8));
        topSection.setOpaque(false);
        topSection.add(cardRow,   BorderLayout.NORTH);
        topSection.add(pnlNotif,  BorderLayout.CENTER);

        add(topSection, BorderLayout.NORTH);
        add(botRow,     BorderLayout.CENTER);
    }

    public void refresh() {
        new SwingWorker<Void, Void>() {
            int aktif, pending, proses, thrBelum;
            List<Penggajian>     gajiList;
            List<PengajuanCuti>  cutiList;
            String notifTHR  = " ";
            String notifCuti = " ";

            @Override protected Void doInBackground() throws Exception {
                aktif    = new KaryawanDAO().getAllAktif().size();
                cutiList = new CutiDAO().getPending();
                pending  = cutiList.size();

                LocalDate now = LocalDate.now();
                gajiList = new PenggajianDAO().getAllByPeriode(now.getMonthValue(), now.getYear());
                proses   = gajiList.size();

                // THR pending count
                THRDAO thrDAO = new THRDAO();
                List<THR> thrList = thrDAO.getByTahunJenis(now.getYear(), "Idul Fitri");
                thrBelum = (int) thrList.stream().filter(t -> "Belum".equals(t.getStatusBayar())).count();

                // === NOTIFIKASI THR DEADLINE ===
                // Idul Fitri biasanya bulan Maret-April, Natal bulan Desember
                // Warning muncul H-30 sebelum perkiraan deadline
                Month bulanSekarang = now.getMonth();
                if (bulanSekarang == Month.MARCH || bulanSekarang == Month.FEBRUARY) {
                    long sisaHari = java.time.temporal.ChronoUnit.DAYS.between(now,
                        LocalDate.of(now.getYear(), Month.MARCH, 24));
                    if (sisaHari >= 0 && sisaHari <= 30) {
                        notifTHR = "THR Idul Fitri " + now.getYear() +
                            " wajib dibayar paling lambat H-7 sebelum Lebaran. " +
                            "Segera generate dari menu THR Otomatis!";
                    }
                } else if (bulanSekarang == Month.DECEMBER) {
                    notifTHR = "THR Natal " + now.getYear() +
                        " wajib dibayar paling lambat H-7 sebelum Natal (18 Desember). " +
                        "Segera generate dari menu THR Otomatis!";
                }

                // === NOTIFIKASI CUTI PENDING ===
                if (pending > 0) {
                    notifCuti = pending + " pengajuan cuti menunggu persetujuan admin.";
                }

                return null;
            }

            @Override protected void done() {
                try {
                    get();
                    lblKaryawan.setText(String.valueOf(aktif));
                    lblCutiPending.setText(String.valueOf(pending));
                    lblGajiProses.setText(String.valueOf(proses));
                    lblTHRPending.setText(String.valueOf(thrBelum));

                    // Tampilkan notifikasi THR
                    if (!" ".equals(notifTHR)) {
                        lblNotifTHR.setText(notifTHR);
                        lblNotifTHR.setOpaque(true);
                        lblNotifTHR.setBackground(new Color(255, 243, 205));
                        lblNotifTHR.setForeground(new Color(133, 77, 14));
                        lblNotifTHR.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(new Color(250, 199, 117)),
                            BorderFactory.createEmptyBorder(6, 10, 6, 10)));
                    } else {
                        lblNotifTHR.setText(" ");
                        lblNotifTHR.setOpaque(false);
                        lblNotifTHR.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
                    }

                    // Tampilkan notifikasi cuti
                    if (!" ".equals(notifCuti)) {
                        lblNotifCuti.setText(notifCuti);
                        lblNotifCuti.setOpaque(true);
                        lblNotifCuti.setBackground(new Color(219, 234, 254));
                        lblNotifCuti.setForeground(new Color(30, 64, 175));
                        lblNotifCuti.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(new Color(147, 197, 253)),
                            BorderFactory.createEmptyBorder(6, 10, 6, 10)));
                    } else {
                        lblNotifCuti.setText(" ");
                        lblNotifCuti.setOpaque(false);
                        lblNotifCuti.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
                    }

                    // Isi tabel gaji
                    modelGaji.setRowCount(0);
                    for (Penggajian p : gajiList) {
                        modelGaji.addRow(new Object[]{
                            p.getNamaKaryawan(),
                            p.getIdKaryawan(),
                            ValidationUtil.getPeriodeLabel(p.getPeriodeBulan(), p.getPeriodeTahun()),
                            ValidationUtil.formatRupiah(p.getGajiBersih()),
                            p.getStatusBayar()
                        });
                    }

                    // Isi tabel cuti
                    modelCuti.setRowCount(0);
                    for (PengajuanCuti c : cutiList) {
                        modelCuti.addRow(new Object[]{
                            c.getNamaKaryawan(), c.getNamaJenis(),
                            c.getTglMulai(),
                            c.getJumlahHari() + " hari",
                            c.getStatus()
                        });
                    }

                    revalidate(); repaint();

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(DashboardPanel.this,
                        "Gagal memuat dashboard: " + e.getMessage());
                }
            }
        }.execute();
    }

    // ===================== HELPERS =====================
    private JPanel statCard(String label, JLabel valueLabel, Color accent) {
        JPanel card = new JPanel(new BorderLayout(0, 4));
        card.setBackground(UIManager.getColor("Panel.background"));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIManager.getColor("Separator.foreground")),
            BorderFactory.createEmptyBorder(12, 14, 12, 14)));

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lbl.setForeground(UIManager.getColor("Label.disabledForeground"));

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueLabel.setForeground(accent);

        card.add(lbl,        BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    private JPanel section(String title, JTable table) {
        JPanel p = new JPanel(new BorderLayout(0, 6));
        p.setOpaque(false);
        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        p.add(lbl, BorderLayout.NORTH);
        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createLineBorder(
            UIManager.getColor("Separator.foreground")));
        p.add(sp, BorderLayout.CENTER);
        return p;
    }

    private JTable buildTable(DefaultTableModel model) {
        JTable t = new JTable(model);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));
        t.setRowHeight(24);
        t.setShowGrid(false);
        t.setIntercellSpacing(new Dimension(0, 0));
        return t;
    }
}
