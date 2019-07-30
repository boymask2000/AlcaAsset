package com.boymask.alca.alcaasset;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.boymask.alca.alcaasset.common.Global;
import com.boymask.alca.alcaasset.common.TimeUtil;
import com.boymask.alca.alcaasset.rest.ApiService;
import com.boymask.alca.alcaasset.rest.RetrofitInstance;
import com.boymask.alca.alcaasset.rest.beans.Asset;
import com.boymask.alca.alcaasset.rest.beans.ChecklistIntervento;
import com.boymask.alca.alcaasset.rest.beans.ChecklistRestBean;
import com.boymask.alca.alcaasset.rest.beans.SafetyRestBean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class ToDoActivity extends AppCompatActivity {

    private Button ok;
    private EditText dataintervento;
    private TextView numprev;
    private ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);

        ok = (Button) findViewById(R.id.ok);
        dataintervento = (EditText) findViewById(R.id.dataintervento);
        numprev = (TextView) findViewById(R.id.numprev);
        listview = (ListView) findViewById(R.id.list);

        setOk();
        setDataintervento();
    }

    private void setDataintervento() {
        dataintervento.setText(TimeUtil.getLocalizedData());
        refresh(TimeUtil.getCanonicData());

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

                dataintervento.setText(TimeUtil.getLocalizedData(myCalendar.getTime()));
                refresh(TimeUtil.getCanonicData(myCalendar.getTime()));
            }
        };

        dataintervento.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(ToDoActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void refresh(String canonicData) {

        getPreviousInte(canonicData);

        getPreviousAssets(canonicData);


    }
    private void getPreviousAssets(String canonicData) {
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance(this);
        ApiService apiService = retrofit.create(ApiService.class);
        final Single<List<Asset>> lista = apiService.getPreviousInterventiAssets(canonicData, "checklist");

        lista.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<Asset>>() {


            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(List<Asset> as) {
                Log.d("LL", ""+as.size());
                final ToDoActivity.CustomList adapter = new ToDoActivity.CustomList(ToDoActivity.this,
                        as);
                listview.setAdapter(adapter);
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    private void getPreviousInte(String canonicData) {
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance(this);
        ApiService apiService = retrofit.create(ApiService.class);
        Single<Integer> sing = apiService.getPreviousInterventi(canonicData, "checklist");

        sing.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<Integer>() {


            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Integer prev) {
                SpannableString spanString = new SpannableString("" + prev);
                //    spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);
                spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
                //    spanString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spanString.length(), 0);
                //   text.setText(spanString);

                numprev.setText(spanString);

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

    private void setOk() {

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ToDoActivity.this, ScannerActivity.class);
                startActivity(intent);
            }
        });
    }
    public class CustomList extends ArrayAdapter<Asset> {
        private final Activity context;

        private final List<Asset> lista;

        public CustomList(Activity context,
                          List<Asset> lista) {
            super(context, R.layout.row_layout, lista);
            this.context = context;
            this.lista = lista;

        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.todo_row_layout, null, true);
            TextView idTitle = (TextView) rowView.findViewById(R.id.id);
            TextView familyTitle = (TextView) rowView.findViewById(R.id.family);

            idTitle.setText(""+lista.get(position).getRpieIdIndividual());
            familyTitle.setText(""+lista.get(position).getFacSubsystem());

            return rowView;
        }


    }
}