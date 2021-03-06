package com.boymask.alca.alcaasset;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.boymask.alca.alcaasset.common.GlobalInfo;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

public class ScannerActivity extends Activity {

    private CodeScanner mCodeScanner;
    private GlobalInfo info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_scanner);

        Bundle b = getIntent().getExtras();
        info= (GlobalInfo) b.getSerializable("info");

        final EditText textCodice = findViewById(R.id.codiceAsset);
        final TextView full = findViewById(R.id.full);

        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                ScannerActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String value = result.getText();
                        textCodice.setText(getFirst(value));
                        full.setText(value);
                        startNext(value);
                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });


        Button ok = (Button) findViewById(R.id.button);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("SCANNER", "CHIAMO SCANNER");
                String value = textCodice.getText().toString();
                if (value.equals("0")) value = "8436010153238";
                if (value.trim().length() > 0) {
                    Toast.makeText(getApplicationContext(),
                            "Redirecting...", Toast.LENGTH_SHORT).show();

                    startNext(value);
                }
            }
        });
    }

    private String getFirst(String value) {
        int index = value.indexOf('\n');
        if (index != -1) return value.substring(0, index);
        index = value.indexOf(' ');
        if (index != -1) return value.substring(0, index);
        return value;

    }

    private void startNext(String value) {
        Toast.makeText(ScannerActivity.this, value, Toast.LENGTH_SHORT).show();
        final EditText textCodice = findViewById(R.id.codiceAsset);
        textCodice.setText("");
        Intent intent = new Intent(ScannerActivity.this, CheckListSafetyActivity.class);
        Bundle b = new Bundle();
        b.putString("assetKey", value);
     //   intent.putExtras(b);

        b.putSerializable("info", info);
        intent.putExtras(b);
        startActivityForResult(intent,1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 1) {
            finish();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
}
