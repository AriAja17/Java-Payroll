package com.payrollpro.model;

import java.time.LocalDateTime;

public class AuditLog {
    private int id;
    private String username, aksi, tabel, idRecord;
    private String dataSebelum, dataSesudah, keterangan, ipAddress;
    private LocalDateTime createdAt;

    public AuditLog() {}
    public AuditLog(String username, String aksi, String tabel, String idRecord, String keterangan) {
        this.username = username; this.aksi = aksi;
        this.tabel = tabel; this.idRecord = idRecord;
        this.keterangan = keterangan;
    }

    public int    getId()                   { return id; }
    public void   setId(int v)              { this.id = v; }
    public String getUsername()             { return username; }
    public void   setUsername(String v)     { this.username = v; }
    public String getAksi()                 { return aksi; }
    public void   setAksi(String v)         { this.aksi = v; }
    public String getTabel()                { return tabel; }
    public void   setTabel(String v)        { this.tabel = v; }
    public String getIdRecord()             { return idRecord; }
    public void   setIdRecord(String v)     { this.idRecord = v; }
    public String getDataSebelum()          { return dataSebelum; }
    public void   setDataSebelum(String v)  { this.dataSebelum = v; }
    public String getDataSesudah()          { return dataSesudah; }
    public void   setDataSesudah(String v)  { this.dataSesudah = v; }
    public String getKeterangan()           { return keterangan; }
    public void   setKeterangan(String v)   { this.keterangan = v; }
    public String getIpAddress()            { return ipAddress; }
    public void   setIpAddress(String v)    { this.ipAddress = v; }
    public LocalDateTime getCreatedAt()     { return createdAt; }
    public void          setCreatedAt(LocalDateTime v){ this.createdAt = v; }
}
