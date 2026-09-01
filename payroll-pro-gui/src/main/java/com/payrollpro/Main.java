package com.payrollpro;

import com.formdev.flatlaf.FlatDarkLaf;
import com.payrollpro.config.DatabaseConfig;
import com.payrollpro.ui.LoginFrame;
import com.payrollpro.ui.SplashScreen;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        // =========================
        // LOOK AND FEEL
        // =========================
        try {

            FlatDarkLaf.setup();

            UIManager.put("Button.arc", 8);
            UIManager.put("Component.arc", 6);
            UIManager.put("TextComponent.arc", 6);

            UIManager.put("ScrollBar.thumbArc", 999);
            UIManager.put("ScrollBar.width", 10);

            UIManager.put("TabbedPane.tabHeight", 32);

        } catch (Exception e) {

            System.err.println(
                "Gagal set FlatLaf: " + e.getMessage()
            );
        }

        // =========================
        // CEK DATABASE
        // =========================
        if (!DatabaseConfig.testConnection()) {

            JOptionPane.showMessageDialog(
                null,

                "Tidak dapat terhubung ke database!\n\n" +
                "Pastikan:\n" +
                "1. MySQL / XAMPP sudah berjalan\n" +
                "2. Database 'payroll_pro_db' sudah dibuat\n" +
                "3. Username dan password di DatabaseConfig.java benar\n\n" +
                "Jalankan schema.sql terlebih dahulu.",

                "Koneksi Database Gagal",
                JOptionPane.ERROR_MESSAGE
            );

            System.exit(1);
        }

        // =========================
        // JALANKAN APLIKASI
        // =========================
        SwingUtilities.invokeLater(() -> {

        SplashScreen splash = new SplashScreen();
        splash.setVisible(true);

        splash.startLoading(() -> {
            new LoginFrame().setVisible(true);
        });

    });
    }
}