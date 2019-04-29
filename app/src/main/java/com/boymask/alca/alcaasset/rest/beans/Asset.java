package com.boymask.alca.alcaasset.rest.beans;


import java.io.Serializable;

public class Asset implements Serializable {
    private long id;

    private String facNum;
    private String facSystem;
    private String facSubsystem;
    private String assemblyCategory;
    private String nomenclature;
    private String procId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFacNum() {
        return facNum;
    }

    public void setFacNum(String facNum) {
        this.facNum = facNum;
    }

    public String getFacSystem() {
        return facSystem;
    }

    public void setFacSystem(String facSystem) {
        this.facSystem = facSystem;
    }

    public String getFacSubsystem() {
        return facSubsystem;
    }

    public void setFacSubsystem(String facSubsystem) {
        this.facSubsystem = facSubsystem;
    }

    public String getAssemblyCategory() {
        return assemblyCategory;
    }

    public void setAssemblyCategory(String assemblyCategory) {
        this.assemblyCategory = assemblyCategory;
    }

    public String getNomenclature() {
        return nomenclature;
    }

    public void setNomenclature(String nomenclature) {
        this.nomenclature = nomenclature;
    }

    public String getProcId() {
        return procId;
    }

    public void setProcId(String procId) {
        this.procId = procId;
    }

    public String getPmSchedRecipient() {
        return pmSchedRecipient;
    }

    public void setPmSchedRecipient(String pmSchedRecipient) {
        this.pmSchedRecipient = pmSchedRecipient;
    }

    public String getPmSchedSerial() {
        return pmSchedSerial;
    }

    public void setPmSchedSerial(String pmSchedSerial) {
        this.pmSchedSerial = pmSchedSerial;
    }

    public String getRpieIdIndividual() {
        return rpieIdIndividual;
    }

    public void setRpieIdIndividual(String rpieIdIndividual) {
        this.rpieIdIndividual = rpieIdIndividual;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getSchedAssignedOrg() {
        return schedAssignedOrg;
    }

    public void setSchedAssignedOrg(String schedAssignedOrg) {
        this.schedAssignedOrg = schedAssignedOrg;
    }

    public String getLastStatus() {
        return lastStatus;
    }

    public void setLastStatus(String lastStatus) {
        this.lastStatus = lastStatus;
    }

    private String pmSchedRecipient;
    private String pmSchedSerial;
    private String rpieIdIndividual;
    private String frequency;
    private String schedAssignedOrg;
    private String lastStatus="0";


}
