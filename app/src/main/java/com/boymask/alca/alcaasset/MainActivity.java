package com.boymask.alca.alcaasset;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.boymask.alca.alcaasset.rest.ApiService;
import com.boymask.alca.alcaasset.rest.RetrofitInstance;
import com.boymask.alca.alcaasset.rest.beans.Utente;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    private Button b1, b2;
    private EditText userText, passText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initRetrofit();

        b1 = (Button) findViewById(R.id.button);
        userText = (EditText) findViewById(R.id.editText);
        passText = (EditText) findViewById(R.id.editText2);

        b2 = (Button) findViewById(R.id.button2);


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(),
                        "Redirecting...", Toast.LENGTH_SHORT).show();

                // create an instance of the ApiService
                Retrofit retrofit;
                retrofit = RetrofitInstance.getRetrofitInstance();
                ApiService apiService = retrofit.create(ApiService.class);

                String userName = userText.getText().toString();
                String password = passText.getText().toString();

                if (userName.equalsIgnoreCase("pippo")) {
                    Intent intent = new Intent(MainActivity.this, ScannerActivity.class);
                    startActivity(intent);
                } else {
// make a request by calling the corresponding method
                    Single<Utente> person = apiService.login(userName, password, "login");
                    person.subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<Utente>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            //     d.dispose();
                        }

                        @Override
                        public void onSuccess(Utente u) {

                            if (u.getUsername() != null) {
                                Intent intent = new Intent(MainActivity.this, ScannerActivity.class);
                                startActivity(intent);

                            } else
                                Toast.makeText(getApplicationContext(),
                                        "Credenzali errate", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onError(Throwable e) {
                            System.out.println("Error " + e);
                            Log.d("11", "fail", e);
                        }
                    });
                }
            }
        });

     /*   b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AssetPreferencesActivity.class);
                startActivity(i);
            }
        });*/
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.optionsmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.about:

                return true;
            case R.id.setServer:
                Intent i = new Intent(MainActivity.this, AssetPreferencesActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private int MY_CAMERA_REQUEST_CODE = 100;

    private void initRetrofit() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.INTERNET}, MY_CAMERA_REQUEST_CODE);
            Toast.makeText(getApplicationContext(),
                    "No Internet", Toast.LENGTH_LONG).show();
            return;
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_CAMERA_REQUEST_CODE);
            Toast.makeText(getApplicationContext(),
                    "No WRITE_EXTERNAL_STORAGE", Toast.LENGTH_LONG).show();
            return;
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
            Toast.makeText(getApplicationContext(),
                    "No Camera", Toast.LENGTH_LONG).show();
            return;
        } else
            Toast.makeText(getApplicationContext(),
                    "Camera OK", Toast.LENGTH_LONG).show();


    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_CAMERA_REQUEST_CODE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();

            } else {

                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();

            }

        }
    }
}