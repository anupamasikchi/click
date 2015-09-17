package com.click.rollingstone.click2;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Anupama_Sikchi on 9/3/2015.
 */
public class Click2AdminReceiver extends DeviceAdminReceiver {

    @Override
    public void onPasswordSucceeded(Context context, Intent intent) {
        super.onPasswordSucceeded(context, intent);
        Log.i("Click2", "Correct Password was entered");
        Click2Counter.getInstance().i = 0;
    }

    @Override
    public void onPasswordFailed(Context context, Intent intent) {

        try {
        Click2Logging.getInstance().write("On password failed event received");
        DevicePolicyManager mgr = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        if (Build.VERSION.SDK_INT < 21) {
            Click2Logging.getInstance().write("SDK Version is less than 21");
            Intent intent1 = new Intent(context, CameraView.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
        }
        else{
            Click2Logging.getInstance().write("SDK Version is greater than or equal to 21");
            Click2CaptureHim captureHim = new Click2CaptureHim(context);
                    captureHim.takePicture();
            }
            Click2Logging.getInstance().write("On password failed event ends");
        }
        catch (Exception e) {
            Click2Logging.getInstance().write("Exception while");
        }
    }



}
