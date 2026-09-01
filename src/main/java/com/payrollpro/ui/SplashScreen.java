package com.payrollpro.ui;

import javax.swing.*;
import java.awt.*;

public class SplashScreen extends JFrame {

    private JProgressBar progressBar;
    private JLabel lblStatus;

    public SplashScreen() {

        setTitle("PayrollPro");
        setSize(500, 280);
        setLocationRelativeTo(null);
        setUndecorated(true);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(22,22,42));

        JLabel lblTitle = new JLabel("PayrollPro");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBounds(180, 60, 250, 40);
        panel.add(lblTitle);

        JLabel lblSub = new JLabel("Prototype Sistem Penggajian Modern");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSub.setForeground(new Color(180,180,200));
        lblSub.setBounds(130, 100, 300, 25);
        panel.add(lblSub);

        JLabel lblDev = new JLabel("Developed by Syauqi XI RPL 1");
        lblDev.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblDev.setForeground(new Color(120,120,140));
        lblDev.setBounds(170, 130, 250, 20);
        panel.add(lblDev);

        progressBar = new JProgressBar();
        progressBar.setBounds(50, 210, 400, 20);
        progressBar.setStringPainted(true);
        panel.add(progressBar);

        lblStatus = new JLabel("Loading modules...");
        lblStatus.setForeground(Color.WHITE);
        lblStatus.setBounds(50, 185, 300, 20);
        panel.add(lblStatus);

        add(panel);
    }

    public void startLoading(Runnable onFinish) {

        new Thread(() -> {

            try {

                for (int i = 0; i <= 100; i++) {

                    Thread.sleep(25);

                    final int progress = i;

                    SwingUtilities.invokeLater(() -> {
                        progressBar.setValue(progress);

                        if (progress < 30) {
                            lblStatus.setText("Initializing system...");
                        } else if (progress < 60) {
                            lblStatus.setText("Loading payroll modules...");
                        } else if (progress < 90) {
                            lblStatus.setText("Preparing dashboard...");
                        } else {
                            lblStatus.setText("Launching application...");
                        }
                    });
                }


                SwingUtilities.invokeLater(() -> {
                    dispose();
                    if (onFinish != null) onFinish.run();
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

        }).start();
    }
}