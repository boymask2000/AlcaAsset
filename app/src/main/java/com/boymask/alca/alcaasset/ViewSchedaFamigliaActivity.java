package com.boymask.alca.alcaasset;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.boymask.alca.alcaasset.common.Preferences;
import com.boymask.alca.alcaasset.rest.beans.Asset;
import com.boymask.alca.alcaasset.rest.beans.Famiglia;
import com.boymask.alca.alcaasset.rest.beans.ManualeFamiglia;

public class ViewSchedaFamigliaActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_scheda_famiglia);

        Button close = (Button) findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Bundle b = getIntent().getExtras();
        String rmpId = null;
        if (b != null)
            rmpId = b.getString("assetRMPID");

        getAssetByRMPID(rmpId);
    }

    private void getAssetByRMPID(String rmpId) {
        String baseUrl = Preferences.getBaseUrl(this);
        String url = baseUrl + "asset/getbyrmpid/{rmpid}";
        AndroidNetworking.get(url)
                .addPathParameter("rmpid", rmpId)
                .setTag(this)
                .setPriority(Priority.LOW)
                .build()
                .getAsObject(Asset.class, new ParsedRequestListener<Asset>() {
                    @Override
                    public void onResponse(Asset asset) {
                        /* do anything with response */
                        Log.d("fam", "id : " + asset.getId());
                        Log.d("fam", "famiglia : " + asset.getFacSystem());

                        getFamilyId(asset.getFacSystem());
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("ERR", anError.getErrorBody());
                    }


                });

    }

    private void getFamilyId(String familyName) {
        String baseUrl = Preferences.getBaseUrl(this);
        String url = baseUrl + "famiglia/getbyname/{name}";
        AndroidNetworking.get(url)
                .addPathParameter("name", familyName)
                .setTag(this)
                .setPriority(Priority.LOW)
                .build()
                .getAsObject(Famiglia.class, new ParsedRequestListener<Famiglia>() {
                    @Override
                    public void onResponse(Famiglia fam) {
                        /* do anything with response */
                        Log.d("fam", "id : " + fam.getId());
                        Log.d("fam", "famiglia : " + fam.getFamiglia());

                        getFilePDF(fam.getId());

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("ERR", anError.getErrorBody());
                    }


                });
    }

    private void getFilePDF(long famid) {

        String baseUrl = Preferences.getBaseUrl(this);
        String url = baseUrl + "famiglia/getpdfscheda/{famid}";
        AndroidNetworking.get(url)
                .addPathParameter("famid", "" + famid)
                .setTag(this)
                .setPriority(Priority.LOW)
                .build()
                .getAsObject(ManualeFamiglia.class, new ParsedRequestListener<ManualeFamiglia>() {
                    @Override
                    public void onResponse(ManualeFamiglia manuale) {
                        /* do anything with response */
                        Log.d("fam", "filename : " + manuale.getNomeFile());


                        downloadPDF(manuale.getNomeFile());

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("ERR", "" + anError);
                    }


                });
    }

    private void downloadPDF(String filename) {

        String baseUrl = Preferences.getBaseUrlNoRest(this) + "resources/ManualiFamiglia/" + filename;

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(
                Uri.parse(baseUrl),
                "application/pdf");

        startActivityForResult(intent,4);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 4) {
            finish();
        }
    }
}
