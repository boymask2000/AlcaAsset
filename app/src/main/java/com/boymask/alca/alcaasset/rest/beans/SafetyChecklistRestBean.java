package com.boymask.alca.alcaasset.rest.beans;

import java.io.Serializable;
import java.util.List;

public class SafetyChecklistRestBean implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;



    public List<SafetyRestBean> getLista() {
        return lista;
    }

    public void setLista(List<SafetyRestBean> lista) {
        this.lista = lista;
    }

    private String family;

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    private List<SafetyRestBean> lista;

}