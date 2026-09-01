package com.payrollpro.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PengajuanCuti {
    private int idCuti, idJenis, jumlahHari;
    private String idKaryawan, namaKaryawan, namaJenis;
    private String alasan, status, diprosesOleh, catatanAdmin;
    private LocalDate tglMulai, tglSelesai;
    private LocalDateTime tglProses, createdAt;

    public PengajuanCuti() {}

    public int    getIdCuti()               { return idCuti; }
    public void   setIdCuti(int v)          { this.idCuti = v; }
    public String getIdKaryawan()           { return idKaryawan; }
    public void   setIdKaryawan(String v)   { this.idKaryawan = v; }
    public String getNamaKaryawan()         { return namaKaryawan; }
    public void   setNamaKaryawan(String v) { this.namaKaryawan = v; }
    public int    getIdJenis()              { return idJenis; }
    public void   setIdJenis(int v)         { this.idJenis = v; }
    public String getNamaJenis()            { return namaJenis; }
    public void   setNamaJenis(String v)    { this.namaJenis = v; }
    public LocalDate getTglMulai()          { return tglMulai; }
    public void      setTglMulai(LocalDate v){ this.tglMulai = v; }
    public LocalDate getTglSelesai()        { return tglSelesai; }
    public void      setTglSelesai(LocalDate v){ this.tglSelesai = v; }
    public int    getJumlahHari()           { return jumlahHari; }
    public void   setJumlahHari(int v)      { this.jumlahHari = v; }
    public String getAlasan()               { return alasan; }
    public void   setAlasan(String v)       { this.alasan = v; }
    public String getStatus()               { return status; }
    public void   setStatus(String v)       { this.status = v; }
    public String getDiprosesOleh()         { return diprosesOleh; }
    public void   setDiprosesOleh(String v) { this.diprosesOleh = v; }
    public String getCatatanAdmin()         { return catatanAdmin; }
    public void   setCatatanAdmin(String v) { this.catatanAdmin = v; }
    public LocalDateTime getTglProses()     { return tglProses; }
    public void          setTglProses(LocalDateTime v){ this.tglProses = v; }
    public LocalDateTime getCreatedAt()     { return createdAt; }
    public void          setCreatedAt(LocalDateTime v){ this.createdAt = v; }
}
