package com.payrollpro.model;

import java.time.LocalDate;
import java.time.Period;

public class Karyawan {
    private String idKaryawan, idGolongan, nama, jenisKelamin;
    private String tempatLahir, alamat, statusNikah, statusKaryawan, npwp;
    private LocalDate tanggalLahir, tanggalMasuk, tanggalResign;
    private int jumlahAnak;
    private Golongan golongan;

    public Karyawan() {}

    public int getMasaKerjaBulan() {
        if (tanggalMasuk == null) return 0;
        LocalDate selesai = (tanggalResign != null) ? tanggalResign : LocalDate.now();
        return (int) java.time.temporal.ChronoUnit.MONTHS.between(tanggalMasuk, selesai);
    }

    public boolean isProba() { return getMasaKerjaBulan() < 3 || "Probation".equals(statusKaryawan); }
    public boolean isMenikah() { return "Menikah".equals(statusNikah); }

    public String getPtkpKode() {
        if (isMenikah()) {
            return "K/" + Math.min(jumlahAnak, 3);
        }
        return "TK/" + Math.min(jumlahAnak, 3);
    }

    public String getIdKaryawan()           { return idKaryawan; }
    public void   setIdKaryawan(String v)   { this.idKaryawan = v; }
    public String getIdGolongan()           { return idGolongan; }
    public void   setIdGolongan(String v)   { this.idGolongan = v; }
    public String getNama()                 { return nama; }
    public void   setNama(String v)         { this.nama = v; }
    public String getJenisKelamin()         { return jenisKelamin; }
    public void   setJenisKelamin(String v) { this.jenisKelamin = v; }
    public String getTempatLahir()          { return tempatLahir; }
    public void   setTempatLahir(String v)  { this.tempatLahir = v; }
    public String getAlamat()               { return alamat; }
    public void   setAlamat(String v)       { this.alamat = v; }
    public String getStatusNikah()          { return statusNikah; }
    public void   setStatusNikah(String v)  { this.statusNikah = v; }
    public String getStatusKaryawan()         { return statusKaryawan; }
    public void   setStatusKaryawan(String v) { this.statusKaryawan = v; }
    public String getNpwp()                 { return npwp; }
    public void   setNpwp(String v)         { this.npwp = v; }
    public LocalDate getTanggalLahir()        { return tanggalLahir; }
    public void      setTanggalLahir(LocalDate v) { this.tanggalLahir = v; }
    public LocalDate getTanggalMasuk()        { return tanggalMasuk; }
    public void      setTanggalMasuk(LocalDate v) { this.tanggalMasuk = v; }
    public LocalDate getTanggalResign()       { return tanggalResign; }
    public void      setTanggalResign(LocalDate v){ this.tanggalResign = v; }
    public int  getJumlahAnak()             { return jumlahAnak; }
    public void setJumlahAnak(int v)        { this.jumlahAnak = v; }
    public Golongan getGolongan()           { return golongan; }
    public void     setGolongan(Golongan v) { this.golongan = v; }

    @Override public String toString() { return idKaryawan + " - " + nama; }
}
