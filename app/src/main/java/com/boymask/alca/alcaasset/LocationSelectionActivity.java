package com.boymask.alca.alcaasset;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.boymask.alca.alcaasset.common.Preferences;
import com.boymask.alca.alcaasset.rest.beans.Location;

import java.util.List;

public class LocationSelectionActivity extends Activity {

    private ListView listview;
    private Button ok;
    private Button cancel;
    private String selectedIp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_selection);

        getLocsFromServer();

        listview = (ListView) findViewById(R.id.list);
        ok = (Button) findViewById(R.id.ok);
        cancel = (Button) findViewById(R.id.cancel);

        setButtons();
    }

    private void setButtons() {
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(LocationSelectionActivity.this);
                SharedPreferences.Editor editor1 = settings.edit();
                editor1.putString("hostname", selectedIp);
                editor1.commit();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void getLocsFromServer() {

        String baseUrl = Preferences.getBaseUrl(this);

        AndroidNetworking.get(baseUrl + "location/getall")

                .setTag(this)
                //     .setPriority(Priority.LOW)
                .build()
                .getAsObjectList(Location.class, new ParsedRequestListener<List<Location>>() {
                    @Override
                    public void onResponse(List<Location> locs) {

                        final LocationSelectionActivity.CustomList adapter = new LocationSelectionActivity.CustomList(LocationSelectionActivity.this,
                                locs);
                        listview.setAdapter(adapter);
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("FF", anError.getErrorBody());
                    }
                });
    }

    private void setSelectedIp(String ip) {
        selectedIp = ip;
    }

    public class CustomList extends ArrayAdapter<Location> {
        private final LocationSelectionActivity context;

        private final List<Location> lista;

        public CustomList(LocationSelectionActivity context,
                          List<Location> lista) {
            super(context, R.layout.row_layout, lista);
            this.context = context;
            this.lista = lista;

        }

        int selectedPosition = 0;

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            View v = view;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                LayoutInflater v2 = context.getLayoutInflater();
                v = v2.inflate(R.layout.location_row_layout, null);
                //    RadioButton r = (RadioButton)v.findViewById(R.id.radiobutton);
            }

            TextView name = (TextView) v.findViewById(R.id.name);
            //   TextView descr = (TextView) v.findViewById(R.id.desc);


            name.setText("" + lista.get(position).getName());
            //    descr.setText(""+lista.get(position).getDescription());

            RadioButton r = (RadioButton) v.findViewById(R.id.radiobutton);
            r.setChecked(position == selectedPosition);
            r.setTag(position);
            r.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedPosition = (Integer) view.getTag();
                    notifyDataSetChanged();

                    Log.d("SEL", lista.get(selectedPosition).getName());
                    context.setSelectedIp(lista.get(selectedPosition).getIp());
                }
            });

            return v;
        }
    }


}
