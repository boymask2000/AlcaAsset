package com.boymask.alca.alcaasset;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.boymask.alca.alcaasset.common.TimeUtil;
import com.boymask.alca.alcaasset.rest.ApiService;
import com.boymask.alca.alcaasset.rest.RetrofitInstance;
import com.boymask.alca.alcaasset.rest.beans.InterventoRestBean;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RichiestaNuovoInterventoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_richiesta_nuovo_intervento);

        Bundle b = getIntent().getExtras();
        InterventoRestBean irb = null;
        if (b != null)
            irb = (InterventoRestBean) b.getSerializable("interventoRestBean");

        final Button ok = (Button) findViewById(R.id.ok);


        final Button cancel = (Button) findViewById(R.id.cancel);

        final EditText data = (EditText) findViewById(R.id.dataintervento);
        setOk(ok, irb, data);
        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

            private void updateLabel() {
                String myFormat = "yyyyMMdd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ITALY);

                data.setText(sdf.format(myCalendar.getTime()));
            }
        };

        data.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(RichiestaNuovoInterventoActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    private void setOk(Button ok, final InterventoRestBean irb, final EditText data) {
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(),
                        "Redirecting...", Toast.LENGTH_SHORT).show();

                irb.setData_pianificata(data.getText().toString());
                irb.setData_effettiva("");
                irb.setData_teorica("");

                Retrofit retrofit = RetrofitInstance.getRetrofitInstance(RichiestaNuovoInterventoActivity.this);
                ApiService apiService = retrofit.create(ApiService.class);

                apiService.creaIntervento(irb).enqueue(new Callback<InterventoRestBean>() {
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
}
