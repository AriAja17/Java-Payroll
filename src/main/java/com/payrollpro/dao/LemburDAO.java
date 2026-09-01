package com.payrollpro.dao;

import com.payrollpro.config.DatabaseConfig;
import com.payrollpro.model.Lembur;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LemburDAO {

    public List<Lembur> getByKaryawanBulan(String idKaryawan, int bulan, int tahun) throws SQLException {
        List<Lembur> list = new ArrayList<>();
        String sql = "SELECT l.*, k.nama FROM lembur l " +
                     "JOIN karyawan k ON l.id_karyawan=k.id_karyawan " +
                     "WHERE l.id_karyawan=? AND MONTH(l.tanggal_lembur)=? AND YEAR(l.tanggal_lembur)=?";
        try (PreparedStatement ps = DatabaseConfig.getConnection().prepareStatement(sql)) {
            ps.setString(1, idKaryawan);
            ps.setInt(2, bulan);
            ps.setInt(3, tahun);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    public List<Lembur> getAllByBulan(int bulan, int tahun) throws SQLException {
        List<Lembur> list = new ArrayList<>();
        String sql = "SELECT l.*, k.nama FROM lembur l " +
                     "JOIN karyawan k ON l.id_karyawan=k.id_karyawan " +
                     "WHERE MONTH(l.tanggal_lembur)=? AND YEAR(l.tanggal_lembur)=? " +
                     "ORDER BY l.tanggal_lembur";
        try (PreparedStatement ps = DatabaseConfig.getConnection().prepareStatement(sql)) {
            ps.setInt(1, bulan);
            ps.setInt(2, tahun);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    public double getTotalUpahByKaryawanBulan(String idKaryawan, int bulan, int tahun) throws SQLException {
        String sql = "SELECT COALESCE(SUM(total_upah),0) FROM lembur " +
                     "WHERE id_karyawan=? AND MONTH(tanggal_lembur)=? AND YEAR(tanggal_lembur)=?";
        try (PreparedStatement ps = DatabaseConfig.getConnection().prepareStatement(sql)) {
            ps.setString(1, idKaryawan);
            ps.setInt(2, bulan);
            ps.setInt(3, tahun);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getDouble(1);
            }
        }
        return 0;
    }

    public void insert(Lembur l) throws SQLException {
        String sql = "INSERT INTO lembur (id_karyawan,tanggal_lembur,jam_lembur," +
                     "is_hari_libur,tarif_per_jam,total_upah,keterangan) VALUES (?,?,?,?,?,?,?)";
        try (PreparedStatement ps = DatabaseConfig.getConnection().prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, l.getIdKaryawan());
            ps.setDate(2, Date.valueOf(l.getTanggalLembur()));
            ps.setInt(3, l.getJamLembur());
            ps.setBoolean(4, l.isHariLibur());
            ps.setDouble(5, l.getTarifPerJam());
            ps.setDouble(6, l.getTotalUpah());
            ps.setString(7, l.getKeterangan());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) l.setIdLembur(keys.getInt(1));
            }
        }
    }

    public void update(Lembur l) throws SQLException {
        String sql = "UPDATE lembur SET id_karyawan=?,tanggal_lembur=?,jam_lembur=?," +
                     "is_hari_libur=?,tarif_per_jam=?,total_upah=?,keterangan=? WHERE id_lembur=?";
        try (PreparedStatement ps = DatabaseConfig.getConnection().prepareStatement(sql)) {
            ps.setString(1, l.getIdKaryawan());
            ps.setDate(2, Date.valueOf(l.getTanggalLembur()));
            ps.setInt(3, l.getJamLembur());
            ps.setBoolean(4, l.isHariLibur());
            ps.setDouble(5, l.getTarifPerJam());
            ps.setDouble(6, l.getTotalUpah());
            ps.setString(7, l.getKeterangan());
            ps.setInt(8, l.getIdLembur());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM lembur WHERE id_lembur=?";
        try (PreparedStatement ps = DatabaseConfig.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private Lembur map(ResultSet rs) throws SQLException {
        Lembur l = new Lembur();
        l.setIdLembur(rs.getInt("id_lembur"));
        l.setIdKaryawan(rs.getString("id_karyawan"));
        l.setNamaKaryawan(rs.getString("nama"));
        l.setTanggalLembur(rs.getDate("tanggal_lembur").toLocalDate());
        l.setJamLembur(rs.getInt("jam_lembur"));
        l.setHariLibur(rs.getBoolean("is_hari_libur"));
        l.setTarifPerJam(rs.getDouble("tarif_per_jam"));
        l.setTotalUpah(rs.getDouble("total_upah"));
        l.setKeterangan(rs.getString("keterangan"));
        return l;
    }
}
