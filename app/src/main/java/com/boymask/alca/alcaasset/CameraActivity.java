package com.boymask.alca.alcaasset;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.boymask.alca.alcaasset.common.Beep;
import com.boymask.alca.alcaasset.rest.ApiService;
import com.boymask.alca.alcaasset.rest.RetrofitInstance;
import com.boymask.alca.alcaasset.rest.beans.Asset;
import com.boymask.alca.alcaasset.rest.beans.InterventoRestBean;
import com.boymask.alca.alcaasset.rest.beans.Utente;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

public class CameraActivity extends Activity {
    private Camera mCamera;
    private CameraPreview mPreview;
    private long interventoId=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);


        Bundle b = getIntent().getExtras();
        InterventoRestBean interventoRestBean;
        if (b != null) {
            interventoRestBean = (InterventoRestBean) b.getSerializable("InterventoRestBean");
            interventoId = interventoRestBean.getId();
            Log.d("p", "interventoId= "+interventoId);
        }

        if (!checkCameraHardware(this)) finish();

        mCamera = getCameraInstance();

        if (mCamera == null) {
            Toast.makeText(getApplicationContext(),
                    "Impossibile accedere alla camera", Toast.LENGTH_LONG).show();
            finish();
        }

        final Camera.PictureCallback picture = new Camera.PictureCallback() {

            @Override
            public void onPictureTaken(byte[] data, Camera camera) {

                File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
                if (pictureFile == null) {
                    Log.d("p", "Error creating media file, check storage permissions");
                    return;
                }

                try {
                    FileOutputStream fos = new FileOutputStream(pictureFile);
                    fos.write(data);
                    fos.close();
                } catch (FileNotFoundException e) {
                    Log.d("p", "File not found: " + e.getMessage());
                } catch (IOException e) {
                    Log.d("p", "Error accessing file: " + e.getMessage());
                }
                Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
                ApiService apiService = retrofit.create(ApiService.class);
                // File file = new File("pp");

                MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", pictureFile.getName(), RequestBody.create(MediaType.parse("image/*"), pictureFile));

                Call<Utente> call = apiService.uploadFile(filePart,interventoId);
                call.enqueue(new Callback<Utente>() {


                    @Override
                    public void onResponse(Call<Utente> call, retrofit2.Response<Utente> response) {
                        Toast.makeText(CameraActivity.this, "" + "Ok", Toast.LENGTH_LONG).show();
                        Log.d("d", "response");
                        finish();
                    }

                    @Override
                    public void onFailure(Call<Utente> call, Throwable t) {
                        Toast.makeText(CameraActivity.this, "" + "Ok", Toast.LENGTH_LONG).show();
                        Log.d("d", "fail");
                        t.printStackTrace();
                        finish();
                    }
                });
            }
        };

// Add a listener to the Capture button
        final Button captureButton = (Button) findViewById(R.id.button_capture);
        captureButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // get an image from the camera
                        try {
                            mCamera.takePicture(null, null, picture);
                            captureButton.setEnabled(false);
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                        Beep.playCameraClick(CameraActivity.this);
                    }
                }
        );


// Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);


    }

    /**
     * Check if this device has a camera
     */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    /**
     * A safe way to get an instance of the Camera object.
     */
    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    @Override
    protected void onPause() {
        super.onPause();

        releaseCamera();              // release the camera immediately on pause event
    }


    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }

    /**
     * Create a File for saving an image or video
     */
    private static File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

}
