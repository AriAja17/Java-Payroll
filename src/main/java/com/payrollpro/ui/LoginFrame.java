package com.payrollpro.ui;

import com.payrollpro.dao.UserDAO;
import com.payrollpro.model.User;
import com.payrollpro.service.AuditService;
import com.payrollpro.util.RoleManager;

/**
 * Form login PayrollPro.
 * Username default : admin      | Password: admin123
 * Username default : operator1  | Password: operator123
 */

public class LoginFrame extends javax.swing.JFrame {

    public LoginFrame() {
        initComponents();
        setLocationRelativeTo(null);
        setupPlaceholder();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        lblTitle = new javax.swing.JLabel();
        lblSubtitle = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        txtPassword = new javax.swing.JPasswordField();
        btnLogin = new javax.swing.JButton();
        lblStatus = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("PayrollPro - Login");
        setResizable(false);

        lblTitle.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 22));
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("PayrollPro");

        lblSubtitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSubtitle.setText("Sistem Penggajian Terintegrasi");

        btnLogin.setText("Masuk");
        btnLogin.addActionListener(evt -> doLogin());

        txtPassword.addActionListener(evt -> doLogin());

        lblStatus.setForeground(new java.awt.Color(200, 50, 50));
        lblStatus.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblStatus.setText(" ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                .addGroup(layout.createSequentialGroup()
                    .addGap(60)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)

                        .addComponent(lblTitle)
                        .addComponent(lblSubtitle)

                        .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)

                        .addComponent(btnLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)

                        .addComponent(lblStatus)

                    )
                    .addGap(60))
        );  
        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addGap(40)
                .addComponent(lblTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblSubtitle)

                .addGap(25)

                .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)

                .addGap(12)

                .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)

                .addGap(18)

                .addComponent(btnLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)

                .addGap(10)

                .addComponent(lblStatus)

                .addGap(40)
        );

        pack();
    }

    // ===================== PLACEHOLDER =====================

    private void setupPlaceholder() {

        // Username placeholder
        txtUsername.setText("Username");
        txtUsername.setForeground(java.awt.Color.GRAY);

        txtUsername.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (txtUsername.getText().equals("Username")) {
                    txtUsername.setText("");
                    txtUsername.setForeground(java.awt.Color.WHITE);
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (txtUsername.getText().isEmpty()) {
                    txtUsername.setText("Username");
                    txtUsername.setForeground(java.awt.Color.GRAY);
                }
            }
        });

        // Password placeholder
        txtPassword.setText("Password");
        txtPassword.setForeground(java.awt.Color.GRAY);
        txtPassword.setEchoChar((char) 0);

        txtPassword.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (new String(txtPassword.getPassword()).equals("Password")) {
                    txtPassword.setText("");
                    txtPassword.setEchoChar('•');
                    txtPassword.setForeground(java.awt.Color.WHITE);
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (txtPassword.getPassword().length == 0) {
                    txtPassword.setText("Password");
                    txtPassword.setEchoChar((char) 0);
                    txtPassword.setForeground(java.awt.Color.GRAY);
                }
            }
        });
    }

    // ===================== LOGIN LOGIC =====================

    private void doLogin() {

        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        // placeholder fix
        if (username.equals("Username")) username = "";
        if (password.equals("Password")) password = "";

        // final copy untuk inner class
        final String finalUsername = username;
        final String finalPassword = password;

        btnLogin.setEnabled(false);
        lblStatus.setText("Memeriksa...");

        javax.swing.SwingWorker<User, Void> worker = new javax.swing.SwingWorker<>() {
            @Override
            protected User doInBackground() throws Exception {
                return new UserDAO().login(finalUsername, finalPassword);
            }

            @Override
            protected void done() {
                try {
                    User user = get();

                    if (user != null) {
                        RoleManager.getInstance().setCurrentUser(user);
                        AuditService.logLogin(user.getUsername());

                        dispose();

                        javax.swing.SwingUtilities.invokeLater(() ->
                            new MainFrame().setVisible(true)
                        );

                    } else {
                        lblStatus.setText("Username atau password salah.");
                        txtPassword.setText("");
                        btnLogin.setEnabled(true);
                    }

                } catch (Exception ex) {
                    lblStatus.setText("Error: " + ex.getMessage());
                    btnLogin.setEnabled(true);
                }
            }
        };
        worker.execute();
    }

    // ===================== VARIABLES =====================

    private javax.swing.JButton btnLogin;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblSubtitle;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUsername;
}