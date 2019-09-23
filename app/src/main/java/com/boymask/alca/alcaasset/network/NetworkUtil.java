package com.boymask.alca.alcaasset.network;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtil {
    public static final int TYPE_WIFI = 1;
    public static final int TYPE_MOBILE = 2;
    public static final int TYPE_NOT_CONNECTED = 0;
    public static final int NETWORK_STATUS_NOT_CONNECTED = 0;
    public static final int NETWORK_STATUS_WIFI = 1;
    public static final int NETWORK_STATUS_MOBILE = 2;

    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public static String getConnectivityStatusString(Context context) {
        int conn = NetworkUtil.getConnectivityStatus(context);
        String status = "";
        if (conn == NetworkUtil.TYPE_WIFI) {
            status = "NETWORK_STATUS_WIFI";
        } else if (conn == NetworkUtil.TYPE_MOBILE) {
            status = "NETWORK_STATUS_MOBILE";
        } else if (conn == NetworkUtil.TYPE_NOT_CONNECTED) {
            status = "NETWORK_STATUS_NOT_CONNECTED";
        }
        return status;
    }

    public static void initNetworkMonitor(Context ctx) {
        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_ACTION");
        ctx.registerReceiver(new NetworkChangeReceiver(), intentFilter);
    }
}