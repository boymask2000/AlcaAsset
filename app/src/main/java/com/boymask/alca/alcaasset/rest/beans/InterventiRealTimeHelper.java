package com.boymask.alca.alcaasset.rest.beans;

import android.app.Activity;
import android.content.Context;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.boymask.alca.alcaasset.common.Global;
import com.boymask.alca.alcaasset.common.Preferences;

import org.json.JSONObject;

public class InterventiRealTimeHelper {
    public static void notificaInizioIntervento(InterventoRestBean interventoRestBean, Context ctx) {
        InterventoRealTime irt = new InterventoRealTime();
        irt.setUser(Global.getUser().getUsername());
        irt.setAssetRMP(Global.getAsset().getRpieIdIndividual());
        irt.setInterventoid(interventoRestBean.getId());
        irt.setEsito(0);
        irt.setStato("STARTED");

        String baseUrl = Preferences.getBaseUrl(ctx);
        AndroidNetworking.post(baseUrl + "interventiRealTime/inizioIntervento")
                .addApplicationJsonBody(irt) // posting java object
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {


                    }

                    @Override
                    public void onError(ANError error) {
                        error.printStackTrace();

                    }
                });
    }

    public static void notificaFineIntervento(InterventoRestBean inter, Context ctx) {
        InterventoRealTime irt = new InterventoRealTime();
        irt.setUser(Global.getUser().getUsername());
        irt.setAssetRMP(Global.getAsset().getRpieIdIndividual());
        irt.setInterventoid(inter.getId());
        irt.setEsito(inter.getEsito());
        irt.setStato("COMPLETED");

        String baseUrl = Preferences.getBaseUrl(ctx);
        AndroidNetworking.post(baseUrl + "interventiRealTime/fineIntervento")
                .addApplicationJsonBody(irt) // posting java object
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {


                    }

                    @Override
                    public void onError(ANError error) {
                        error.printStackTrace();

                    }
                });
    }
}
