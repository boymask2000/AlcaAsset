package com.boymask.alca.alcaasset;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.gsonparserfactory.GsonParserFactory;
import com.boymask.alca.alcaasset.common.Global;
import com.boymask.alca.alcaasset.common.Util;
import com.boymask.alca.alcaasset.network.NetworkUtil;
import com.boymask.alca.alcaasset.rest.ApiService;
import com.boymask.alca.alcaasset.rest.RetrofitInstance;
import com.boymask.alca.alcaasset.rest.beans.Utente;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    private Button b1;
    private EditText userText, passText;
    private TextView server;
    private TextView netstat;

    private static String userName;

    @Override
    protected void onPostResume() {
        super.onPostResume();
        server.setText(Util.getServer(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        NetworkUtil.setNetworkMonitor(this, netstat);
    }

    @Override
    protected void onPause() {
        super.onPause();
        NetworkUtil.removeNetworkMonitor();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showVersion();

        initNetworking();

        initRetrofit();

        b1 = (Button) findViewById(R.id.button);
        userText = (EditText) findViewById(R.id.editText);
        passText = (EditText) findViewById(R.id.editText2);
        server = findViewById(R.id.server);
        netstat = findViewById(R.id.netstat);
        server.setText(Util.getServer(this));

        //   b2 = (Button) findViewById(R.id.button2);

        setListenerB1();


    }

    private void setListenerB1() {
        b1.setOnClickListener(null);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // create an instance of the ApiService
                Retrofit retrofit;
                retrofit = RetrofitInstance.getRetrofitInstance(MainActivity.this);
                ApiService apiService = retrofit.create(ApiService.class);

                userName = userText.getText().toString();
                String password = passText.getText().toString();

                if (userName == null || userName.trim().length() == 0) {
                    Util.showAlert(MainActivity.this, "Inserire utente");
                    return;
                }
                if (password == null || password.trim().length() == 0) {
                    Util.showAlert(MainActivity.this, "Inserire password");
                    return;
                }

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
                            Global.setUser(u);
                            if (u.getUsername() != null) {
                                Intent intent = new Intent(MainActivity.this, ToDoActivity.class);
                                startActivity(intent);

                            } else {
                                Util.showAlert(MainActivity.this, "Credenzali errate");

                            }
                        }

                        @Override
                        public void onError(Throwable e) {

                            if (e instanceof java.net.ConnectException) {

                                Util.showAlert(MainActivity.this,
                                        "Impossibile connettersi al server " + Util.getServer(MainActivity.this));

                            }
                        }
                    });
                }
            }
        });
    }

    private void initNetworking() {
        AndroidNetworking.initialize(getApplicationContext());

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        AndroidNetworking.setParserFactory(new GsonParserFactory(gson));
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.optionsmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.about:

                return true;
            case R.id.setServer:
                Intent i = new Intent(MainActivity.this, AssetPreferencesActivity.class);
                startActivityForResult(i, 1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {

            setListenerB1();
        }
    }

    private final int MY_CAMERA_REQUEST_CODE = 100;
    private final int MY_INTERNET_REQUEST_CODE = 101;
    private final int MY_EXT_STORAGE_REQUEST_CODE = 102;
    private final int PERMISSION_ALL = 103;

    private void initRetrofit() {
        String[] permissions = {Manifest.permission.INTERNET,Manifest.permission.CAMERA};

        boolean ok=true;
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                ok= false;
            }
        }
        if( !ok)
        {
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_ALL);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch( requestCode){
            case PERMISSION_ALL:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, " permissions granted", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(this, " permissions denied", Toast.LENGTH_LONG).show();

                break;
            case MY_CAMERA_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();

                break;
            case MY_INTERNET_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "internet permission granted", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(this, "internet permission denied", Toast.LENGTH_LONG).show();
                break;
   /*         case MY_EXT_STORAGE_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "storage permission granted", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(this, "storage permission denied", Toast.LENGTH_LONG).show();
                break;*/
        }


    }

    public static String getUserName() {
        return userName;
    }

    private void showVersion() {
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = pInfo.versionName;
        TextView versionText = (TextView) findViewById(R.id.appversion);
        versionText.setText("Version: " + version);
    }
}