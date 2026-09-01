package com.payrollpro.dao;

import com.payrollpro.config.DatabaseConfig;
import com.payrollpro.model.Karyawan;
import com.payrollpro.model.Golongan;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KaryawanDAO {

    public List<Karyawan> getAll() throws SQLException {
        List<Karyawan> list = new ArrayList<>();
        String sql = "SELECT k.*, g.nama_golongan, g.gaji_pokok, g.tunjangan_istri, " +
                     "g.tunjangan_anak, g.transport, g.uang_makan " +
                     "FROM karyawan k JOIN golongan g ON k.id_golongan=g.id_golongan " +
                     "ORDER BY k.id_karyawan";
        try (Statement st = DatabaseConfig.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public List<Karyawan> getAllAktif() throws SQLException {
        List<Karyawan> list = new ArrayList<>();
        String sql = "SELECT k.*, g.nama_golongan, g.gaji_pokok, g.tunjangan_istri, " +
                     "g.tunjangan_anak, g.transport, g.uang_makan " +
                     "FROM karyawan k JOIN golongan g ON k.id_golongan=g.id_golongan " +
                     "WHERE k.status_karyawan IN ('Aktif','Probation') ORDER BY k.nama";
        try (Statement st = DatabaseConfig.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public Karyawan getById(String id) throws SQLException {
        String sql = "SELECT k.*, g.nama_golongan, g.gaji_pokok, g.tunjangan_istri, " +
                     "g.tunjangan_anak, g.transport, g.uang_makan " +
                     "FROM karyawan k JOIN golongan g ON k.id_golongan=g.id_golongan " +
                     "WHERE k.id_karyawan = ?";
        try (PreparedStatement ps = DatabaseConfig.getConnection().prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        }
        return null;
    }

    public void insert(Karyawan k) throws SQLException {
        String sql = "INSERT INTO karyawan (id_karyawan,id_golongan,nama,jenis_kelamin," +
                     "tempat_lahir,tanggal_lahir,alamat,status_nikah,jumlah_anak,npwp," +
                     "tanggal_masuk,status_karyawan) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = DatabaseConfig.getConnection().prepareStatement(sql)) {
            ps.setString(1, k.getIdKaryawan());
            ps.setString(2, k.getIdGolongan());
            ps.setString(3, k.getNama());
            ps.setString(4, k.getJenisKelamin());
            ps.setString(5, k.getTempatLahir());
            ps.setDate(6, k.getTanggalLahir() != null ? Date.valueOf(k.getTanggalLahir()) : null);
            ps.setString(7, k.getAlamat());
            ps.setString(8, k.getStatusNikah());
            ps.setInt(9, k.getJumlahAnak());
            ps.setString(10, k.getNpwp());
            ps.setDate(11, Date.valueOf(k.getTanggalMasuk()));
            ps.setString(12, k.getStatusKaryawan());
            ps.executeUpdate();
        }
    }

    public void update(Karyawan k) throws SQLException {
        String sql = "UPDATE karyawan SET id_golongan=?,nama=?,jenis_kelamin=?," +
                     "tempat_lahir=?,tanggal_lahir=?,alamat=?,status_nikah=?," +
                     "jumlah_anak=?,npwp=?,tanggal_masuk=?,status_karyawan=?," +
                     "tanggal_resign=? WHERE id_karyawan=?";
        try (PreparedStatement ps = DatabaseConfig.getConnection().prepareStatement(sql)) {
            ps.setString(1, k.getIdGolongan());
            ps.setString(2, k.getNama());
            ps.setString(3, k.getJenisKelamin());
            ps.setString(4, k.getTempatLahir());
            ps.setDate(5, k.getTanggalLahir() != null ? Date.valueOf(k.getTanggalLahir()) : null);
            ps.setString(6, k.getAlamat());
            ps.setString(7, k.getStatusNikah());
            ps.setInt(8, k.getJumlahAnak());
            ps.setString(9, k.getNpwp());
            ps.setDate(10, Date.valueOf(k.getTanggalMasuk()));
            ps.setString(11, k.getStatusKaryawan());
            ps.setDate(12, k.getTanggalResign() != null ? Date.valueOf(k.getTanggalResign()) : null);
            ps.setString(13, k.getIdKaryawan());
            ps.executeUpdate();
        }
    }

    /** Hapus hanya jika tidak ada riwayat gaji atau lembur. */
    public boolean delete(String id) throws SQLException {
        if (punyaRiwayatGaji(id) || punyaRiwayatLembur(id)) return false;
        String sql = "DELETE FROM karyawan WHERE id_karyawan = ?";
        try (PreparedStatement ps = DatabaseConfig.getConnection().prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        }
        return true;
    }

    public boolean punyaRiwayatGaji(String idKaryawan) throws SQLException {
        String sql = "SELECT COUNT(*) FROM penggajian WHERE id_karyawan=?";
        try (PreparedStatement ps = DatabaseConfig.getConnection().prepareStatement(sql)) {
            ps.setString(1, idKaryawan);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    public boolean punyaRiwayatLembur(String idKaryawan) throws SQLException {
        String sql = "SELECT COUNT(*) FROM lembur WHERE id_karyawan=?";
        try (PreparedStatement ps = DatabaseConfig.getConnection().prepareStatement(sql)) {
            ps.setString(1, idKaryawan);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    public String generateId() throws SQLException {
        String sql = "SELECT id_karyawan FROM karyawan ORDER BY id_karyawan DESC LIMIT 1";
        try (Statement st = DatabaseConfig.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                String last = rs.getString(1).replace("KRY-", "");
                int next = Integer.parseInt(last) + 1;
                return String.format("KRY-%03d", next);
            }
        }
        return "KRY-001";
    }

    private Karyawan map(ResultSet rs) throws SQLException {
        Karyawan k = new Karyawan();
        k.setIdKaryawan(rs.getString("id_karyawan"));
        k.setIdGolongan(rs.getString("id_golongan"));
        k.setNama(rs.getString("nama"));
        k.setJenisKelamin(rs.getString("jenis_kelamin"));
        k.setTempatLahir(rs.getString("tempat_lahir"));
        k.setAlamat(rs.getString("alamat"));
        k.setStatusNikah(rs.getString("status_nikah"));
        k.setJumlahAnak(rs.getInt("jumlah_anak"));
        k.setNpwp(rs.getString("npwp"));
        k.setStatusKaryawan(rs.getString("status_karyawan"));
        Date tl = rs.getDate("tanggal_lahir");
        if (tl != null) k.setTanggalLahir(tl.toLocalDate());
        Date tm = rs.getDate("tanggal_masuk");
        if (tm != null) k.setTanggalMasuk(tm.toLocalDate());
        Date tr = rs.getDate("tanggal_resign");
        if (tr != null) k.setTanggalResign(tr.toLocalDate());

        Golongan g = new Golongan(
            rs.getString("id_golongan"),
            rs.getString("nama_golongan"),
            rs.getDouble("gaji_pokok"),
            rs.getDouble("tunjangan_istri"),
            rs.getDouble("tunjangan_anak"),
            rs.getDouble("transport"),
            rs.getDouble("uang_makan")
        );
        k.setGolongan(g);
        return k;
    }
}
