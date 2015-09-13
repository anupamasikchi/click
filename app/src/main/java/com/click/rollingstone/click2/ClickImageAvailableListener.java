package com.click.rollingstone.click2;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.ImageReader;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Anupama_Sikchi on 9/2/2015.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class ClickImageAvailableListener implements ImageReader.OnImageAvailableListener{

    Context context = null;

    public ClickImageAvailableListener(Context context){
        this.context = context;
    }
    @Override
    public void onImageAvailable(ImageReader reader) {


        try {

        Log.d("Click2","Came in OnImageAvailable");
        Image image= reader.acquireLatestImage();
            final Image.Plane[] planes = image.getPlanes();
            final ByteBuffer buffer = planes[0].getBuffer();
            final byte[] data = new byte[buffer.capacity()];
            buffer.get(data);
             Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

            //Bitmap.c
      /*  Bitmap bMap=Bitmap.createBitmap(buffer.capacity(),buffer.capacity(),Bitmap.Config.ARGB_8888);
        bMap.copyPixelsFromBuffer(buffer);*/
            String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                   "/Click";
            File dir = new File(file_path);
            if(!dir.exists())
                dir.mkdirs();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String currentDateandTime = sdf.format(new Date());
            File file = new File(dir, "Image"+currentDateandTime+".jpg");
            FileOutputStream fOut = new FileOutputStream(file);

            bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            fOut.close();

/*
        File directory = context.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,"profile.jpg");

        FileOutputStream fos = null;


            fos = new FileOutputStream(mypath);

            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();*/

        } catch (Exception e) {
            Log.getStackTraceString(e);
        }
    }
}
