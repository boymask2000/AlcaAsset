package com.boymask.alca.alcaasset.rest.beans;

import java.io.Serializable;
import java.util.List;

public class ChecklistRestBean implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Asset asset;

    public long getInterventoId() {
        return interventoId;
    }

    public void setInterventoId(long interventoId) {
        this.interventoId = interventoId;
    }

    private long interventoId;
    private List<ChecklistIntervento> lista;
    public Asset getAsset() {
        return asset;
    }
    public void setAsset(Asset asset) {
        this.asset = asset;
    }
    public List<ChecklistIntervento> getLista() {
        return lista;
    }
    public void setLista(List<ChecklistIntervento> lista) {
        this.lista = lista;
    }
}