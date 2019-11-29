package com.boymask.alca.alcaasset.rest.beans;

import java.io.Serializable;

public class ManualeFamiglia implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private long id;
    private long familyId;
    private String nomeFile;
    private String descr;
    private String shortDescr;
    private String ext;

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    private int tipo;


    private TypeManuale typeManuale;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getFamilyId() {
        return familyId;
    }

    public void setFamilyId(long familyId) {
        this.familyId = familyId;
    }

    public String getNomeFile() {
        return nomeFile;
    }

    public void setNomeFile(String nomeFile) {
        this.nomeFile = nomeFile;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getShortDescr() {
        return shortDescr;
    }

    public void setShortDescr(String shortDescr) {
        this.shortDescr = shortDescr;
    }


    public TypeManuale getTypeManuale() {
        return typeManuale;
    }

    public void setTypeManuale(TypeManuale typeManuale) {
        this.typeManuale = typeManuale;
        this.tipo = typeManuale.getId();
    }

}