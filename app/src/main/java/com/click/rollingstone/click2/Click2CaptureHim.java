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
         Click2Logging.getInstance().write("CameraDevice: takePicture called");
         backgroundHandler = new Handler(context.getMainLooper());
         setupCamera2();
         openCamera2();
    }

    private void setupCamera2() {

        Click2Logging.getInstance().write("CameraDevice: setting up Camera");
        CameraManager manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);

        try {

            for (String cameraId : manager.getCameraIdList()) {
                CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);

                if (characteristics.get(CameraCharacteristics.LENS_FACING) != CameraCharacteristics.LENS_FACING_FRONT) {
                    continue;
                }

                Click2Logging.getInstance().write("CameraDevice: front camera id is - "+cameraId);
                this.cameraId = cameraId;

                int picWidth = 640;
                int picHeight = 480;

                imageReader = ImageReader.newInstance(picWidth, picHeight, ImageFormat.JPEG, 2);

                ClickImageAvailableListener imageAvailableListener = new ClickImageAvailableListener(context);
                imageReader.setOnImageAvailableListener(imageAvailableListener, backgroundHandler);
            }
            Click2Logging.getInstance().write("CameraDevice: Camera setup successful");
        } catch (CameraAccessException | NullPointerException  e) {
                Click2Logging.getInstance().write("CameraDevice: error while setting up camera- "+e.getMessage());
        }
    }

    private void openCamera2() {
        CameraManager manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        try {
            Click2Logging.getInstance().write("CameraDevice: opening camera");
            manager.openCamera(cameraId, cameraStateCallback, backgroundHandler);
            Click2Logging.getInstance().write("CameraDevice: open camera successful ");
        } catch (CameraAccessException e) {
            Click2Logging.getInstance().write("CameraDevice: error while opening up camera- " + e.getMessage());
        }
    }



    private final CameraDevice.StateCallback cameraStateCallback = new CameraDevice.StateCallback() {

        @Override
        public void onOpened(CameraDevice device) {
            Click2Logging.getInstance().write("CameraDevice: onOpened Callback");
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

            Click2Logging.getInstance().write("CameraDevice: creating capture Request");
            CaptureRequest.Builder requestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            requestBuilder.addTarget(imageReader.getSurface());

            // Focus
            requestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);

            // Orientation

            cameraCaptureSession.capture(requestBuilder.build(), new ClickCaptureCallBack(cameraDevice), null);
            Click2Logging.getInstance().write("CameraDevice: create capture Request Successful");
        } catch (CameraAccessException e) {
            Click2Logging.getInstance().write("CameraDevice: error while trying to create capture Request - "+e.getMessage());
        }
    }

}
