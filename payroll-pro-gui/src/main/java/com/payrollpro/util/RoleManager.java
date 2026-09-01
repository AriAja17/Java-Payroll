package com.payrollpro.util;

import com.payrollpro.model.User;

/**
 * Singleton yang menyimpan user yang sedang login.
 * Dipakai di seluruh aplikasi untuk cek role dan username.
 */
public class RoleManager {

    private static RoleManager instance;
    private User currentUser;

    private RoleManager() {}

    public static RoleManager getInstance() {
        if (instance == null) instance = new RoleManager();
        return instance;
    }

    public void setCurrentUser(User user) { this.currentUser = user; }
    public User getCurrentUser()          { return currentUser; }

    public boolean isAdmin() {
        return currentUser != null && currentUser.isAdmin();
    }

    public String getUsername() {
        return currentUser != null ? currentUser.getUsername() : "unknown";
    }

    /** Aktifkan atau nonaktifkan komponen berdasarkan role. */
    public void applyRole(javax.swing.JComponent... adminOnlyComponents) {
        boolean admin = isAdmin();
        for (javax.swing.JComponent c : adminOnlyComponents) {
            c.setEnabled(admin);
            c.setOpaque(!admin);
        }
    }

    public void logout() { currentUser = null; }
}
