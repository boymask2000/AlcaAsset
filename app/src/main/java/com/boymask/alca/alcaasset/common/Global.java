package com.boymask.alca.alcaasset.common;

import com.boymask.alca.alcaasset.rest.beans.Asset;

public class Global {
    public static Asset getAsset() {
        return asset;
    }

    public static void setAsset(Asset asset) {
        Global.asset = asset;
    }

    private static Asset asset;
}
