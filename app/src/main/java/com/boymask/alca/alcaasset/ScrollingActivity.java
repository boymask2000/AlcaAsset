package com.boymask.alca.alcaasset;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.boymask.alca.alcaasset.rest.beans.ChecklistIntervento;
import com.boymask.alca.alcaasset.rest.beans.ChecklistRestBean;
import com.boymask.alca.alcaasset.rest.beans.InterventoRestBean;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ScrollingActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private Map<Integer, Boolean> checkMap = new HashMap<>();
    private Button ok;
    ListView listview;
    TextView assetDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        listview = (ListView) findViewById(R.id.list);
        assetDesc = (TextView) findViewById(R.id.assetDesc);
        ok = (Button) findViewById(R.id.button);

        Bundle b = getIntent().getExtras();
        List<ChecklistIntervento> lista = null;
        InterventoRestBean irb = null;
        ChecklistRestBean crb = (ChecklistRestBean) b.getSerializable("ChecklistRestBean");

        next(crb.getLista(),crb.getInterventoId());



    }

    private void next(List<ChecklistIntervento> checks, final long id) {


        if (checks.size() == 0) {
            Toast.makeText(getApplicationContext(),
                    "Nessun intevento previsto", Toast.LENGTH_LONG).show();
            finish();
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
                    Log.d("dd", "no");
                } else {
                    checkMap.put(itemIndex, true);
                    itemCheckbox.setChecked(true);
                    updateOk();
                    Log.d("dd", "si");
                }


                //Toast.makeText(getApplicationContext(), "select item text : " + itemDto.getItemText(), Toast.LENGTH_SHORT).show();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScrollingActivity.this, InterventoActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("alca.asset.interventoId", id);
                intent.putExtras(b);
                startActivityForResult(intent, 1);
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

    public class CustomList extends ArrayAdapter<ChecklistIntervento> {
        private final Activity context;

        private final List<ChecklistIntervento> lista;

        public CustomList(Activity context,
                          List<ChecklistIntervento> lista) {
            super(context, R.layout.row_layout, lista);
            this.context = context;
            this.lista = lista;

        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.row_layout, null, true);
            TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);

            txtTitle.setText(""+lista.get(position).getId());

            return rowView;
        }


    }
}
