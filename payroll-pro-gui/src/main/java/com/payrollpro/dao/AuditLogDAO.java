package com.payrollpro.dao;

import com.payrollpro.config.DatabaseConfig;
import com.payrollpro.model.AuditLog;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuditLogDAO {

    public void insert(AuditLog log) throws SQLException {
        String sql = "INSERT INTO audit_log (username,aksi,tabel,id_record," +
                     "data_sebelum,data_sesudah,keterangan) VALUES (?,?,?,?,?,?,?)";
        try (PreparedStatement ps = DatabaseConfig.getConnection().prepareStatement(sql)) {
            ps.setString(1, log.getUsername());
            ps.setString(2, log.getAksi());
            ps.setString(3, log.getTabel());
            ps.setString(4, log.getIdRecord());
            ps.setString(5, log.getDataSebelum());
            ps.setString(6, log.getDataSesudah());
            ps.setString(7, log.getKeterangan());
            ps.executeUpdate();
        }
    }

    public List<AuditLog> getRecent(int limit) throws SQLException {
        List<AuditLog> list = new ArrayList<>();
        String sql = "SELECT * FROM audit_log ORDER BY created_at DESC LIMIT ?";
        try (PreparedStatement ps = DatabaseConfig.getConnection().prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    public List<AuditLog> getAll() throws SQLException {
        List<AuditLog> list = new ArrayList<>();
        String sql = "SELECT * FROM audit_log ORDER BY created_at DESC";
        try (Statement st = DatabaseConfig.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    private AuditLog map(ResultSet rs) throws SQLException {
        AuditLog a = new AuditLog();
        a.setId(rs.getInt("id"));
        a.setUsername(rs.getString("username"));
        a.setAksi(rs.getString("aksi"));
        a.setTabel(rs.getString("tabel"));
        a.setIdRecord(rs.getString("id_record"));
        a.setDataSebelum(rs.getString("data_sebelum"));
        a.setDataSesudah(rs.getString("data_sesudah"));
        a.setKeterangan(rs.getString("keterangan"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) a.setCreatedAt(ts.toLocalDateTime());
        return a;
    }
}
