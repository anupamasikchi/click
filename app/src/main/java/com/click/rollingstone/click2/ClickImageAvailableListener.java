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
    String filename;

    public ClickImageAvailableListener(Context context){
        this.context = context;
    }
    @Override
    public void onImageAvailable(ImageReader reader) {

        try {
        Click2Logging.getInstance().write("CameraDevice: Image is available now");
        Image image= reader.acquireLatestImage();
            final Image.Plane[] planes = image.getPlanes();
            final ByteBuffer buffer = planes[0].getBuffer();
            final byte[] data = new byte[buffer.capacity()];
            buffer.get(data);
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

            File dir = new File(Click2Logging.getInstance().appPath);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String currentDateandTime = sdf.format(new Date());
            File file = new File(dir, "Image"+currentDateandTime+".jpg");
            FileOutputStream fOut = new FileOutputStream(file);

            bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            fOut.close();
            Click2Logging.getInstance().write("CameraDevice: Image saved");
            filename = file.getPath();
        } catch (Exception e) {
            Click2Logging.getInstance().write("CameraDevice: Error while saving Image - "+e.getMessage());
        }

        try {
            SendMailTask sm = new SendMailTask(filename);
            sm.setApplicationContext(this.context);
            sm.execute();
        }catch (Exception e){
            Click2Logging.getInstance().write("ClickImageAvailableListener: Error in sending mail.");
        }
    }
}
