package com.boymask.alca.alcaasset.rest.beans;


import java.io.Serializable;

public class Famiglia implements Serializable{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private long id;
    private String famiglia;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFamiglia() {
        return famiglia;
    }

    public void setFamiglia(String famiglia) {
        this.famiglia = famiglia;
    }
}
