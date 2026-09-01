package com.payrollpro.dao;

import com.payrollpro.config.DatabaseConfig;
import com.payrollpro.model.UMP;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UMPDAO {

    public List<UMP> getByTahun(int tahun) throws SQLException {
        List<UMP> list = new ArrayList<>();
        String sql = "SELECT * FROM ump_master WHERE tahun=? ORDER BY wilayah";
        try (PreparedStatement ps = DatabaseConfig.getConnection().prepareStatement(sql)) {
            ps.setInt(1, tahun);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    public double getUMP(String wilayah, int tahun) throws SQLException {
        String sql = "SELECT nilai_ump FROM ump_master WHERE wilayah=? AND tahun=?";
        try (PreparedStatement ps = DatabaseConfig.getConnection().prepareStatement(sql)) {
            ps.setString(1, wilayah);
            ps.setInt(2, tahun);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getDouble(1);
            }
        }
        return 0;
    }

    public void upsert(UMP u) throws SQLException {
        String sql = "INSERT INTO ump_master (wilayah,tahun,nilai_ump) VALUES (?,?,?) " +
                     "ON DUPLICATE KEY UPDATE nilai_ump=?";
        try (PreparedStatement ps = DatabaseConfig.getConnection().prepareStatement(sql)) {
            ps.setString(1, u.getWilayah());
            ps.setInt(2, u.getTahun());
            ps.setDouble(3, u.getNilaiUmp());
            ps.setDouble(4, u.getNilaiUmp());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM ump_master WHERE id=?";
        try (PreparedStatement ps = DatabaseConfig.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private UMP map(ResultSet rs) throws SQLException {
        UMP u = new UMP();
        u.setId(rs.getInt("id"));
        u.setWilayah(rs.getString("wilayah"));
        u.setTahun(rs.getInt("tahun"));
        u.setNilaiUmp(rs.getDouble("nilai_ump"));
        return u;
    }
}
