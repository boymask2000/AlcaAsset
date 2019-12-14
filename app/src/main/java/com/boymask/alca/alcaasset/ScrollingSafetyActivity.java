package com.boymask.alca.alcaasset;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.boymask.alca.alcaasset.common.GlobalInfo;
import com.boymask.alca.alcaasset.common.Messaggio;
import com.boymask.alca.alcaasset.common.MsgType;
import com.boymask.alca.alcaasset.common.Preferences;
import com.boymask.alca.alcaasset.rest.beans.ChecklistIntervento;
import com.boymask.alca.alcaasset.rest.beans.InterventoRestBean;
import com.boymask.alca.alcaasset.rest.beans.SafetyChecklistRestBean;
import com.boymask.alca.alcaasset.rest.beans.SafetyRestBean;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ScrollingSafetyActivity extends Activity {
//    private RecyclerView recyclerView;
//    private RecyclerView.Adapter mAdapter;
//    private RecyclerView.LayoutManager layoutManager;

    private Map<Integer, Boolean> checkMap = new HashMap<>();
    private Button ok;
    private Button cancel;
    private ListView listview;

    private TextView rpid;
    private String assetKey;
    private String family;
    private List<String> notCompliantSafety = new ArrayList<>();
    private String activityResult;
    private GlobalInfo info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safety_scrolling);

        listview = (ListView) findViewById(R.id.list);

        rpid = (TextView) findViewById(R.id.rmpie);
        TextView fam = (TextView) findViewById(R.id.family);
        TextView subfam = (TextView) findViewById(R.id.subfamily);
        ok = (Button) findViewById(R.id.ok);
        cancel = (Button) findViewById(R.id.cancel);

        Bundle b = getIntent().getExtras();
        List<ChecklistIntervento> lista = null;
        InterventoRestBean irb = null;
        SafetyChecklistRestBean crb = (SafetyChecklistRestBean) b.getSerializable("SafetyChecklistRestBean");
        assetKey = b.getString("rfid");
        info = (GlobalInfo) b.getSerializable("info");
        rpid.setText(assetKey);
        family = crb.getFamily();

        fam.setText(info.getAsset().getFacSystem());
    //    subfam.setText(info.getAsset().getFacSubsystem());
        next(crb.getLista());


    }

    private void next(List<SafetyRestBean> checks) {


        if (checks.size() == 0) {
//            Toast.makeText(getApplicationContext(),
//                    "Nessun intevento previsto", Toast.LENGTH_LONG).show();
            goNext();
            return;
        }

        buildCheckMap(checks.size());

        final CustomList adapter = new CustomList(this,
                checks);

        //  final ListViewItemCheckboxBaseAdapter listViewDataAdapter = new ListViewItemCheckboxBaseAdapter(getApplicationContext(), initItemList);

        listview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        //    updateOk(null);

/*        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                b.putSerializable("info", info);
                intent.putExtras(b);
                startActivityForResult(intent, 2);
            }
        });
    }

    private void execCancel() {

        Messaggio msg = new Messaggio();
        msg.setUsername(info.getUser().getUsername());

        msg.setMsgType(MsgType.WARNING);

        String msgText = "Eseguito CANCEL su checklist sicurezza per intervento su asset " + assetKey;
        String compTxt = getTextFromNotCompList();
        if (compTxt.length() > 0)
            msgText += ": " + compTxt;

        String ms = "CANCEL_ON_SECUR_CKLIST|"+assetKey+"|"+compTxt;

        msg.setMsgCode("CANCEL_ON_SECUR_CKLIST");
//        msg.addParameter(assetKey);
//        msg.addParameter(compTxt);
        msg.setText(ms);


        notifyCancelInServer(msg);

        termina();
    }

    private String getTextFromNotCompList() {
        String out = "";
        for (String s : notCompliantSafety) {
            if (out.length() > 0) out += ", ";
            out += s;
        }
        return out;
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
                        termina();
                    }
                });
    }

    private void goNext() {
        Intent intent = new Intent(ScrollingSafetyActivity.this, CheckListActivity.class);
        Bundle b = new Bundle();
        b.putString("assetKey", assetKey);
        b.putString("family", family);
        b.putSerializable("info", info);
        intent.putExtras(b);
        startActivityForResult(intent, 1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 1) {
            termina();
        }
        if (requestCode == 2) goNext();
    }

    private void termina() {
        Intent intent = getIntent();
        intent.putExtra("key", activityResult);
        setResult(1, intent);

        finish();
    }


    private void updateOk(List<SafetyRestBean> lista) {
        notCompliantSafety.clear();
        boolean out = true;
        Set<Map.Entry<Integer, Boolean>> entryset = checkMap.entrySet();

        if (lista != null) {
            Iterator<Map.Entry<Integer, Boolean>> iter1 = entryset.iterator();
            while (iter1.hasNext()) {
                Map.Entry<Integer, Boolean> e = iter1.next();
                if (!e.getValue()) {
                    Integer key = e.getKey();
                    String msg = lista.get(key).getRisk_en() + " - " + lista.get(key).getPpe_en();
                    notCompliantSafety.add(msg);
                }
            }
        }


        Iterator<Map.Entry<Integer, Boolean>> iter = entryset.iterator();
        while (iter.hasNext()) {
            Map.Entry<Integer, Boolean> e = iter.next();
            if (!e.getValue()) {
                out = false;
                break;
            }
            Integer key = e.getKey();


        }
        if (out) activityResult = "";
        else activityResult = "SEC";
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

            updateOk(lista);

        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
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

            final CheckBox checkYes = (CheckBox) rowView.findViewById(R.id.checkbox_yes);
            final CheckBox checkNo = (CheckBox) rowView.findViewById(R.id.checkbox_no);


            checkYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        checkNo.setChecked(false);
                        checkMap.put(position, true);

                    } else {
                        checkNo.setChecked(true);
                        checkMap.put(position, false);
                    }
                    updateOk(lista);
                }
            });
            checkNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        checkYes.setChecked(false);
                        checkMap.put(position, false);
                    } else {
                        checkYes.setChecked(true);
                        checkMap.put(position, true);
                    }
                    updateOk(lista);
                }
            });


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
