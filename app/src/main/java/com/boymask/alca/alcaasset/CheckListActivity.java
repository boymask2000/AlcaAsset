package com.boymask.alca.alcaasset;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.boymask.alca.alcaasset.common.Beep;
import com.boymask.alca.alcaasset.rest.ApiService;
import com.boymask.alca.alcaasset.rest.RetrofitInstance;
import com.boymask.alca.alcaasset.rest.beans.ChecklistRestBean;

import java.io.Serializable;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class CheckListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list);


        Bundle b = getIntent().getExtras();
        String assetKey = null; // or other values
        if (b != null)
            assetKey = b.getString("assetKey");

        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
        ApiService apiService = retrofit.create(ApiService.class);


        Single<ChecklistRestBean> lista = apiService.getChecklist(assetKey, "checklist");

        lista.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<ChecklistRestBean>() {
            @Override
            public void onSubscribe(Disposable d) {
            //    d.dispose();
            }

            @Override
            public void onSuccess(ChecklistRestBean crb) {

           /*     List<Checklist> u = crb.getLista();

                for (Checklist c : u)
                    Log.d("aa", c.getDescription());*/

                if (crb.getAsset() == null) {

                    Toast.makeText(getApplicationContext(),
                            "Asset non trovato", Toast.LENGTH_LONG).show();

                    finish();
                } else {
                    Beep.play();


                    Intent intent = new Intent(CheckListActivity.this, ScrollingActivity.class);
                    Bundle b = new Bundle();
                    b.putSerializable("ChecklistRestBean", (Serializable) crb);
                    intent.putExtras(b);
                    startActivityForResult(intent, 1);
                }
            }


            @Override
            public void onError(Throwable e) {
                System.out.println("Error " + e);
                Log.d("11", "fail", e);
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
