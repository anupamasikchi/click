package com.click.rollingstone.click2;

import android.annotation.TargetApi;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.media.ImageReader;

import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.SurfaceView;

import java.util.LinkedList;
import java.util.List;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)

public class MainActivity extends ActionBarActivity {
    String cameraId;
    CameraDevice cameraDevice;
    Handler backgroundHandler;
    ImageReader imageReader;
    CameraCaptureSession cameraCaptureSession;
    DevicePolicyManager mDPM;
    ComponentName mAdminName;
    //SurfaceView sv = (SurfaceView)findViewById(R.id.surfaceView1); //add this to your xml view
    //;
    //public SurfaceView getSurfaceView(){
    //    return sv;
    //}


Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
    //SurfaceView sv=(SurfaceView)findViewById(R.id.surfaceView1);
        mDPM = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
        mAdminName = new ComponentName(getApplicationContext(),Click2AdminReceiver.class);

        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,mAdminName);

        startActivity(intent);

        /*HandlerThread thread = new HandlerThread("CameraPreview");
        thread.start();
        backgroundHandler = new Handler(thread.getLooper());
        setupCamera2();
        openCamera2();*/
    }


}