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

        DevicePolicyManager mgr = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        int no = mgr.getCurrentFailedPasswordAttempts();
        if (Build.VERSION.SDK_INT < 22) {
            Intent intent1 = new Intent(context, CameraView.class);
            // context.startActivity(new Intent(context, ClickTakePicture.class));
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
        }
        else{
            try {

                    Click2CaptureHim captureHim = new Click2CaptureHim(context);
                    captureHim.takePicture();

            } catch (Exception e) {
                Log.i("Click2", "Crashed due to" + e);
            }
        }
       /* Click2Counter.getInstance().i++;
        int number = Click2Counter.getInstance().i;
        //Toast.makeText(context,"Password was incorrect"+number,Toast.LENGTH_LONG).show();
        super.onPasswordFailed(context, intent);
        // call camera code here
        Log.i("Click2", "Wrong Password was entered");

            Click2Capture cc=new Click2Capture(context);
            cc.CallCamera();
        } else {
            try {
                if (number == 2) {

                    Click2CaptureHim captureHim = new Click2CaptureHim(context);
                    captureHim.takePicture();
                    Click2Counter.getInstance().i = 0;
                }

            } catch (Exception e) {
                Log.i("Click2", "Crashed due to" + e);
            }
        }*/
    }



}
