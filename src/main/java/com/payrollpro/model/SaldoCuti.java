package com.payrollpro.model;

public class SaldoCuti {
    private int id, idJenis, tahun, kuota, terpakai;
    private String idKaryawan, namaKaryawan, namaJenis;

    public SaldoCuti() {}

    public int getSisa() { return kuota - terpakai; }

    public int    getId()                   { return id; }
    public void   setId(int v)              { this.id = v; }
    public String getIdKaryawan()           { return idKaryawan; }
    public void   setIdKaryawan(String v)   { this.idKaryawan = v; }
    public String getNamaKaryawan()         { return namaKaryawan; }
    public void   setNamaKaryawan(String v) { this.namaKaryawan = v; }
    public int    getIdJenis()              { return idJenis; }
    public void   setIdJenis(int v)         { this.idJenis = v; }
    public String getNamaJenis()            { return namaJenis; }
    public void   setNamaJenis(String v)    { this.namaJenis = v; }
    public int    getTahun()               { return tahun; }
    public void   setTahun(int v)          { this.tahun = v; }
    public int    getKuota()               { return kuota; }
    public void   setKuota(int v)          { this.kuota = v; }
    public int    getTerpakai()            { return terpakai; }
    public void   setTerpakai(int v)       { this.terpakai = v; }
}
