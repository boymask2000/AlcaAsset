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

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.boymask.alca.alcaasset.common.GlobalInfo;
import com.boymask.alca.alcaasset.common.Preferences;
import com.boymask.alca.alcaasset.common.TimeUtil;
import com.boymask.alca.alcaasset.common.Util;
import com.boymask.alca.alcaasset.rest.ApiService;
import com.boymask.alca.alcaasset.rest.RetrofitInstance;
import com.boymask.alca.alcaasset.rest.beans.Asset;
import com.boymask.alca.alcaasset.rest.beans.InterventiRealTimeHelper;
import com.boymask.alca.alcaasset.rest.beans.InterventoRestBean;

import org.json.JSONArray;
import org.json.JSONObject;

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

    private Button buttonScelta;
    private InterventoRestBean inter;
    private GlobalInfo info;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intervento);
        buttonScelta = (Button) findViewById(R.id.buttonScelta);
        TextView rmpie = (TextView) findViewById(R.id.rmpie);
        TextView fam = (TextView) findViewById(R.id.family);
        TextView subfam = (TextView) findViewById(R.id.subfamily);

        Bundle b = getIntent().getExtras();
        long interventoId = 0;
        if (b != null) {
            interventoId = b.getLong("alca.asset.interventoId");
            info = (GlobalInfo) b.getSerializable("info");
            rmpie.setText(info.getAsset().getRpieIdIndividual());
            fam.setText(info.getAsset().getFacSystem());
            subfam.setText(info.getAsset().getFacSubsystem());
        }

        getInterventi(interventoId);
    }

    private void getInterventi(long id) {

        Retrofit retrofit = RetrofitInstance.getRetrofitInstance(this);
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
                            "Nessun intevento previsto", Toast.LENGTH_LONG).show();
                    finish();
                    return;
                }

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
        this.inter = inter;
        inter.setUser(MainActivity.getUserName());
        TextView dataPianificata = (TextView) findViewById(R.id.dataPianificata);
        dataPianificata.setText(TimeUtil.getFormattedDate(inter.getData_pianificata()));

        Button b1 = (Button) findViewById(R.id.button1);
        ColorDrawable buttonColor = (ColorDrawable) b1.getBackground();
        buttonScelta.setBackground(buttonColor);
        inter.setEsito(1);

        setButtons();

        Button nuovoIntervento = (Button) findViewById(R.id.nuovoIntervento);
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
                inter.setEsito(0);
                info.getInterventoRestBean().setEsito(0);
                finish();
            }
        });
        setFoto(foto, inter);

        //    setAudio(audio,inter);

        Button ok = (Button) findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inter.setCommento(commento.getText().toString());

                if (inter.getEsito() != 1 && commento.getText().toString().trim().length() == 0) {
                    Util.showAlert(InterventoActivity.this, "Inserire un commento per l'esito non positivo");
                    return;
                }

                updateInterventoInServer(inter);
         //       InterventiRealTimeHelper.notificaFineIntervento(info, info.getInterventoRestBean(), InterventoActivity.this);

            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (info.getInterventoRestBean() != null)
            InterventiRealTimeHelper.notificaFineIntervento(info, info.getInterventoRestBean(), InterventoActivity.this);
    }
    private void updateInterventoInServer(InterventoRestBean inter) {
        //  AndroidNetworking.initialize(getApplicationContext());
        String baseUrl = Preferences.getBaseUrl(this);
        AndroidNetworking.post(baseUrl + "intervento/updateIntervento")
                .addApplicationJsonBody(inter) // posting java object
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Util.showMessage(InterventoActivity.this, "Dati inviati");
                        Toast.makeText(InterventoActivity.this, "Dati inviati", Toast.LENGTH_LONG).show();
                        finish();
                    }

                    @Override
                    public void onError(ANError error) {
                        error.printStackTrace();
                        Util.showAlert(InterventoActivity.this, "Dati non inviati [" + error.getMessage() + "]");
                        Toast.makeText(InterventoActivity.this, "Dati non inviati", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void updateInterventoInServer2(InterventoRestBean inter) {
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance(InterventoActivity.this);
        ApiService apiService = retrofit.create(ApiService.class);

        apiService.updateIntervento(inter).enqueue(new Callback<InterventoRestBean>() {
            @Override
            public void onResponse(Call<InterventoRestBean> call, Response<InterventoRestBean> response) {
                // Util.showMessage(InterventoActivity.this,"Dati inviati");
                //  Toast.makeText(InterventoActivity.this, "Dati inviati", Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onFailure(Call<InterventoRestBean> call, Throwable t) {
                t.printStackTrace();
                //   Util.showAlert(InterventoActivity.this,"Dati non inviati ["+t.getMessage()+"]");
                //  Toast.makeText(InterventoActivity.this, "Dati non inviati", Toast.LENGTH_LONG).show();

                finish();
            }
        });
    }

    private void setFoto(Button b, final InterventoRestBean inter) {
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(InterventoActivity.this, AndroidCameraApiActivity.class);
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

    private void setButtons() {
        final Button b1 = (Button) findViewById(R.id.button1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorDrawable buttonColor = (ColorDrawable) b1.getBackground();
                buttonScelta.setBackground(buttonColor);

                inter.setEsito(1);
                info.getInterventoRestBean().setEsito(inter.getEsito());
            }
        });
        final Button b2 = (Button) findViewById(R.id.button2);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorDrawable buttonColor = (ColorDrawable) b2.getBackground();
                buttonScelta.setBackground(buttonColor);

                inter.setEsito(2);
                info.getInterventoRestBean().setEsito(inter.getEsito());
            }
        });
        final Button b3 = (Button) findViewById(R.id.button3);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorDrawable buttonColor = (ColorDrawable) b3.getBackground();
                buttonScelta.setBackground(buttonColor);

                inter.setEsito(3);
                info.getInterventoRestBean().setEsito(inter.getEsito());
            }
        });
        final Button b4 = (Button) findViewById(R.id.button4);
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorDrawable buttonColor = (ColorDrawable) b4.getBackground();
                buttonScelta.setBackground(buttonColor);

                inter.setEsito(4);
                info.getInterventoRestBean().setEsito(inter.getEsito());
            }
        });
        final Button b5 = (Button) findViewById(R.id.button5);
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorDrawable buttonColor = (ColorDrawable) b5.getBackground();
                buttonScelta.setBackground(buttonColor);

                inter.setEsito(5);
                info.getInterventoRestBean().setEsito(inter.getEsito());
            }
        });
        final Button b6 = (Button) findViewById(R.id.button6);
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorDrawable buttonColor = (ColorDrawable) b6.getBackground();
                buttonScelta.setBackground(buttonColor);

                inter.setEsito(6);
                info.getInterventoRestBean().setEsito(inter.getEsito());
            }
        });
        final Button b7 = (Button) findViewById(R.id.button7);
        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorDrawable buttonColor = (ColorDrawable) b7.getBackground();
                buttonScelta.setBackground(buttonColor);

                inter.setEsito(7);
                info.getInterventoRestBean().setEsito(inter.getEsito());
            }
        });
        final Button b8 = (Button) findViewById(R.id.button8);
        b8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorDrawable buttonColor = (ColorDrawable) b8.getBackground();
                buttonScelta.setBackground(buttonColor);

                inter.setEsito(8);
                info.getInterventoRestBean().setEsito(inter.getEsito());
            }
        });
        final Button b9 = (Button) findViewById(R.id.button9);
        b9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorDrawable buttonColor = (ColorDrawable) b9.getBackground();
                buttonScelta.setBackground(buttonColor);

                inter.setEsito(9);
                info.getInterventoRestBean().setEsito(inter.getEsito());
            }
        });
    }
}
