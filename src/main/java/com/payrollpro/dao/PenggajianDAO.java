package com.payrollpro.dao;

import com.payrollpro.config.DatabaseConfig;
import com.payrollpro.model.Penggajian;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PenggajianDAO {

    public Penggajian getByKaryawanPeriode(String idKaryawan, int bulan, int tahun)
            throws SQLException {
        String sql = "SELECT p.*, k.nama FROM penggajian p " +
                     "JOIN karyawan k ON p.id_karyawan=k.id_karyawan " +
                     "WHERE p.id_karyawan=? AND p.periode_bulan=? AND p.periode_tahun=?";
        try (PreparedStatement ps = DatabaseConfig.getConnection().prepareStatement(sql)) {
            ps.setString(1, idKaryawan);
            ps.setInt(2, bulan);
            ps.setInt(3, tahun);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        }
        return null;
    }

    public List<Penggajian> getAllByPeriode(int bulan, int tahun) throws SQLException {
        List<Penggajian> list = new ArrayList<>();
        String sql = "SELECT p.*, k.nama FROM penggajian p " +
                     "JOIN karyawan k ON p.id_karyawan=k.id_karyawan " +
                     "WHERE p.periode_bulan=? AND p.periode_tahun=? ORDER BY k.nama";
        try (PreparedStatement ps = DatabaseConfig.getConnection().prepareStatement(sql)) {
            ps.setInt(1, bulan);
            ps.setInt(2, tahun);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    public boolean isLocked(String idKaryawan, int bulan, int tahun) throws SQLException {
        String sql = "SELECT is_locked FROM penggajian WHERE id_karyawan=? " +
                     "AND periode_bulan=? AND periode_tahun=?";
        try (PreparedStatement ps = DatabaseConfig.getConnection().prepareStatement(sql)) {
            ps.setString(1, idKaryawan);
            ps.setInt(2, bulan);
            ps.setInt(3, tahun);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getBoolean("is_locked");
            }
        }
        return false;
    }

    public void lock(int idGaji) throws SQLException {
        String sql = "UPDATE penggajian SET is_locked=TRUE, status_bayar='Lunas' WHERE id_gaji=?";
        try (PreparedStatement ps = DatabaseConfig.getConnection().prepareStatement(sql)) {
            ps.setInt(1, idGaji);
            ps.executeUpdate();
        }
    }

    public void insert(Penggajian p) throws SQLException {
        String sql = "INSERT INTO penggajian (id_karyawan,periode_bulan,periode_tahun," +
                "gaji_pokok,tunjangan_istri,tunjangan_anak,transport,uang_makan," +
                "upah_lembur,potongan_cuti,pph21,bpjs_kes_karyawan,bpjs_jht_karyawan," +
                "bpjs_jp_karyawan,bpjs_kes_perusahaan,bpjs_jht_perusahaan,bpjs_jp_perusahaan," +
                "bpjs_jkk,bpjs_jkm,total_bruto,total_potongan,gaji_bersih," +
                "is_prorata,hari_kerja_aktual,hari_kerja_bulan,status_bayar,catatan) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = DatabaseConfig.getConnection().prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getIdKaryawan());
            ps.setInt(2, p.getPeriodeBulan());
            ps.setInt(3, p.getPeriodeTahun());
            ps.setDouble(4, p.getGajiPokok());
            ps.setDouble(5, p.getTunjanganIstri());
            ps.setDouble(6, p.getTunjanganAnak());
            ps.setDouble(7, p.getTransport());
            ps.setDouble(8, p.getUangMakan());
            ps.setDouble(9, p.getUpahLembur());
            ps.setDouble(10, p.getPotonganCuti());
            ps.setDouble(11, p.getPph21());
            ps.setDouble(12, p.getBpjsKesKaryawan());
            ps.setDouble(13, p.getBpjsJhtKaryawan());
            ps.setDouble(14, p.getBpjsJpKaryawan());
            ps.setDouble(15, p.getBpjsKesPerusahaan());
            ps.setDouble(16, p.getBpjsJhtPerusahaan());
            ps.setDouble(17, p.getBpjsJpPerusahaan());
            ps.setDouble(18, p.getBpjsJkk());
            ps.setDouble(19, p.getBpjsJkm());
            ps.setDouble(20, p.getTotalBruto());
            ps.setDouble(21, p.getTotalPotongan());
            ps.setDouble(22, p.getGajiBersih());
            ps.setBoolean(23, p.isProrata());
            ps.setObject(24, p.getHariKerjaAktual());
            ps.setObject(25, p.getHariKerjaBulan());
            ps.setString(26, p.getStatusBayar());
            ps.setString(27, p.getCatatan());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) p.setIdGaji(keys.getInt(1));
            }
        }
    }

    public void update(Penggajian p) throws SQLException {
        String sql = "UPDATE penggajian SET gaji_pokok=?,tunjangan_istri=?," +
                "tunjangan_anak=?,transport=?,uang_makan=?,upah_lembur=?," +
                "potongan_cuti=?,pph21=?,bpjs_kes_karyawan=?,bpjs_jht_karyawan=?," +
                "bpjs_jp_karyawan=?,total_bruto=?,total_potongan=?,gaji_bersih=?," +
                "status_bayar=?,catatan=? WHERE id_gaji=? AND is_locked=FALSE";
        try (PreparedStatement ps = DatabaseConfig.getConnection().prepareStatement(sql)) {
            ps.setDouble(1, p.getGajiPokok());
            ps.setDouble(2, p.getTunjanganIstri());
            ps.setDouble(3, p.getTunjanganAnak());
            ps.setDouble(4, p.getTransport());
            ps.setDouble(5, p.getUangMakan());
            ps.setDouble(6, p.getUpahLembur());
            ps.setDouble(7, p.getPotonganCuti());
            ps.setDouble(8, p.getPph21());
            ps.setDouble(9, p.getBpjsKesKaryawan());
            ps.setDouble(10, p.getBpjsJhtKaryawan());
            ps.setDouble(11, p.getBpjsJpKaryawan());
            ps.setDouble(12, p.getTotalBruto());
            ps.setDouble(13, p.getTotalPotongan());
            ps.setDouble(14, p.getGajiBersih());
            ps.setString(15, p.getStatusBayar());
            ps.setString(16, p.getCatatan());
            ps.setInt(17, p.getIdGaji());
            ps.executeUpdate();
        }
    }

    public void delete(int idGaji) throws SQLException {
        String sql = "DELETE FROM penggajian WHERE id_gaji=? AND is_locked=FALSE";
        try (PreparedStatement ps = DatabaseConfig.getConnection().prepareStatement(sql)) {
            ps.setInt(1, idGaji);
            ps.executeUpdate();
        }
    }

    private Penggajian map(ResultSet rs) throws SQLException {
        Penggajian p = new Penggajian();
        p.setIdGaji(rs.getInt("id_gaji"));
        p.setIdKaryawan(rs.getString("id_karyawan"));
        p.setNamaKaryawan(rs.getString("nama"));
        p.setPeriodeBulan(rs.getInt("periode_bulan"));
        p.setPeriodeTahun(rs.getInt("periode_tahun"));
        p.setGajiPokok(rs.getDouble("gaji_pokok"));
        p.setTunjanganIstri(rs.getDouble("tunjangan_istri"));
        p.setTunjanganAnak(rs.getDouble("tunjangan_anak"));
        p.setTransport(rs.getDouble("transport"));
        p.setUangMakan(rs.getDouble("uang_makan"));
        p.setUpahLembur(rs.getDouble("upah_lembur"));
        p.setPotonganCuti(rs.getDouble("potongan_cuti"));
        p.setPph21(rs.getDouble("pph21"));
        p.setBpjsKesKaryawan(rs.getDouble("bpjs_kes_karyawan"));
        p.setBpjsJhtKaryawan(rs.getDouble("bpjs_jht_karyawan"));
        p.setBpjsJpKaryawan(rs.getDouble("bpjs_jp_karyawan"));
        p.setBpjsKesPerusahaan(rs.getDouble("bpjs_kes_perusahaan"));
        p.setBpjsJhtPerusahaan(rs.getDouble("bpjs_jht_perusahaan"));
        p.setBpjsJpPerusahaan(rs.getDouble("bpjs_jp_perusahaan"));
        p.setBpjsJkk(rs.getDouble("bpjs_jkk"));
        p.setBpjsJkm(rs.getDouble("bpjs_jkm"));
        p.setTotalBruto(rs.getDouble("total_bruto"));
        p.setTotalPotongan(rs.getDouble("total_potongan"));
        p.setGajiBersih(rs.getDouble("gaji_bersih"));
        p.setProrata(rs.getBoolean("is_prorata"));
        p.setLocked(rs.getBoolean("is_locked"));
        p.setStatusBayar(rs.getString("status_bayar"));
        p.setCatatan(rs.getString("catatan"));
        Object hka = rs.getObject("hari_kerja_aktual");
        if (hka != null) p.setHariKerjaAktual((Integer) hka);
        Object hkb = rs.getObject("hari_kerja_bulan");
        if (hkb != null) p.setHariKerjaBulan((Integer) hkb);
        return p;
    }
}
