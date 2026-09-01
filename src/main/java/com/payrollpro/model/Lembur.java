package com.payrollpro.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Lembur {
    private int idLembur;
    private String idKaryawan, namaKaryawan, keterangan;
    private LocalDate tanggalLembur;
    private int jamLembur;
    private boolean isHariLibur;
    private double tarifPerJam, totalUpah;
    private LocalDateTime createdAt;

    public Lembur() {}

    public int    getIdLembur()               { return idLembur; }
    public void   setIdLembur(int v)          { this.idLembur = v; }
    public String getIdKaryawan()             { return idKaryawan; }
    public void   setIdKaryawan(String v)     { this.idKaryawan = v; }
    public String getNamaKaryawan()           { return namaKaryawan; }
    public void   setNamaKaryawan(String v)   { this.namaKaryawan = v; }
    public LocalDate getTanggalLembur()       { return tanggalLembur; }
    public void      setTanggalLembur(LocalDate v) { this.tanggalLembur = v; }
    public int    getJamLembur()              { return jamLembur; }
    public void   setJamLembur(int v)         { this.jamLembur = v; }
    public boolean isHariLibur()              { return isHariLibur; }
    public void    setHariLibur(boolean v)    { this.isHariLibur = v; }
    public double getTarifPerJam()            { return tarifPerJam; }
    public void   setTarifPerJam(double v)    { this.tarifPerJam = v; }
    public double getTotalUpah()              { return totalUpah; }
    public void   setTotalUpah(double v)      { this.totalUpah = v; }
    public String getKeterangan()             { return keterangan; }
    public void   setKeterangan(String v)     { this.keterangan = v; }
    public LocalDateTime getCreatedAt()       { return createdAt; }
    public void          setCreatedAt(LocalDateTime v) { this.createdAt = v; }
}
