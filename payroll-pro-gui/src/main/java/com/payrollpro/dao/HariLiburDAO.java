package com.payrollpro.dao;

import com.payrollpro.config.DatabaseConfig;
import com.payrollpro.model.HariLibur;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HariLiburDAO {

    public List<HariLibur> getAll() throws SQLException {
        List<HariLibur> list = new ArrayList<>();
        String sql = "SELECT * FROM hari_libur ORDER BY tanggal";
        try (Statement st = DatabaseConfig.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                HariLibur h = new HariLibur();
                h.setId(rs.getInt("id"));
                h.setTanggal(rs.getDate("tanggal").toLocalDate());
                h.setKeterangan(rs.getString("keterangan"));
                list.add(h);
            }
        }
        return list;
    }

    /** Ambil semua tanggal libur sebagai List<LocalDate> untuk kalkulasi. */
    public List<LocalDate> getAllTanggal() throws SQLException {
        List<LocalDate> list = new ArrayList<>();
        String sql = "SELECT tanggal FROM hari_libur";
        try (Statement st = DatabaseConfig.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(rs.getDate("tanggal").toLocalDate());
        }
        return list;
    }

    public boolean isHariLibur(LocalDate tanggal) throws SQLException {
        String sql = "SELECT COUNT(*) FROM hari_libur WHERE tanggal=?";
        try (PreparedStatement ps = DatabaseConfig.getConnection().prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(tanggal));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    public void insert(HariLibur h) throws SQLException {
        String sql = "INSERT INTO hari_libur (tanggal, keterangan) VALUES (?,?)";
        try (PreparedStatement ps = DatabaseConfig.getConnection().prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(h.getTanggal()));
            ps.setString(2, h.getKeterangan());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM hari_libur WHERE id=?";
        try (PreparedStatement ps = DatabaseConfig.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
