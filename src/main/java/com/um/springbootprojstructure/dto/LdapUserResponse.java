package com.um.springbootprojstructure.dto;

public class LdapUserResponse {

    private String dn;
    private String uid;
    private String cn;
    private String sn;
    private String givenName;
    private String mail;

    public LdapUserResponse() {}

    public LdapUserResponse(String dn, String uid, String cn, String sn, String givenName, String mail) {
        this.dn = dn;
        this.uid = uid;
        this.cn = cn;
        this.sn = sn;
        this.givenName = givenName;
        this.mail = mail;
    }

    public String getDn() { return dn; }
    public void setDn(String dn) { this.dn = dn; }

    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }

    public String getCn() { return cn; }
    public void setCn(String cn) { this.cn = cn; }

    public String getSn() { return sn; }
    public void setSn(String sn) { this.sn = sn; }

    public String getGivenName() { return givenName; }
    public void setGivenName(String givenName) { this.givenName = givenName; }

    public String getMail() { return mail; }
    public void setMail(String mail) { this.mail = mail; }
}