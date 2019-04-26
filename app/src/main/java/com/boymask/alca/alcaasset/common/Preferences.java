package com.boymask.alca.alcaasset.common;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preferences {
    public static String getHostname(Activity act){
        return getParameter("hostname", act);
    }
    public static String getParameter(String key , Context ctx){
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(ctx);
       return SP.getString(key, "NA");
    }
}
