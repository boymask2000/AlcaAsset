package com.boymask.alca.alcaasset;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.boymask.alca.alcaasset.common.Global;
import com.boymask.alca.alcaasset.common.Messaggio;
import com.boymask.alca.alcaasset.common.MsgType;
import com.boymask.alca.alcaasset.common.Preferences;
import com.boymask.alca.alcaasset.rest.beans.ChecklistIntervento;
import com.boymask.alca.alcaasset.rest.beans.InterventoRestBean;
import com.boymask.alca.alcaasset.rest.beans.SafetyChecklistRestBean;
import com.boymask.alca.alcaasset.rest.beans.SafetyRestBean;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ScrollingSafetyActivity extends Activity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private Map<Integer, Boolean> checkMap = new HashMap<>();
    private Button ok;
    private Button cancel;
    private ListView listview;
    private TextView assetDesc;
    private TextView rpid;
    private String assetKey;
    private String family;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safety_scrolling);

        listview = (ListView) findViewById(R.id.list);
        assetDesc = (TextView) findViewById(R.id.assetDesc);
        rpid = (TextView) findViewById(R.id.rpid);
        ok = (Button) findViewById(R.id.ok);
        cancel = (Button) findViewById(R.id.cancel);

        Bundle b = getIntent().getExtras();
        List<ChecklistIntervento> lista = null;
        InterventoRestBean irb = null;
        SafetyChecklistRestBean crb = (SafetyChecklistRestBean) b.getSerializable("SafetyChecklistRestBean");
        assetKey = b.getString("rfid");
        rpid.setText(assetKey);
        family = crb.getFamily();
        next(crb.getLista());


    }

    private void next(List<SafetyRestBean> checks) {


        if (checks.size() == 0) {
//            Toast.makeText(getApplicationContext(),
//                    "Nessun intevento previsto", Toast.LENGTH_LONG).show();
            goNext();
            return;
        }


        //    assetDesc.setText(asset.getNomenclature());

        buildCheckMap(checks.size());

        final CustomList adapter = new CustomList(this,
                checks);

        //  final ListViewItemCheckboxBaseAdapter listViewDataAdapter = new ListViewItemCheckboxBaseAdapter(getApplicationContext(), initItemList);

        listview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        updateOk();
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long l) {
                // Get user selected item.
                Object itemObject = adapterView.getAdapter().getItem(itemIndex);


                // Get the checkbox.
                CheckBox itemCheckbox = (CheckBox) view.findViewById(R.id.checkbox);

                // Reverse the checkbox and clicked item check state.
                if (itemCheckbox.isChecked()) {
                    checkMap.put(itemIndex, false);
                    itemCheckbox.setChecked(false);
                    updateOk();
                    //    Log.d("dd", "no");
                } else {
                    checkMap.put(itemIndex, true);
                    itemCheckbox.setChecked(true);
                    updateOk();
                    //   Log.d("dd", "si");
                }


                //Toast.makeText(getApplicationContext(), "select item text : " + itemDto.getItemText(), Toast.LENGTH_SHORT).show();
            }
        });

/*        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goNext();
            }
        });*/
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                execCancel();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScrollingSafetyActivity.this, ViewSchedaFamigliaActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("assetRMPID", assetKey);
                intent.putExtras(b);
                startActivityForResult(intent, 2);
            }
        });
    }

    private void execCancel() {
        Messaggio msg = new Messaggio();
        msg.setUsername(Global.getUser().getUsername());
        msg.setText("Eseguito CANCEL su checklist sicurezza per intervento su asset " + assetKey);
        msg.setMsgType(MsgType.WARNING);

        notifyCancelInServer(msg);

        finish();
    }

    private void notifyCancelInServer(Messaggio msg) {

        String baseUrl = Preferences.getBaseUrl(this);
        AndroidNetworking.post(baseUrl + "intervento/cancelOnSafety")
                .addApplicationJsonBody(msg) // posting java object
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        finish();
                    }

                    @Override
                    public void onError(ANError error) {
                        error.printStackTrace();
                        finish();
                    }
                });
    }

    private void goNext() {
        Intent intent = new Intent(ScrollingSafetyActivity.this, CheckListActivity.class);
        Bundle b = new Bundle();
        b.putString("assetKey", assetKey);
        b.putString("family", family);
        intent.putExtras(b);
        startActivityForResult(intent, 1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 1) {
            finish();
        }
        if (requestCode == 2) goNext();
    }


    private void updateOk() {
        boolean out = true;
        Set<Map.Entry<Integer, Boolean>> entryset = checkMap.entrySet();
        Iterator<Map.Entry<Integer, Boolean>> iter = entryset.iterator();
        while (iter.hasNext()) {
            Map.Entry<Integer, Boolean> e = iter.next();
            if (!e.getValue()) {
                out = false;
                break;
            }

        }
        ok.setEnabled(out);
    }

    private void buildCheckMap(int size) {
        for (int i = 0; i < size; i++)
            checkMap.put(i, false);
    }

    public class CustomList extends ArrayAdapter<SafetyRestBean> {
        private final Activity context;

        private final List<SafetyRestBean> lista;

        public CustomList(Activity context,
                          List<SafetyRestBean> lista) {
            super(context, R.layout.row_layout, lista);
            this.context = context;
            this.lista = lista;

        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.safety_row_layout, null, true);
            TextView riskTitle = (TextView) rowView.findViewById(R.id.risk);
            TextView ppeTitle = (TextView) rowView.findViewById(R.id.ppe);
            ImageView imageV = (ImageView) rowView.findViewById(R.id.imageicon);

            riskTitle.setText("" + lista.get(position).getRisk_en());
            ppeTitle.setText("" + lista.get(position).getPpe_en());
            int resourceId = getResource(position);
            if (resourceId > 0)
                imageV.setImageResource(resourceId);

            return rowView;
        }

        private int getResource(int position) {
            // int id = getResources().getIdentifier("com.boymask.alca.alcaasset:drawable/ppe_" + position, null, null);
            int imgId = lista.get(position).getImgId();

            int id = getResources().getIdentifier("ppe_" + imgId, "drawable",
                    context.getPackageName());

            return id;
        }


    }
}
