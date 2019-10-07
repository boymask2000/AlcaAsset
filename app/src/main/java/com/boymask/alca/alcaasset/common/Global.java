package com.boymask.alca.alcaasset.common;

import com.boymask.alca.alcaasset.rest.beans.Asset;
import com.boymask.alca.alcaasset.rest.beans.Utente;

public class Global {
    public static Asset getAsset() {
        return asset;
    }

    public static void setAsset(Asset asset) {
        Global.asset = asset;
    }

    private static Asset asset;


    private static Utente user;

    public static void setUser(Utente u) {
        user = u;
    }

    public static Utente getUser() {
        return user;
    }

}
