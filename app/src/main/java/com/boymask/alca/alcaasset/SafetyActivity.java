package com.boymask.alca.alcaasset;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.boymask.alca.alcaasset.common.Global;
import com.boymask.alca.alcaasset.rest.ApiService;
import com.boymask.alca.alcaasset.rest.RetrofitInstance;
import com.boymask.alca.alcaasset.rest.beans.ChecklistIntervento;
import com.boymask.alca.alcaasset.rest.beans.ChecklistRestBean;
import com.boymask.alca.alcaasset.rest.beans.InterventoRestBean;
import com.boymask.alca.alcaasset.rest.beans.SafetyRestBean;

import java.io.Serializable;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class SafetyActivity extends Activity {
    private String rfid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safety);

        Bundle b = getIntent().getExtras();
        rfid = null; // or other values
        if (b != null)
            rfid = b.getString("assetKey");

        recuperaAsset();
    }

    private void recuperaAsset() {
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
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
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
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
                getSafety(interventoRestBean.getId());
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    private void getSafety(final long id) {
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
        ApiService apiService = retrofit.create(ApiService.class);
        final Single<SafetyRestBean> lista = apiService.getSafety(rfid, "checklist");

        lista.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<SafetyRestBean>() {


            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(SafetyRestBean safetyRestBean) {
                TextView testo = (TextView) findViewById(R.id.testo);
                testo.setText(safetyRestBean.getTesto());

                Button  ok = (Button) findViewById(R.id.ok);
                Button  cancel = (Button) findViewById(R.id.cancel);
                setOk(ok);
                setCancel(cancel);
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    private void setCancel(Button cancel) {
    }

    private void setOk(Button ok) {
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SafetyActivity.this, CheckListActivity.class);
                Bundle b = new Bundle();
                b.putString("assetKey", rfid);
                intent.putExtras(b);
                startActivityForResult(intent,1);
            }});
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 1) {
            finish();
        }
    }

}
