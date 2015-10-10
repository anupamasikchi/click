package com.click.rollingstone.click2;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;

/**
 * Created by Dhara_Badani on 9/17/2015.
 */
public class Click2Uninstall extends Click2AdminReceiver {
    public void Uninstall(Context context) {
        try {
            //Remove Click folder
            try {
                String appPath = Click2Logging.getInstance().appPath;
                File dir = new File(appPath);
                if (dir.isDirectory())
                {
                    String[] children = dir.list();
                    for (int i = 0; i < children.length; i++)
                    {
                        new File(dir, children[i]).delete();
                    }

                    dir.delete();
                }
            }
            catch(Exception ex){
            }
            //Disabling Admin rights
            ComponentName devAdminReceiver = new ComponentName(context, Click2AdminReceiver.class);
            DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
            dpm.removeActiveAdmin(devAdminReceiver);
            //Moving on to uninstall the App
            Uri packageURI = Uri.parse("package:com.click.rollingstone.click2");
            Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
            uninstallIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(uninstallIntent);
        } catch (Exception e) {
            e.printStackTrace();
            //Add Logs here}
        }
    }
}
