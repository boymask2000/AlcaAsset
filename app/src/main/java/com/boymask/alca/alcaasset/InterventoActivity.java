package com.boymask.alca.alcaasset;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.boymask.alca.alcaasset.common.TimeUtil;
import com.boymask.alca.alcaasset.rest.ApiService;
import com.boymask.alca.alcaasset.rest.RetrofitInstance;
import com.boymask.alca.alcaasset.rest.beans.Asset;
import com.boymask.alca.alcaasset.rest.beans.InterventoRestBean;

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
    private Button buttonScelta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intervento);
        buttonScelta = (Button) findViewById(R.id.buttonScelta);

        Bundle b = getIntent().getExtras();
        long interventoId=0;
        if (b != null)
            interventoId =  b.getLong("alca.asset.interventoId");

        getInterventi(interventoId);
    }

    private void getInterventi(long id) {

        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
        ApiService apiService = retrofit.create(ApiService.class);


        Single<InterventoRestBean> person = apiService.getIntervento(id, "interventi");

        person.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<InterventoRestBean>() {
            @Override
            public void onSubscribe(Disposable d) {
                // we'll come back to this in a moment
            }

            @Override
            public void onSuccess(InterventoRestBean interventoRestBean) {
                if (interventoRestBean == null) {
                    Toast.makeText(getApplicationContext(),
                            "Nessun intevento previsto", Toast.LENGTH_LONG).show();finish();
                    return;
                }

              //  InterventoRestBean intervento = lista.get(0);

                showInfo(interventoRestBean);
            }



            @Override
            public void onError(Throwable e) {
                System.out.println("Error " + e);
                Log.d("11", "fail", e);
            }
        });
    }

    private void showInfo(final InterventoRestBean inter) {
        inter.setUser(MainActivity.getUserName());
        TextView dataPianificata = (TextView) findViewById(R.id.dataPianificata);
        dataPianificata.setText(TimeUtil.getFormattedDate(inter.getData_pianificata()));
 
        Button b1 = (Button) findViewById(R.id.button1);
        ColorDrawable buttonColor = (ColorDrawable) b1.getBackground();
        buttonScelta.setBackground(buttonColor);
        inter.setEsito(1);

        setButtons(inter);
        
        Button nuovoIntervento =  (Button) findViewById(R.id.nuovoIntervento);
        nuovoIntervento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nuovoIntervento(inter);
            }
        });
      //  Button audio = (Button) findViewById(R.id.audio);
        final EditText commento = (EditText) findViewById(R.id.commento);
        Button foto = (Button) findViewById(R.id.photo);
        Button esci = (Button) findViewById(R.id.esci);
        esci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setFoto(foto,inter);

    //    setAudio(audio,inter);

        Button ok = (Button) findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inter.setCommento(commento.getText().toString());
                Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
                ApiService apiService = retrofit.create(ApiService.class);

                apiService.updateIntervento(inter).enqueue(new Callback<InterventoRestBean>() {
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

    private void setCommento(Button b, InterventoRestBean inter) {

    }

    private void setFoto(Button b, final InterventoRestBean inter) {
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent intent = new Intent(InterventoActivity.this, CameraActivity.class);
                Intent intent = new Intent(InterventoActivity.this, AndroidCameraApiActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("InterventoRestBean", inter);
                intent.putExtras(b);
                startActivity(intent);

            }
        });
    }
    private void setAudio(Button b, final InterventoRestBean inter) {
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InterventoActivity.this, AudioRecordActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("InterventoRestBean", inter);
                intent.putExtras(b);
                startActivity(intent);

            }
        });
    }

    private void nuovoIntervento(InterventoRestBean inter) {
        Intent intent = new Intent(InterventoActivity.this, RichiestaNuovoInterventoActivity.class);
        Bundle b = new Bundle();
        b.putSerializable("interventoRestBean", inter);
        intent.putExtras(b);
        startActivity(intent);
    }

    private void setButtons(final InterventoRestBean inter) {
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
