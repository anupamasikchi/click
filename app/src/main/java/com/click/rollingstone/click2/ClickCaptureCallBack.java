package com.click.rollingstone.click2;

import android.annotation.TargetApi;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.os.Build;
import android.util.Log;

/**
 * Created by Anupama_Sikchi on 9/2/2015.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class ClickCaptureCallBack extends CameraCaptureSession.CaptureCallback {

    CameraDevice device = null;
    ClickCaptureCallBack(CameraDevice device){
    this.device = device;
    }
    @Override
    public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
        super.onCaptureCompleted(session, request, result);
        Click2Logging.getInstance().write("CameraDevice: capture complete");
        if(device!=null){
            device.close();
        }
    }

}
