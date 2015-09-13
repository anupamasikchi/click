package com.click.rollingstone.click2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Anupama_Sikchi on 9/3/2015.
 */
public class Click2BroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_USER_PRESENT)){
            Log.i("Click2", "We made it");
            //Toas
        }

    }
}
