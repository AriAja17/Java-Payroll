package com.payrollpro.service;

import com.payrollpro.dao.AuditLogDAO;
import com.payrollpro.model.AuditLog;
import com.payrollpro.util.RoleManager;

public class AuditService {

    private static final AuditLogDAO dao = new AuditLogDAO();

    public static void log(String aksi, String tabel, String idRecord, String keterangan) {
        try {
            String username = RoleManager.getInstance().getUsername();
            AuditLog entry = new AuditLog(username, aksi, tabel, idRecord, keterangan);
            dao.insert(entry);
        } catch (Exception e) {
            System.err.println("[AUDIT] Gagal catat log: " + e.getMessage());
        }
    }

    public static void logInsert(String tabel, String idRecord) {
        log("INSERT", tabel, idRecord, "Tambah: " + idRecord);
    }

    public static void logUpdate(String tabel, String idRecord) {
        log("UPDATE", tabel, idRecord, "Update: " + idRecord);
    }

    public static void logDelete(String tabel, String idRecord) {
        log("DELETE", tabel, idRecord, "Hapus: " + idRecord);
    }

    public static void logLock(String periode) {
        log("LOCK", "penggajian", periode, "Kunci periode: " + periode);
    }

    public static void logExport(String jenis) {
        log("EXPORT", "-", "-", "Export: " + jenis);
    }

    public static void logBackup() {
        log("BACKUP", "-", "-", "Backup database");
    }

    public static void logLogin(String username) {
        try {
            dao.insert(new AuditLog(username, "LOGIN", "-", "-", "Login berhasil"));
        } catch (Exception ignored) {}
    }
}
