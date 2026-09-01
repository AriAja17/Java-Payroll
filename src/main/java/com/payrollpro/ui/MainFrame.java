package com.payrollpro.ui;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.payrollpro.ui.panels.*;
import com.payrollpro.util.RoleManager;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends javax.swing.JFrame {

    private boolean isDarkTheme = true;

    // Panel instances
    private DashboardPanel  dashboardPanel;
    private KaryawanPanel   karyawanPanel;
    private GolonganPanel   golonganPanel;
    private LemburPanel     lemburPanel;
    private PenggajianPanel penggajianPanel;
    private CutiPanel       cutiPanel;
    private THRPanel        thrPanel;
    private LaporanPanel    laporanPanel;
    private PengaturanPanel pengaturanPanel;

    private CardLayout cardLayout;
    
    private String currentPanelKey = "dashboard";

    public MainFrame() {
    initComponents();
    afterInit();
    setSize(1100, 660);
    setMinimumSize(new Dimension(900, 580));
    setLocationRelativeTo(null);
}

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        pnlSidebar    = new javax.swing.JPanel();
        lblAppName    = new javax.swing.JLabel();
        lblVersion    = new javax.swing.JLabel();
        lblDev = new javax.swing.JLabel();
        sepSidebar    = new javax.swing.JSeparator();
        btnDashboard  = new javax.swing.JButton();
        btnKaryawan   = new javax.swing.JButton();
        btnGolongan   = new javax.swing.JButton();
        btnLembur     = new javax.swing.JButton();
        btnPenggajian = new javax.swing.JButton();
        btnCuti       = new javax.swing.JButton();
        lblMenuTambahan = new javax.swing.JLabel();
        btnTHR        = new javax.swing.JButton();
        btnLaporan    = new javax.swing.JButton();
        btnPengaturan = new javax.swing.JButton();
        sepBottom     = new javax.swing.JSeparator();
        btnThemeToggle= new javax.swing.JButton();
        btnLogout     = new javax.swing.JButton();
        pnlRight      = new javax.swing.JPanel();
        pnlTopBar     = new javax.swing.JPanel();
        lblPageTitle  = new javax.swing.JLabel();
        lblUserInfo   = new javax.swing.JLabel();
        pnlContent    = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("PayrollPro - Sistem Penggajian");

        // Sidebar
        pnlSidebar.setBackground(new java.awt.Color(22, 22, 42));

        lblAppName.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        lblAppName.setForeground(new java.awt.Color(255, 255, 255));
        lblAppName.setText("PayrollPro");

        lblVersion.setForeground(new java.awt.Color(140, 140, 160));
        lblVersion.setText("v1.0 - Protoype");

        lblDev.setForeground(new java.awt.Color(120,120,140));
        lblDev.setFont(new java.awt.Font("Segoe UI", java.awt.Font.ITALIC, 10));
        lblDev.setText("Developed by Syauqi XI RPL 1");
        sepSidebar.setForeground(new java.awt.Color(60, 60, 80));

        // Sidebar nav buttons - style umum
        javax.swing.JButton[] navBtns = new javax.swing.JButton[]{};

        btnDashboard.setText("Dashboard");
        btnDashboard.setBorderPainted(false);
        btnDashboard.setFocusPainted(false);
        btnDashboard.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnDashboard.addActionListener(evt -> btnDashboardActionPerformed(evt));

        btnKaryawan.setText("Data Karyawan");
        btnKaryawan.setBorderPainted(false);
        btnKaryawan.setFocusPainted(false);
        btnKaryawan.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnKaryawan.addActionListener(evt -> btnKaryawanActionPerformed(evt));

        btnGolongan.setText("Data Golongan");
        btnGolongan.setBorderPainted(false);
        btnGolongan.setFocusPainted(false);
        btnGolongan.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnGolongan.addActionListener(evt -> btnGolonganActionPerformed(evt));

        btnLembur.setText("Data Lembur");
        btnLembur.setBorderPainted(false);
        btnLembur.setFocusPainted(false);
        btnLembur.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnLembur.addActionListener(evt -> btnLemburActionPerformed(evt));

        btnPenggajian.setText("Penggajian");
        btnPenggajian.setBorderPainted(false);
        btnPenggajian.setFocusPainted(false);
        btnPenggajian.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnPenggajian.addActionListener(evt -> btnPenggajianActionPerformed(evt));

        btnCuti.setText("Manajemen Cuti");
        btnCuti.setBorderPainted(false);
        btnCuti.setFocusPainted(false);
        btnCuti.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnCuti.addActionListener(evt -> btnCutiActionPerformed(evt));

        lblMenuTambahan.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 9));
        lblMenuTambahan.setForeground(new java.awt.Color(100, 100, 120));
        lblMenuTambahan.setText("TAMBAHAN");

        btnTHR.setText("THR Otomatis");
        btnTHR.setBorderPainted(false);
        btnTHR.setFocusPainted(false);
        btnTHR.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnTHR.addActionListener(evt -> btnTHRActionPerformed(evt));

        btnLaporan.setText("Laporan & Export");
        btnLaporan.setBorderPainted(false);
        btnLaporan.setFocusPainted(false);
        btnLaporan.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnLaporan.addActionListener(evt -> btnLaporanActionPerformed(evt));

        btnPengaturan.setText("Pengaturan");
        btnPengaturan.setBorderPainted(false);
        btnPengaturan.setFocusPainted(false);
        btnPengaturan.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnPengaturan.addActionListener(evt -> btnPengaturanActionPerformed(evt));

        sepBottom.setForeground(new java.awt.Color(60, 60, 80));

        btnThemeToggle.setText("Tema: Gelap");
        btnThemeToggle.setBorderPainted(false);
        btnThemeToggle.setFocusPainted(false);
        btnThemeToggle.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnThemeToggle.addActionListener(evt -> btnThemeToggleActionPerformed(evt));

        btnLogout.setForeground(new java.awt.Color(220, 80, 80));
        btnLogout.setText("Keluar");
        btnLogout.setBorderPainted(false);
        btnLogout.setFocusPainted(false);
        btnLogout.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnLogout.addActionListener(evt -> btnLogoutActionPerformed(evt));

        // Sidebar GroupLayout
        javax.swing.GroupLayout pnlSidebarLayout = new javax.swing.GroupLayout(pnlSidebar);
        pnlSidebar.setLayout(pnlSidebarLayout);
        pnlSidebarLayout.setHorizontalGroup(
            pnlSidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSidebarLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(pnlSidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblAppName)
                    .addComponent(lblVersion)
                    .addComponent(sepSidebar, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDashboard,  javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnKaryawan,   javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGolongan,   javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLembur,     javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPenggajian, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCuti,       javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblMenuTambahan)
                    .addComponent(btnTHR,        javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLaporan,    javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPengaturan, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sepBottom,     javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnThemeToggle,javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLogout,     javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDev))
                .addContainerGap(12, Short.MAX_VALUE))
        );
        pnlSidebarLayout.setVerticalGroup(
            pnlSidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSidebarLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(lblAppName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblVersion)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sepSidebar, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDashboard)
                .addGap(2, 2, 2)
                .addComponent(btnKaryawan)
                .addGap(2, 2, 2)
                .addComponent(btnGolongan)
                .addGap(2, 2, 2)
                .addComponent(btnLembur)
                .addGap(2, 2, 2)
                .addComponent(btnPenggajian)
                .addGap(2, 2, 2)
                .addComponent(btnCuti)
                .addGap(8, 8, 8)
                .addComponent(lblMenuTambahan)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnTHR)
                .addGap(2, 2, 2)
                .addComponent(btnLaporan)
                .addGap(2, 2, 2)
                .addComponent(btnPengaturan)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(sepBottom, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnThemeToggle)
                .addGap(2, 2, 2)
                .addComponent(btnLogout)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblDev)
                .addGap(8, 8, 8))
        );

        // TopBar
        lblPageTitle.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        lblPageTitle.setText("Dashboard");

        lblUserInfo.setText("Admin | admin");

        javax.swing.GroupLayout pnlTopBarLayout = new javax.swing.GroupLayout(pnlTopBar);
        pnlTopBar.setLayout(pnlTopBarLayout);
        pnlTopBarLayout.setHorizontalGroup(
            pnlTopBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTopBarLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(lblPageTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblUserInfo)
                .addGap(16, 16, 16))
        );
        pnlTopBarLayout.setVerticalGroup(
            pnlTopBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTopBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(lblPageTitle)
                .addComponent(lblUserInfo))
        );

        // Content panel - pakai CardLayout
        cardLayout = new CardLayout();
        pnlContent.setLayout(cardLayout);

        // pnlRight layout
        javax.swing.GroupLayout pnlRightLayout = new javax.swing.GroupLayout(pnlRight);
        pnlRight.setLayout(pnlRightLayout);
        pnlRightLayout.setHorizontalGroup(
            pnlRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlTopBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlContent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlRightLayout.setVerticalGroup(
            pnlRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRightLayout.createSequentialGroup()
                .addComponent(pnlTopBar, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(pnlContent, javax.swing.GroupLayout.DEFAULT_SIZE, 616, Short.MAX_VALUE))
        );

        // Main frame layout
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlSidebar, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(pnlRight, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlSidebar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlRight, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>

    // ===================== EVENT HANDLERS =====================

    private void btnDashboardActionPerformed(java.awt.event.ActionEvent evt) {
        showPanel("dashboard", "Dashboard");
    }
    private void btnKaryawanActionPerformed(java.awt.event.ActionEvent evt) {
        showPanel("karyawan", "Data Karyawan");
    }
    private void btnGolonganActionPerformed(java.awt.event.ActionEvent evt) {
        showPanel("golongan", "Data Golongan");
    }
    private void btnLemburActionPerformed(java.awt.event.ActionEvent evt) {
        showPanel("lembur", "Data Lembur");
    }
    private void btnPenggajianActionPerformed(java.awt.event.ActionEvent evt) {
        showPanel("penggajian", "Penggajian");
    }
    private void btnCutiActionPerformed(java.awt.event.ActionEvent evt) {
        showPanel("cuti", "Manajemen Cuti");
    }
    private void btnTHRActionPerformed(java.awt.event.ActionEvent evt) {
        showPanel("thr", "THR Otomatis");
    }
    private void btnLaporanActionPerformed(java.awt.event.ActionEvent evt) {
        showPanel("laporan", "Laporan & Export");
    }
    private void btnPengaturanActionPerformed(java.awt.event.ActionEvent evt) {
        showPanel("pengaturan", "Pengaturan");
    }

    private void btnThemeToggleActionPerformed(java.awt.event.ActionEvent evt) {
        toggleTheme();
    }

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {
        int ok = JOptionPane.showConfirmDialog(this,
            "Yakin ingin keluar?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.YES_OPTION) {
            RoleManager.getInstance().logout();
            dispose();
            SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
        }
    }

    // ===================== CUSTOM LOGIC =====================

    private void afterInit() {
        // Init semua panel
        dashboardPanel  = new DashboardPanel(this);
        karyawanPanel   = new KaryawanPanel(this);
        golonganPanel   = new GolonganPanel(this);
        lemburPanel     = new LemburPanel(this);
        penggajianPanel = new PenggajianPanel(this);
        cutiPanel       = new CutiPanel(this);
        thrPanel        = new THRPanel(this);
        laporanPanel    = new LaporanPanel(this);
        pengaturanPanel = new PengaturanPanel(this);

        pnlContent.add(dashboardPanel,  "dashboard");
        pnlContent.add(karyawanPanel,   "karyawan");
        pnlContent.add(golonganPanel,   "golongan");
        pnlContent.add(lemburPanel,     "lembur");
        pnlContent.add(penggajianPanel, "penggajian");
        pnlContent.add(cutiPanel,       "cuti");
        pnlContent.add(thrPanel,        "thr");
        pnlContent.add(laporanPanel,    "laporan");
        pnlContent.add(pengaturanPanel, "pengaturan");

        // Set info user
        String username = RoleManager.getInstance().getUsername();
        String role     = RoleManager.getInstance().isAdmin() ? "Admin" : "Operator";
        lblUserInfo.setText(role + " | " + username);

        // Sembunyikan Pengaturan kalau bukan admin
        btnPengaturan.setVisible(RoleManager.getInstance().isAdmin());

        // Style sidebar buttons
        applySidebarStyle();

        // Set ukuran default
        setSize(1100, 660);
        setMinimumSize(new Dimension(900, 580));

        // Tampilkan dashboard
        showPanel("dashboard", "Dashboard");
    }

    public void showPanel(String key, String title) {
        cardLayout.show(pnlContent, key);
        lblPageTitle.setText(title);
        highlightActiveBtn(key);
        currentPanelKey = key;
        
        // Refresh panel
        switch (key) {
            case "dashboard"  -> dashboardPanel.refresh();
            case "karyawan"   -> karyawanPanel.refresh();
            case "golongan"   -> golonganPanel.refresh();
            case "lembur"     -> lemburPanel.refresh();
            case "penggajian" -> penggajianPanel.refresh();
            case "cuti"       -> cutiPanel.refresh();
            case "thr"        -> thrPanel.refresh();
            case "laporan"    -> laporanPanel.refresh();
            case "pengaturan" -> pengaturanPanel.refresh();
        }
    }

    private void toggleTheme() {
        try {
            if (isDarkTheme) {
                FlatLightLaf.setup();
                btnThemeToggle.setText("Tema: Gelap");
                isDarkTheme = false;
            } else {
                FlatDarkLaf.setup();
                btnThemeToggle.setText("Tema: Terang");
                isDarkTheme = true;
            }

            SwingUtilities.updateComponentTreeUI(this);
            applySidebarStyle();
            highlightActiveBtn(currentPanelKey);
            repaint();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal ganti tema: " + e.getMessage());
        }
    }

    private void applySidebarStyle() {
        Color sidebarBg;
        Color textNormal;
        Color textDim;
        Color dangerFg = new Color(220, 80, 80);

        if (isDarkTheme) {
            sidebarBg  = new Color(22, 22, 42);
            textNormal = new Color(235, 235, 245);
            textDim    = new Color(140, 140, 160);
        } else {
            sidebarBg  = UIManager.getColor("Panel.background");
            if (sidebarBg == null) sidebarBg = new Color(245, 245, 245);

            textNormal = UIManager.getColor("Label.foreground");
            if (textNormal == null) textNormal = new Color(40, 40, 40);

            textDim = new Color(110, 110, 120);
        }

        pnlSidebar.setBackground(sidebarBg);
        lblAppName.setForeground(textNormal);
        lblVersion.setForeground(textDim);
        lblMenuTambahan.setForeground(textDim);

        javax.swing.JButton[] navBtns = {
            btnDashboard, btnKaryawan, btnGolongan, btnLembur,
            btnPenggajian, btnCuti, btnTHR, btnLaporan,
            btnPengaturan, btnThemeToggle
        };

        for (javax.swing.JButton btn : navBtns) {
            btn.setBackground(sidebarBg);
            btn.setForeground(textNormal);
            btn.setOpaque(true);
            btn.setBorderPainted(false);
            btn.setFocusPainted(false);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        btnLogout.setBackground(sidebarBg);
        btnLogout.setForeground(dangerFg);
        btnLogout.setOpaque(true);
        btnLogout.setBorderPainted(false);
        btnLogout.setFocusPainted(false);
        btnLogout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private void highlightActiveBtn(String key) {
        Color sidebarBg;
        Color activeColor;
        Color activeFg;
        Color normalFg;

        if (isDarkTheme) {
            sidebarBg   = new Color(22, 22, 42);
            activeColor = new Color(50, 50, 80);
            activeFg    = Color.WHITE;
            normalFg    = new Color(235, 235, 245);
        } else {
            sidebarBg   = UIManager.getColor("Panel.background");
            if (sidebarBg == null) sidebarBg = new Color(245, 245, 245);

            activeColor = new Color(220, 230, 245);
            activeFg    = UIManager.getColor("Label.foreground");
            if (activeFg == null) activeFg = new Color(40, 40, 40);

            normalFg    = UIManager.getColor("Label.foreground");
            if (normalFg == null) normalFg = new Color(40, 40, 40);
        }

        java.util.Map<String, javax.swing.JButton> btnMap = new java.util.LinkedHashMap<>();
        btnMap.put("dashboard",  btnDashboard);
        btnMap.put("karyawan",   btnKaryawan);
        btnMap.put("golongan",   btnGolongan);
        btnMap.put("lembur",     btnLembur);
        btnMap.put("penggajian", btnPenggajian);
        btnMap.put("cuti",       btnCuti);
        btnMap.put("thr",        btnTHR);
        btnMap.put("laporan",    btnLaporan);
        btnMap.put("pengaturan", btnPengaturan);

        for (java.util.Map.Entry<String, javax.swing.JButton> e : btnMap.entrySet()) {
            if (e.getKey().equals(key)) {
                e.getValue().setBackground(activeColor);
                e.getValue().setForeground(activeFg);
            } else {
                e.getValue().setBackground(sidebarBg);
                e.getValue().setForeground(normalFg);
            }
        }
    }

    // Variables declaration - do not modify
    private javax.swing.JButton btnCuti;
    private javax.swing.JButton btnDashboard;
    private javax.swing.JButton btnGolongan;
    private javax.swing.JButton btnKaryawan;
    private javax.swing.JButton btnLaporan;
    private javax.swing.JButton btnLembur;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnPenggajian;
    private javax.swing.JButton btnPengaturan;
    private javax.swing.JButton btnTHR;
    private javax.swing.JButton btnThemeToggle;
    private javax.swing.JLabel  lblAppName;
    private javax.swing.JLabel  lblMenuTambahan;
    private javax.swing.JLabel  lblPageTitle;
    private javax.swing.JLabel  lblUserInfo;
    private javax.swing.JLabel  lblVersion;
    private javax.swing.JPanel  pnlContent;
    private javax.swing.JPanel  pnlRight;
    private javax.swing.JPanel  pnlSidebar;
    private javax.swing.JPanel  pnlTopBar;
    private javax.swing.JSeparator sepBottom;
    private javax.swing.JSeparator sepSidebar;
    private javax.swing.JLabel lblDev;
    // End of variables declaration
}
