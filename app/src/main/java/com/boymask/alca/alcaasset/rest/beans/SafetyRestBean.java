package com.boymask.alca.alcaasset.rest.beans;

public class SafetyRestBean {
    private long id;
    private long familyid;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getFamilyid() {
        return familyid;
    }

    public void setFamilyid(long familyid) {
        this.familyid = familyid;
    }

    public String getTesto() {
        return testo;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

    private String testo;
}
