package com.payrollpro.dao;

import com.payrollpro.config.DatabaseConfig;
import com.payrollpro.model.JenisCuti;
import com.payrollpro.model.PengajuanCuti;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CutiDAO {

    public List<JenisCuti> getAllJenis() throws SQLException {
        List<JenisCuti> list = new ArrayList<>();
        String sql = "SELECT * FROM jenis_cuti ORDER BY id_jenis";
        try (Statement st = DatabaseConfig.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                JenisCuti j = new JenisCuti();
                j.setIdJenis(rs.getInt("id_jenis"));
                j.setNamaCuti(rs.getString("nama_cuti"));
                j.setKuotaHari(rs.getInt("kuota_hari"));
                j.setDibayar(rs.getBoolean("is_dibayar"));
                j.setButuhDokumen(rs.getBoolean("butuh_dokumen"));
                j.setKeterangan(rs.getString("keterangan"));
                list.add(j);
            }
        }
        return list;
    }

    public List<PengajuanCuti> getAllPengajuan() throws SQLException {
        List<PengajuanCuti> list = new ArrayList<>();
        String sql = "SELECT c.*, k.nama, j.nama_cuti FROM pengajuan_cuti c " +
                     "JOIN karyawan k ON c.id_karyawan=k.id_karyawan " +
                     "JOIN jenis_cuti j ON c.id_jenis=j.id_jenis " +
                     "ORDER BY c.created_at DESC";
        try (Statement st = DatabaseConfig.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapPengajuan(rs));
        }
        return list;
    }

    public List<PengajuanCuti> getPending() throws SQLException {
        List<PengajuanCuti> list = new ArrayList<>();
        String sql = "SELECT c.*, k.nama, j.nama_cuti FROM pengajuan_cuti c " +
                     "JOIN karyawan k ON c.id_karyawan=k.id_karyawan " +
                     "JOIN jenis_cuti j ON c.id_jenis=j.id_jenis " +
                     "WHERE c.status='Pending' ORDER BY c.created_at";
        try (Statement st = DatabaseConfig.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapPengajuan(rs));
        }
        return list;
    }

    /** Hitung total hari alpha (tidak dibayar) karyawan di suatu periode untuk potongan gaji. */
    public int getTotalAlpha(String idKaryawan, int bulan, int tahun) throws SQLException {
        String sql = "SELECT COALESCE(SUM(c.jumlah_hari),0) FROM pengajuan_cuti c " +
                     "JOIN jenis_cuti j ON c.id_jenis=j.id_jenis " +
                     "WHERE c.id_karyawan=? AND c.status='Disetujui' " +
                     "AND j.is_dibayar=FALSE " +
                     "AND MONTH(c.tgl_mulai)=? AND YEAR(c.tgl_mulai)=?";
        try (PreparedStatement ps = DatabaseConfig.getConnection().prepareStatement(sql)) {
            ps.setString(1, idKaryawan);
            ps.setInt(2, bulan);
            ps.setInt(3, tahun);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 0;
    }

    public void insert(PengajuanCuti c) throws SQLException {
        String sql = "INSERT INTO pengajuan_cuti (id_karyawan,id_jenis,tgl_mulai," +
                     "tgl_selesai,jumlah_hari,alasan,status) VALUES (?,?,?,?,?,?,'Pending')";
        try (PreparedStatement ps = DatabaseConfig.getConnection().prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getIdKaryawan());
            ps.setInt(2, c.getIdJenis());
            ps.setDate(3, Date.valueOf(c.getTglMulai()));
            ps.setDate(4, Date.valueOf(c.getTglSelesai()));
            ps.setInt(5, c.getJumlahHari());
            ps.setString(6, c.getAlasan());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) c.setIdCuti(keys.getInt(1));
            }
        }
    }

    public void updateStatus(int idCuti, String status, String oleh, String catatan)
            throws SQLException {
        String sql = "UPDATE pengajuan_cuti SET status=?,diproses_oleh=?," +
                     "catatan_admin=?,tgl_proses=NOW() WHERE id_cuti=?";
        try (PreparedStatement ps = DatabaseConfig.getConnection().prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setString(2, oleh);
            ps.setString(3, catatan);
            ps.setInt(4, idCuti);
            ps.executeUpdate();
        }
    }

    public void delete(int idCuti) throws SQLException {
        String sql = "DELETE FROM pengajuan_cuti WHERE id_cuti=? AND status='Pending'";
        try (PreparedStatement ps = DatabaseConfig.getConnection().prepareStatement(sql)) {
            ps.setInt(1, idCuti);
            ps.executeUpdate();
        }
    }

    private PengajuanCuti mapPengajuan(ResultSet rs) throws SQLException {
        PengajuanCuti c = new PengajuanCuti();
        c.setIdCuti(rs.getInt("id_cuti"));
        c.setIdKaryawan(rs.getString("id_karyawan"));
        c.setNamaKaryawan(rs.getString("nama"));
        c.setIdJenis(rs.getInt("id_jenis"));
        c.setNamaJenis(rs.getString("nama_cuti"));
        c.setTglMulai(rs.getDate("tgl_mulai").toLocalDate());
        c.setTglSelesai(rs.getDate("tgl_selesai").toLocalDate());
        c.setJumlahHari(rs.getInt("jumlah_hari"));
        c.setAlasan(rs.getString("alasan"));
        c.setStatus(rs.getString("status"));
        c.setDiprosesOleh(rs.getString("diproses_oleh"));
        c.setCatatanAdmin(rs.getString("catatan_admin"));
        return c;
    }
}
