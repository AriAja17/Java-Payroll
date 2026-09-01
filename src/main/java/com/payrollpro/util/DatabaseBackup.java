package com.payrollpro.util;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Backup database menggunakan mysqldump yang dipanggil dari Java Runtime.
 *
 * SYARAT: mysqldump harus ada di PATH sistem.
 * XAMPP Windows: tambahkan C:\xampp\mysql\bin ke System PATH.
 * Linux/Mac: biasanya sudah otomatis ada.
 */
public class DatabaseBackup {

    private static final String DB_NAME = "payroll_pro_db";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

    public static File backup(String outputDir) throws IOException, InterruptedException {
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename  = "backup_payroll_" + timestamp + ".sql";
        File   outputFile = new File(outputDir, filename);

        new File(outputDir).mkdirs();

        String[] command;
        if (DB_PASS == null || DB_PASS.isEmpty()) {
            command = new String[]{
                "mysqldump", "-u", DB_USER,
                "--databases", DB_NAME,
                "--result-file=" + outputFile.getAbsolutePath()
            };
        } else {
            command = new String[]{
                "mysqldump", "-u", DB_USER, "-p" + DB_PASS,
                "--databases", DB_NAME,
                "--result-file=" + outputFile.getAbsolutePath()
            };
        }

        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true);
        Process process = pb.start();

        // Baca output untuk logging
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("[BACKUP] " + line);
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new IOException("mysqldump gagal dengan exit code: " + exitCode +
                    ". Pastikan mysqldump ada di PATH sistem.");
        }

        System.out.println("[BACKUP] Berhasil: " + outputFile.getAbsolutePath());
        return outputFile;
    }
}
