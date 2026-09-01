package com.payrollpro.dao;

import com.payrollpro.config.DatabaseConfig;
import com.payrollpro.model.THR;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class THRDAO {

    public List<THR> getByTahunJenis(int tahun, String jenisHariRaya) throws SQLException {
        List<THR> list = new ArrayList<>();
        String sql = "SELECT t.*, k.nama FROM thr t " +
                     "JOIN karyawan k ON t.id_karyawan=k.id_karyawan " +
                     "WHERE t.tahun=? AND t.jenis_hari_raya=? ORDER BY k.nama";
        try (PreparedStatement ps = DatabaseConfig.getConnection().prepareStatement(sql)) {
            ps.setInt(1, tahun);
            ps.setString(2, jenisHariRaya);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    public void insertOrUpdate(THR t) throws SQLException {
        String sql = "INSERT INTO thr (id_karyawan,tahun,jenis_hari_raya,masa_kerja_bln," +
                     "gaji_pokok,faktor,nominal_thr,status_bayar) VALUES (?,?,?,?,?,?,?,'Belum') " +
                     "ON DUPLICATE KEY UPDATE masa_kerja_bln=?,gaji_pokok=?,faktor=?,nominal_thr=?";
        try (PreparedStatement ps = DatabaseConfig.getConnection().prepareStatement(sql)) {
            ps.setString(1, t.getIdKaryawan());
            ps.setInt(2, t.getTahun());
            ps.setString(3, t.getJenisHariRaya());
            ps.setInt(4, t.getMasaKerjaBulan());
            ps.setDouble(5, t.getGajiPokok());
            ps.setDouble(6, t.getFaktor());
            ps.setDouble(7, t.getNominalThr());
            ps.setInt(8, t.getMasaKerjaBulan());
            ps.setDouble(9, t.getGajiPokok());
            ps.setDouble(10, t.getFaktor());
            ps.setDouble(11, t.getNominalThr());
            ps.executeUpdate();
        }
    }

    public void tandaiLunas(int idThr) throws SQLException {
        String sql = "UPDATE thr SET status_bayar='Lunas', tanggal_bayar=CURDATE() WHERE id_thr=?";
        try (PreparedStatement ps = DatabaseConfig.getConnection().prepareStatement(sql)) {
            ps.setInt(1, idThr);
            ps.executeUpdate();
        }
    }

    private THR map(ResultSet rs) throws SQLException {
        THR t = new THR();
        t.setIdThr(rs.getInt("id_thr"));
        t.setIdKaryawan(rs.getString("id_karyawan"));
        t.setNamaKaryawan(rs.getString("nama"));
        t.setTahun(rs.getInt("tahun"));
        t.setJenisHariRaya(rs.getString("jenis_hari_raya"));
        t.setMasaKerjaBulan(rs.getInt("masa_kerja_bln"));
        t.setGajiPokok(rs.getDouble("gaji_pokok"));
        t.setFaktor(rs.getDouble("faktor"));
        t.setNominalThr(rs.getDouble("nominal_thr"));
        t.setStatusBayar(rs.getString("status_bayar"));
        Date tb = rs.getDate("tanggal_bayar");
        if (tb != null) t.setTanggalBayar(tb.toLocalDate());
        return t;
    }
}
