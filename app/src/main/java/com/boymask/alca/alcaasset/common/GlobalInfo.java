package com.boymask.alca.alcaasset.common;

import com.boymask.alca.alcaasset.rest.beans.Asset;
import com.boymask.alca.alcaasset.rest.beans.InterventoRestBean;
import com.boymask.alca.alcaasset.rest.beans.Utente;

import java.io.Serializable;

public class GlobalInfo implements Serializable {
    private Utente user;
    private Asset asset;


    private InterventoRestBean interventoRestBean;

    public Utente getUser() {
        return user;
    }

    public void setUser(Utente user) {
        this.user = user;
    }

    public  Asset getAsset() {
        return asset;
    }

    public  void setAsset(Asset asset) {
        this.asset = asset;
    }

    public void setInterventoRestBean(InterventoRestBean interventoRestBean) {
        this.interventoRestBean=interventoRestBean;
    }
    public InterventoRestBean getInterventoRestBean() {
        return interventoRestBean;
    }

}
