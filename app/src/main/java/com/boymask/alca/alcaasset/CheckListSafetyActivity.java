package com.boymask.alca.alcaasset;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.boymask.alca.alcaasset.common.Global;
import com.boymask.alca.alcaasset.common.Preferences;
import com.boymask.alca.alcaasset.common.Util;
import com.boymask.alca.alcaasset.rest.ApiService;
import com.boymask.alca.alcaasset.rest.RetrofitInstance;
import com.boymask.alca.alcaasset.rest.beans.ChecklistRestBean;
import com.boymask.alca.alcaasset.rest.beans.InterventiRealTimeHelper;
import com.boymask.alca.alcaasset.rest.beans.InterventoRealTime;
import com.boymask.alca.alcaasset.rest.beans.InterventoRestBean;
import com.boymask.alca.alcaasset.rest.beans.SafetyChecklistRestBean;
import com.boymask.alca.alcaasset.rest.beans.SafetyRestBean;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;
import java.util.NoSuchElementException;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class CheckListSafetyActivity extends Activity {

    private String rfid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list);


        Bundle b = getIntent().getExtras();
        rfid = null; // or other values
        if (b != null)
            rfid = b.getString("assetKey");

        recuperaAsset();


    }

    private void recuperaAsset() {
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance(this);
        ApiService apiService = retrofit.create(ApiService.class);
        Single<ChecklistRestBean> sing = apiService.getChecklist(rfid, "checklist");

        sing.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<ChecklistRestBean>() {


            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(ChecklistRestBean checklistRestBean) {
                if (checklistRestBean.getAsset() == null) {

                    Toast.makeText(getApplicationContext(),
                            R.string.asset_non_trovato, Toast.LENGTH_LONG).show();

                    finish();
                    return;
                }
                Global.setAsset(checklistRestBean.getAsset());

                recuperaIntervento(checklistRestBean);
            }

            @Override
            public void onError(Throwable e) {

                if (e instanceof java.net.ConnectException) {
                    //   Util.showMessage(findViewById(R.id.button), R.string.problemi_di_collegamento);
                    Toast.makeText(CheckListSafetyActivity.this, R.string.problemi_di_collegamento, Toast.LENGTH_LONG).show();
                    finish();
                    return;
                }
            }
        });

    }


    private void recuperaIntervento(final ChecklistRestBean checklistRestBean) {
        String baseUrl = Preferences.getBaseUrl(this);
        AndroidNetworking.get(baseUrl + "intervento/getnext/{rfid}")
                .addPathParameter("rfid", rfid)
                .setTag(this)
                .setPriority(Priority.LOW)
                .build()
                .getAsObject(InterventoRestBean.class, new ParsedRequestListener<InterventoRestBean>() {
                    @Override
                    public void onResponse(InterventoRestBean interventoRestBean) {
                        // do anything with response
                        Log.d("WW", "id : " + interventoRestBean.getId());
                        getChecklist(checklistRestBean.getAsset().getFacSystem());
                        InterventiRealTimeHelper.notificaInizioIntervento(interventoRestBean, CheckListSafetyActivity.this);

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("ERR", anError.toString());
                    }
                });
    }


    private void getChecklist(final String family) {
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance(this);
        ApiService apiService = retrofit.create(ApiService.class);
        final Single<List<SafetyRestBean>> lista = apiService.getSafetyChecklistForFamily(family, "checklist");

        lista.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<SafetyRestBean>>() {


            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(List<SafetyRestBean> safetyRestBeans) {
                SafetyChecklistRestBean crb = new SafetyChecklistRestBean();
                crb.setFamily(family);
                crb.setLista(safetyRestBeans);
                Intent intent = new Intent(CheckListSafetyActivity.this, ScrollingSafetyActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("SafetyChecklistRestBean", (Serializable) crb);
                b.putString("rfid", rfid);
                intent.putExtras(b);
                startActivityForResult(intent, 1);
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 1) {
            finish();
        }
    }


}
