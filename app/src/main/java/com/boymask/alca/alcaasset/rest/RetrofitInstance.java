package com.boymask.alca.alcaasset.rest;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {

    private static Retrofit retrofit;
    private static String protocol = "http";
    private static String port = "8080";
    private static String base = "Asset2/rest/";
    private static String BASE_URL = "http://2.232.192.230:8080/Asset2/rest/";

    public static Retrofit getRetrofitInstance(Context ctx) {
        String url;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        String server = prefs.getString("hostname", "");

        if (server != null && server.trim().length() > 0)
            url = protocol + "://" + server + ":" + port + "/" + base;
        else
            url = BASE_URL;

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }
}