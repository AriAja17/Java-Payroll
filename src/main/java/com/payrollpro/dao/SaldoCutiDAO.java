package com.payrollpro.dao;

import com.payrollpro.config.DatabaseConfig;
import com.payrollpro.model.SaldoCuti;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SaldoCutiDAO {

    public SaldoCuti getByKaryawanJenisTahun(String idKaryawan, int idJenis, int tahun)
            throws SQLException {
        String sql = "SELECT s.*, k.nama, j.nama_cuti FROM saldo_cuti s " +
                     "JOIN karyawan k ON s.id_karyawan=k.id_karyawan " +
                     "JOIN jenis_cuti j ON s.id_jenis=j.id_jenis " +
                     "WHERE s.id_karyawan=? AND s.id_jenis=? AND s.tahun=?";
        try (PreparedStatement ps = DatabaseConfig.getConnection().prepareStatement(sql)) {
            ps.setString(1, idKaryawan);
            ps.setInt(2, idJenis);
            ps.setInt(3, tahun);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        }
        return null;
    }

    public List<SaldoCuti> getByTahun(int tahun) throws SQLException {
        List<SaldoCuti> list = new ArrayList<>();
        String sql = "SELECT s.*, k.nama, j.nama_cuti FROM saldo_cuti s " +
                     "JOIN karyawan k ON s.id_karyawan=k.id_karyawan " +
                     "JOIN jenis_cuti j ON s.id_jenis=j.id_jenis " +
                     "WHERE s.tahun=? AND s.id_jenis=1 ORDER BY k.nama";
        try (PreparedStatement ps = DatabaseConfig.getConnection().prepareStatement(sql)) {
            ps.setInt(1, tahun);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    public void upsert(String idKaryawan, int idJenis, int tahun, int kuota) throws SQLException {
        String sql = "INSERT INTO saldo_cuti (id_karyawan,id_jenis,tahun,kuota,terpakai) " +
                     "VALUES (?,?,?,?,0) ON DUPLICATE KEY UPDATE kuota=?";
        try (PreparedStatement ps = DatabaseConfig.getConnection().prepareStatement(sql)) {
            ps.setString(1, idKaryawan);
            ps.setInt(2, idJenis);
            ps.setInt(3, tahun);
            ps.setInt(4, kuota);
            ps.setInt(5, kuota);
            ps.executeUpdate();
        }
    }

    public void kurangiSaldo(String idKaryawan, int idJenis, int tahun, int jumlah)
            throws SQLException {
        String sql = "UPDATE saldo_cuti SET terpakai = terpakai + ? " +
                     "WHERE id_karyawan=? AND id_jenis=? AND tahun=?";
        try (PreparedStatement ps = DatabaseConfig.getConnection().prepareStatement(sql)) {
            ps.setInt(1, jumlah);
            ps.setString(2, idKaryawan);
            ps.setInt(3, idJenis);
            ps.setInt(4, tahun);
            ps.executeUpdate();
        }
    }

    public void tambahSaldo(String idKaryawan, int idJenis, int tahun, int jumlah)
            throws SQLException {
        String sql = "UPDATE saldo_cuti SET terpakai = GREATEST(0, terpakai - ?) " +
                     "WHERE id_karyawan=? AND id_jenis=? AND tahun=?";
        try (PreparedStatement ps = DatabaseConfig.getConnection().prepareStatement(sql)) {
            ps.setInt(1, jumlah);
            ps.setString(2, idKaryawan);
            ps.setInt(3, idJenis);
            ps.setInt(4, tahun);
            ps.executeUpdate();
        }
    }

    private SaldoCuti map(ResultSet rs) throws SQLException {
        SaldoCuti s = new SaldoCuti();
        s.setId(rs.getInt("id"));
        s.setIdKaryawan(rs.getString("id_karyawan"));
        s.setNamaKaryawan(rs.getString("nama"));
        s.setIdJenis(rs.getInt("id_jenis"));
        s.setNamaJenis(rs.getString("nama_cuti"));
        s.setTahun(rs.getInt("tahun"));
        s.setKuota(rs.getInt("kuota"));
        s.setTerpakai(rs.getInt("terpakai"));
        return s;
    }
}
