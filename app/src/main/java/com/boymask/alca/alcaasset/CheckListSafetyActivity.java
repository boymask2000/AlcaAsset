package com.boymask.alca.alcaasset;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.boymask.alca.alcaasset.common.Global;
import com.boymask.alca.alcaasset.common.Util;
import com.boymask.alca.alcaasset.rest.ApiService;
import com.boymask.alca.alcaasset.rest.RetrofitInstance;
import com.boymask.alca.alcaasset.rest.beans.ChecklistIntervento;
import com.boymask.alca.alcaasset.rest.beans.ChecklistRestBean;
import com.boymask.alca.alcaasset.rest.beans.InterventoRestBean;
import com.boymask.alca.alcaasset.rest.beans.SafetyChecklistRestBean;
import com.boymask.alca.alcaasset.rest.beans.SafetyRestBean;

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
              //  getChecklist(checklistRestBean.getAsset().getFacSystem());
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
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance(this);
        ApiService apiService = retrofit.create(ApiService.class);
        Single<InterventoRestBean> lista = apiService.getNextIntervento(rfid, "checklist");

        lista.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<InterventoRestBean>() {

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(InterventoRestBean interventoRestBean) {
                getChecklist(checklistRestBean.getAsset().getFacSystem());
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if( e instanceof NoSuchElementException){

                    Toast.makeText(CheckListSafetyActivity.this, "Nessun intervento trovato", Toast.LENGTH_LONG).show();
                 //   Util.showAlert(CheckListSafetyActivity.this, "Nessun intervento trovato");
                   finish();
                }

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
