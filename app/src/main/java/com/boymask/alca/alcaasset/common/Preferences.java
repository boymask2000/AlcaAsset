package com.boymask.alca.alcaasset.common;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preferences {
    public static String getHostname(Activity act) {
        return getParameter("hostname", act);
    }

    public static String getParameter(String key, Context ctx) {
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(ctx);
        return SP.getString(key, "NA");
    }

    public static String getBaseUrl(Context ctx) {
        String url;
        String protocol = "http";
        String port = "8080";
        String base = "Asset2/rest/";
        String BASE_URL = "http://2.232.192.230:8080/Asset2/rest/";

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        String server = (String) prefs.getString("hostname", "");
        if (server != null && server.trim().length() > 0)
            url = protocol + "://" + server + ":" + port + "/" + base;
        else
            url = BASE_URL;

        return url;
    }
}
