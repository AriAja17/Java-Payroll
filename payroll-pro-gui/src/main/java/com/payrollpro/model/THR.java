package com.payrollpro.model;

import java.time.LocalDate;

public class THR {
    private int idThr, masaKerjaBulan;
    private String idKaryawan, namaKaryawan, jenisHariRaya, statusBayar;
    private int tahun;
    private double gajiPokok, faktor, nominalThr;
    private LocalDate tanggalBayar;

    public THR() {}

    public int    getIdThr()                { return idThr; }
    public void   setIdThr(int v)           { this.idThr = v; }
    public String getIdKaryawan()           { return idKaryawan; }
    public void   setIdKaryawan(String v)   { this.idKaryawan = v; }
    public String getNamaKaryawan()         { return namaKaryawan; }
    public void   setNamaKaryawan(String v) { this.namaKaryawan = v; }
    public int    getTahun()               { return tahun; }
    public void   setTahun(int v)          { this.tahun = v; }
    public String getJenisHariRaya()          { return jenisHariRaya; }
    public void   setJenisHariRaya(String v)  { this.jenisHariRaya = v; }
    public int    getMasaKerjaBulan()       { return masaKerjaBulan; }
    public void   setMasaKerjaBulan(int v)  { this.masaKerjaBulan = v; }
    public double getGajiPokok()            { return gajiPokok; }
    public void   setGajiPokok(double v)    { this.gajiPokok = v; }
    public double getFaktor()               { return faktor; }
    public void   setFaktor(double v)       { this.faktor = v; }
    public double getNominalThr()           { return nominalThr; }
    public void   setNominalThr(double v)   { this.nominalThr = v; }
    public String getStatusBayar()          { return statusBayar; }
    public void   setStatusBayar(String v)  { this.statusBayar = v; }
    public LocalDate getTanggalBayar()      { return tanggalBayar; }
    public void      setTanggalBayar(LocalDate v){ this.tanggalBayar = v; }
}
