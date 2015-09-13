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
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;

import java.util.LinkedList;
import java.util.List;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)

public class Click2CaptureHim {


    String cameraId;
    CameraDevice cameraDevice;
    Handler backgroundHandler;
    ImageReader imageReader;
    CameraCaptureSession cameraCaptureSession;
    Context context;
    public Click2CaptureHim(Context c){
        context = c;
    }

     public void takePicture(){
        Log.d("Click2","Came to takePicture");
        backgroundHandler = new Handler(context.getMainLooper());
        //if(Click2Counter.getInstance().setUpCamera) {
        //    Click2Counter.getInstance().setUpCamera = false;
        //}
         setupCamera2();
         openCamera2();


    }

    private void setupCamera2() {

        CameraManager manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);

        try {

            for (String cameraId : manager.getCameraIdList()) {
                CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);

                if (characteristics.get(CameraCharacteristics.LENS_FACING) != CameraCharacteristics.LENS_FACING_FRONT) {
                    continue;
                }

                this.cameraId = cameraId;

                int picWidth = 640;
                int picHeight = 480;

                imageReader = ImageReader.newInstance(picWidth, picHeight, ImageFormat.JPEG, 2);

                ClickImageAvailableListener imageAvailableListener = new ClickImageAvailableListener(context);
                imageReader.setOnImageAvailableListener(imageAvailableListener, backgroundHandler);
            }

        } catch (CameraAccessException | NullPointerException e) {
            e.printStackTrace();
        }
    }

        private void openCamera2() {
        CameraManager manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        try {
            Log.d("Click2","Came before OpenCamera");
            manager.openCamera(cameraId, cameraStateCallback, backgroundHandler);
            Log.d("Click2", "Came after OpenCamera");
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }



    private final CameraDevice.StateCallback cameraStateCallback = new CameraDevice.StateCallback() {

        int a;
        @Override
        public void onOpened(CameraDevice device) {
            cameraDevice = device;
            createCaptureSession();
        }

        @Override
        public void onDisconnected(CameraDevice cameraDevice) {}

        @Override
        public void onError(CameraDevice cameraDevice, int error) {}
    };

    private void createCaptureSession() {
        List<Surface> outputSurfaces = new LinkedList<>();
        outputSurfaces.add(imageReader.getSurface());

        try {

            cameraDevice.createCaptureSession(outputSurfaces, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(CameraCaptureSession session) {
                    cameraCaptureSession = session;
                    createCaptureRequest();
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession session) {}
            }, null);

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }




    private void createCaptureRequest() {
        try {

            CaptureRequest.Builder requestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            requestBuilder.addTarget(imageReader.getSurface());

            // Focus
            requestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);

            // Orientation

            cameraCaptureSession.capture(requestBuilder.build(), new ClickCaptureCallBack(cameraDevice), null);

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

}
