package com.payrollpro.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DatabaseConfig - Singleton pattern untuk koneksi database.
 * Ganti DB_HOST, DB_USER, DB_PASS sesuai environment kamu.
 */
public class DatabaseConfig {

    private static final String DB_HOST = "localhost";
    private static final String DB_PORT = "3306";
    private static final String DB_NAME = "payroll_pro_db";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

    private static final String URL =
        "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME
        + "?useSSL=false&serverTimezone=Asia/Jakarta&allowPublicKeyRetrieval=true&characterEncoding=utf8";

    private static Connection connection = null;

    private DatabaseConfig() {}

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, DB_USER, DB_PASS);
                System.out.println("[DB] Koneksi berhasil ke " + DB_NAME);
            } catch (ClassNotFoundException e) {
                throw new SQLException("MySQL Driver tidak ditemukan: " + e.getMessage());
            }
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("[DB] Koneksi ditutup.");
            } catch (SQLException e) {
                System.err.println("[DB] Gagal menutup koneksi: " + e.getMessage());
            }
        }
    }

    public static boolean testConnection() {
        try {
            Connection c = getConnection();
            return c != null && !c.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}
