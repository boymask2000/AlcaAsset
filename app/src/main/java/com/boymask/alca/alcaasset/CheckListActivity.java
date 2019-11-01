package com.boymask.alca.alcaasset;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.boymask.alca.alcaasset.common.Global;
import com.boymask.alca.alcaasset.common.Util;
import com.boymask.alca.alcaasset.rest.ApiService;
import com.boymask.alca.alcaasset.rest.RetrofitInstance;
import com.boymask.alca.alcaasset.rest.beans.ChecklistIntervento;
import com.boymask.alca.alcaasset.rest.beans.ChecklistRestBean;
import com.boymask.alca.alcaasset.rest.beans.InterventoRestBean;

import java.io.Serializable;
import java.util.List;
import java.util.NoSuchElementException;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class CheckListActivity extends Activity {

    private String rfid;
    private String family;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list);


        Bundle b = getIntent().getExtras();
        rfid = null; // or other values
        if (b != null) {
            rfid = b.getString("assetKey");
            family = b.getString("family");
        }
        recuperaAsset();


    }
    private void recuperaAsset() {
        recuperaIntervento();
    }
    private void recuperaAsset1() {
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
                if( checklistRestBean.getAsset()==null){

                    Toast.makeText(getApplicationContext(),
                            R.string.asset_non_trovato, Toast.LENGTH_LONG).show();
                    /*Util.showMessage(findViewById(R.id.coordinatorLayout), R.string.asset_non_trovato);

                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }*/
                    finish();
                    return;
                }
                Log.d("GGG", "" + checklistRestBean.getAsset().getId());
                Global.setAsset(checklistRestBean.getAsset());

                recuperaIntervento();
            }

            @Override
            public void onError(Throwable e) {
Log.d("lll", e.getMessage());
            if( e instanceof java.net.ConnectException){
           //     Util.showMessage(findViewById(R.id.button), R.string.problemi_di_collegamento);

            }
            }
        });

    }

    private void recuperaIntervento() {
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
                Log.d("hh", "" + interventoRestBean.getId());
                Log.d("hh", "" + interventoRestBean.getData_pianificata());
                getChecklist(interventoRestBean.getId());
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();

                Util.showAlert(getApplicationContext(), "Errore server "+e.getMessage());
                if( e instanceof NoSuchElementException){
                 //   Util.showAlert(getApplicationContext(), "Errore server ");
                    finish();
                }

            }
        });
    }

    private void getChecklist(final long id) {
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance(this);
        ApiService apiService = retrofit.create(ApiService.class);
        final Single<List<ChecklistIntervento>> lista = apiService.getChecksForIntervento(id, "checklist");

        lista.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<ChecklistIntervento>>() {


            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(List<ChecklistIntervento> checklistRestBeans) {
                ChecklistRestBean crb = new ChecklistRestBean();
                crb.setInterventoId(id);
                crb.setAssetId(rfid);
                crb.setLista(checklistRestBeans);
                Intent intent = new Intent(CheckListActivity.this, ScrollingActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("ChecklistRestBean", (Serializable) crb);
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
