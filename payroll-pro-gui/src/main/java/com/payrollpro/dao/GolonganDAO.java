package com.payrollpro.dao;

import com.payrollpro.config.DatabaseConfig;
import com.payrollpro.model.Golongan;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GolonganDAO {

    public List<Golongan> getAll() throws SQLException {
        List<Golongan> list = new ArrayList<>();
        String sql = "SELECT * FROM golongan ORDER BY id_golongan";
        try (Statement st = DatabaseConfig.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public Golongan getById(String id) throws SQLException {
        String sql = "SELECT * FROM golongan WHERE id_golongan = ?";
        try (PreparedStatement ps = DatabaseConfig.getConnection().prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        }
        return null;
    }

    public void insert(Golongan g) throws SQLException {
        String sql = "INSERT INTO golongan (id_golongan,nama_golongan,gaji_pokok," +
                     "tunjangan_istri,tunjangan_anak,transport,uang_makan) VALUES (?,?,?,?,?,?,?)";
        try (PreparedStatement ps = DatabaseConfig.getConnection().prepareStatement(sql)) {
            ps.setString(1, g.getIdGolongan());
            ps.setString(2, g.getNamaGolongan());
            ps.setDouble(3, g.getGajiPokok());
            ps.setDouble(4, g.getTunjanganIstri());
            ps.setDouble(5, g.getTunjanganAnak());
            ps.setDouble(6, g.getTransport());
            ps.setDouble(7, g.getUangMakan());
            ps.executeUpdate();
        }
    }

    public void update(Golongan g) throws SQLException {
        String sql = "UPDATE golongan SET nama_golongan=?,gaji_pokok=?," +
                     "tunjangan_istri=?,tunjangan_anak=?,transport=?,uang_makan=? " +
                     "WHERE id_golongan=?";
        try (PreparedStatement ps = DatabaseConfig.getConnection().prepareStatement(sql)) {
            ps.setString(1, g.getNamaGolongan());
            ps.setDouble(2, g.getGajiPokok());
            ps.setDouble(3, g.getTunjanganIstri());
            ps.setDouble(4, g.getTunjanganAnak());
            ps.setDouble(5, g.getTransport());
            ps.setDouble(6, g.getUangMakan());
            ps.setString(7, g.getIdGolongan());
            ps.executeUpdate();
        }
    }

    /** Cek apakah golongan masih dipakai karyawan sebelum hapus. */
    public boolean isDipakai(String idGolongan) throws SQLException {
        String sql = "SELECT COUNT(*) FROM karyawan WHERE id_golongan = ?";
        try (PreparedStatement ps = DatabaseConfig.getConnection().prepareStatement(sql)) {
            ps.setString(1, idGolongan);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM golongan WHERE id_golongan = ?";
        try (PreparedStatement ps = DatabaseConfig.getConnection().prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        }
    }

    public String generateId() throws SQLException {
        String sql = "SELECT id_golongan FROM golongan ORDER BY id_golongan DESC LIMIT 1";
        try (Statement st = DatabaseConfig.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                String last = rs.getString(1).replace("G-", "");
                int next = Integer.parseInt(last) + 1;
                return String.format("G-%02d", next);
            }
        }
        return "G-01";
    }

    private Golongan map(ResultSet rs) throws SQLException {
        return new Golongan(
            rs.getString("id_golongan"),
            rs.getString("nama_golongan"),
            rs.getDouble("gaji_pokok"),
            rs.getDouble("tunjangan_istri"),
            rs.getDouble("tunjangan_anak"),
            rs.getDouble("transport"),
            rs.getDouble("uang_makan")
        );
    }
}
