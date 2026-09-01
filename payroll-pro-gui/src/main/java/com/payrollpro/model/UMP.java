package com.payrollpro.model;

public class UMP {
    private int id, tahun;
    private String wilayah;
    private double nilaiUmp;

    public UMP() {}

    public int    getId()                   { return id; }
    public void   setId(int v)              { this.id = v; }
    public String getWilayah()              { return wilayah; }
    public void   setWilayah(String v)      { this.wilayah = v; }
    public int    getTahun()               { return tahun; }
    public void   setTahun(int v)          { this.tahun = v; }
    public double getNilaiUmp()             { return nilaiUmp; }
    public void   setNilaiUmp(double v)     { this.nilaiUmp = v; }

    @Override public String toString() { return wilayah + " (" + tahun + ")"; }
}
