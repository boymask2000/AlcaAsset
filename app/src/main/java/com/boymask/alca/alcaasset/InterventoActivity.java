package com.boymask.alca.alcaasset;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.boymask.alca.alcaasset.common.TimeUtil;
import com.boymask.alca.alcaasset.rest.ApiService;
import com.boymask.alca.alcaasset.rest.RetrofitInstance;
import com.boymask.alca.alcaasset.rest.beans.Asset;
import com.boymask.alca.alcaasset.rest.beans.InterventoRestBean;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class InterventoActivity extends Activity {

    private Asset asset;
    private int i;
  //  private int scelta = 0;
    private Button buttonScelta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intervento);
       buttonScelta = (Button) findViewById(R.id.buttonScelta);

        Bundle b = getIntent().getExtras();

        if (b != null)
            asset = (Asset) b.getSerializable("posbeu.alca.asset");

        getInterventi(asset.getId());
    }

    private void getInterventi(long id) {

        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
        ApiService apiService = retrofit.create(ApiService.class);


        Single<List<InterventoRestBean>> person = apiService.getInterventi(id, "interventi");

        person.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<InterventoRestBean>>() {
            @Override
            public void onSubscribe(Disposable d) {
                // we'll come back to this in a moment
            }

            @Override
            public void onSuccess(List<InterventoRestBean> lista) {
                InterventoRestBean intervento = lista.get(0);

                showInfo(intervento);
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("Error " + e);
                Log.d("11", "fail", e);
            }
        });
    }

    private void showInfo(final InterventoRestBean inter) {
        TextView dataPianificata = (TextView) findViewById(R.id.dataPianificata);
        dataPianificata.setText(TimeUtil.getFormattedDate(inter.getData_pianificata()));
  /*      int buttons[] = {R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9};
       int esitos[]={1,2,3,4,5,6,7,8,9};
       */

        Button b1 = (Button) findViewById(R.id.button1);
        ColorDrawable buttonColor = (ColorDrawable) b1.getBackground();
        buttonScelta.setBackground(buttonColor);
        inter.setEsito(1);

        setButtons(inter);
        //  final Scelta scelta = new Scelta();
 /*       for (i = 0; i < 9; i++) {
            final Button b = (Button) findViewById(buttons[i]);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ColorDrawable buttonColor = (ColorDrawable) b.getBackground();
                    buttonScelta.setBackground(buttonColor);

                 //   scelta = i + 1;
                    inter.setEsito(i+1);
                }
            });
        }*/

        Button esci = (Button) findViewById(R.id.esci);
        esci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button ok = (Button) findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
                ApiService apiService = retrofit.create(ApiService.class);

                apiService.updateIntervento( inter ).enqueue(new Callback<InterventoRestBean>() {
                    @Override
                    public void onResponse(Call<InterventoRestBean> call, Response<InterventoRestBean> response) {
Log.d("hh", "onresponse");
                        finish();
                    }

                    @Override
                    public void onFailure(Call<InterventoRestBean> call, Throwable t) {
                        Log.d("hh", "onfail");
                        finish();
                    }
                });
            }
        });
    }
    private void setButtons(final InterventoRestBean inter){
        final Button b1 = (Button) findViewById(R.id.button1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorDrawable buttonColor = (ColorDrawable) b1.getBackground();
                buttonScelta.setBackground(buttonColor);

                inter.setEsito(1);
            }
        });
        final Button b2 = (Button) findViewById(R.id.button2);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorDrawable buttonColor = (ColorDrawable) b2.getBackground();
                buttonScelta.setBackground(buttonColor);

                inter.setEsito(2);
            }
        });
        final Button b3 = (Button) findViewById(R.id.button3);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorDrawable buttonColor = (ColorDrawable) b3.getBackground();
                buttonScelta.setBackground(buttonColor);

                inter.setEsito(3);
            }
        });
        final Button b4 = (Button) findViewById(R.id.button4);
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorDrawable buttonColor = (ColorDrawable) b4.getBackground();
                buttonScelta.setBackground(buttonColor);

                inter.setEsito(4);
            }
        });
        final Button b5 = (Button) findViewById(R.id.button5);
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorDrawable buttonColor = (ColorDrawable) b5.getBackground();
                buttonScelta.setBackground(buttonColor);

                inter.setEsito(5);
            }
        });
        final Button b6 = (Button) findViewById(R.id.button6);
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorDrawable buttonColor = (ColorDrawable) b6.getBackground();
                buttonScelta.setBackground(buttonColor);

                inter.setEsito(6);
            }
        });
        final Button b7 = (Button) findViewById(R.id.button7);
        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorDrawable buttonColor = (ColorDrawable) b7.getBackground();
                buttonScelta.setBackground(buttonColor);

                inter.setEsito(7);
            }
        });
        final Button b8 = (Button) findViewById(R.id.button8);
        b8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorDrawable buttonColor = (ColorDrawable) b8.getBackground();
                buttonScelta.setBackground(buttonColor);

                inter.setEsito(8);
            }
        });
        final Button b9 = (Button) findViewById(R.id.button9);
        b9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorDrawable buttonColor = (ColorDrawable) b9.getBackground();
                buttonScelta.setBackground(buttonColor);

                inter.setEsito(9);
            }
        });
    }
}
