package com.boymask.alca.alcaasset.network;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ConnectionQuality;
import com.androidnetworking.interfaces.ConnectionQualityChangeListener;

public class NetworkUtil {

    public static void removeNetworkMonitor() {
        Log.d("NET", "Remove NetworkMonitor");
        AndroidNetworking.removeConnectionQualityChangeListener();
    }

    public static void setNetworkMonitor(Context ctx, final TextView netstat) {
        Log.d("NET", "Set NetworkMonitor");

        AndroidNetworking.setConnectionQualityChangeListener(new ConnectionQualityChangeListener() {
            @Override
            public void onChange(ConnectionQuality connectionQuality, int currentBandwidth) {

                if(connectionQuality == ConnectionQuality.EXCELLENT){
                    Log.d("NET", "Connection EXCELLENT");
                    netstat.setText("EXCELLENT");
                }else if (connectionQuality == ConnectionQuality.GOOD){  netstat.setText("GOOD");
                    Log.d("NET", "Connection GOOD");
                }else if (connectionQuality == ConnectionQuality.MODERATE){  netstat.setText("MODERATE");
                    Log.d("NET", "Connection MODERATE");
                }else if (connectionQuality == ConnectionQuality.POOR){  netstat.setText("POOR");
                    Log.d("NET", "Connection POOR");
                }else if (connectionQuality == ConnectionQuality.UNKNOWN){  netstat.setText("UNKNOWN");
                    Log.d("NET", "Connection UNKNOWN");
                }
             // Note : if (currentBandwidth == 0) : means UNKNOWN
                Log.d("NET", "currentBandwidth: "+currentBandwidth);
            }
        });
    }
}