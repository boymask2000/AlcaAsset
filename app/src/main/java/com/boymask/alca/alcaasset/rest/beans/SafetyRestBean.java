package com.boymask.alca.alcaasset.rest.beans;

import java.io.Serializable;

public class SafetyRestBean implements Serializable {

    private long id;
    private long familyid;
    private String risk_en;
    private String risk_it;



    private int imgId;

    public String getRisk_en() {
        return risk_en;
    }

    public void setRisk_en(String risk_en) {
        this.risk_en = risk_en;
    }

    public String getRisk_it() {
        return risk_it;
    }

    public void setRisk_it(String risk_it) {
        this.risk_it = risk_it;
    }

    public String getPpe_en() {
        return ppe_en;
    }

    public void setPpe_en(String ppe_en) {
        this.ppe_en = ppe_en;
    }

    public String getPpe_it() {
        return ppe_it;
    }

    public void setPpe_it(String ppe_it) {
        this.ppe_it = ppe_it;
    }

    private String ppe_en;
    private String ppe_it;
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
    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }
    private String testo;
}
