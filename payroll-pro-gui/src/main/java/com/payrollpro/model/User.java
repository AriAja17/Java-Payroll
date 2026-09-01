package com.payrollpro.model;

import java.time.LocalDateTime;

public class User {
    private int id;
    private String username, namaLengkap, role;
    private String passwordHash;
    private boolean isActive;
    private LocalDateTime lastLogin, createdAt;

    public User() {}

    public boolean isAdmin() { return "Admin".equals(role); }

    public int    getId()                   { return id; }
    public void   setId(int v)              { this.id = v; }
    public String getUsername()             { return username; }
    public void   setUsername(String v)     { this.username = v; }
    public String getNamaLengkap()          { return namaLengkap; }
    public void   setNamaLengkap(String v)  { this.namaLengkap = v; }
    public String getRole()                 { return role; }
    public void   setRole(String v)         { this.role = v; }
    public String getPasswordHash()         { return passwordHash; }
    public void   setPasswordHash(String v) { this.passwordHash = v; }
    public boolean isActive()               { return isActive; }
    public void    setActive(boolean v)     { this.isActive = v; }
    public LocalDateTime getLastLogin()     { return lastLogin; }
    public void          setLastLogin(LocalDateTime v){ this.lastLogin = v; }
    public LocalDateTime getCreatedAt()     { return createdAt; }
    public void          setCreatedAt(LocalDateTime v){ this.createdAt = v; }
}
