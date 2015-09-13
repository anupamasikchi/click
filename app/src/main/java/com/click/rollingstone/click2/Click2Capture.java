package com.click.rollingstone.click2;

import android.content.Context;
import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import javax.security.auth.callback.Callback;

/**
 * Created by Anupama_Sikchi on 9/5/2015.
 */
public class Click2Capture {
    Context context;
    public Click2Capture(Context c){
        context = c;

    }
    public void CallCamera() {
        try {

            /*TextView myAwesomeTextView = (TextView) findViewById(R.id.textView);
            myAwesomeTextView.setText("1");*/

            /*camera.setPreviewDisplay(surface.getHolder());
            /*camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();


            cc.takePicture();
            cc.releaseCamera();
            */

            Intent intent1 = new Intent(context,ClickTakePicture.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);


        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }





}


