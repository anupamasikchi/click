package com.click.rollingstone.click2;
import android.hardware.Camera;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ClickTakePicture extends ActionBarActivity {

    boolean shouldITake = false;
    String filename = "";
    private void takeSnapShots()
    {
        Toast.makeText(getApplicationContext(), "Image snapshot   Started",Toast.LENGTH_SHORT).show();
        // here below "this" is activity context.
        SurfaceView surface = new SurfaceView(this);
        Camera camera = Camera.open();
        try {
            camera.setPreviewDisplay(surface.getHolder());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        camera.startPreview();
        camera.setPreviewCallback(mPreviewCallBack);
        try {
            //while(!shouldITake){
            //    Log.d("Click2","Waiting for preview to be ready");
            //}
           camera.takePicture(null, null, jpegCallback);
        }
        catch (Exception ex){
            Click2Logging.getInstance().write("Click2: " + ex.getMessage());
        }
        try {
            SendMailTask sm = new SendMailTask(filename);
            sm.setApplicationContext(getApplicationContext());
            sm.execute();
        }catch (Exception e){
            Click2Logging.getInstance().write("ClickTakePicture: " + e.getStackTrace().toString());
            Click2Logging.getInstance().write("ClickTakePicture: Mail sending failed.");
        }
    }

    Camera.PreviewCallback mPreviewCallBack=new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            Log.d("Click2","the preview is ready now");
            try{
           // camera.takePicture(null, null, jpegCallback);
                shouldITake = true;
        }catch (Exception ex){
                Log.d("Click2ayush",ex.getMessage());
            }
        }
    };

    /** picture call back */
    Camera.PictureCallback jpegCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera)
        {
            FileOutputStream outStream = null;
            try {
                String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                        "/Click";
                File dir = new File(file_path);
                if(!dir.exists())
                    dir.mkdirs();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                String currentDateandTime = sdf.format(new Date());
                File file = new File(dir, "Image"+currentDateandTime+".jpg");
                outStream = new FileOutputStream(file);


                outStream.write(data);
                outStream.close();
                filename = file.getPath();
                Log.d("Click2", "onPictureTaken - wrote bytes: " + data.length);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally
            {
                camera.stopPreview();
                camera.release();
                camera = null;
                Toast.makeText(getApplicationContext(), "Image snapshot Done", Toast.LENGTH_LONG).show();


            }
            Log.d("Click2", "onPictureTaken - jpeg");
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clicktake_picture);
        takeSnapShots();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
      //  getMenuInflater().inflate(R.menu.menu_clicktake_picture, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}


/*import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Anupama_Sikchi on 9/6/2015.
 */
/*public class ClickTakePicture extends Activity implements SurfaceHolder.Callback {

    //a variable to store a reference to the Image View at the main.xml file
    private ImageView iv_image;
    //a variable to store a reference to the Surface View at the main.xml file
    private SurfaceView sv;

    //a bitmap to display the captured image
    private Bitmap bmp;

    //Camera variables
    //a surface holder
    private SurfaceHolder sHolder;
    //a variable to control the camera
    private android.hardware.Camera mCamera;
    //the camera parameters
    private android.hardware.Camera.Parameters parameters;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clicktake_picture);
        CameraController cc = new CameraController(getApplicationContext());

        android.hardware.Camera camera = cc.getCameraInstance();

        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surfaceView); //add this to your xml view
        SurfaceHolder surfaceHolder = surfaceView.getHolder();

        Context c= getApplicationContext();

        surfaceHolder.addCallback(ClickTakePicture.this);

        mCamera = cc.getCameraInstance();
        //get the Image View at the main.xml file

        try {
            camera.setPreviewDisplay(surfaceHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        camera.startPreview();
        cc.takePicture();
        cc.releaseCamera();

    }



    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }



    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // The Surface has been created, acquire the camera and tell it where
        // to draw the preview.
        mCamera = android.hardware.Camera.open();
        try {
            mCamera.setPreviewDisplay(holder);

        } catch (IOException exception) {
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //stop the preview
        mCamera.stopPreview();
        //release the camera
        mCamera.release();
        //unbind the camera from this object
        mCamera = null;
    }
}
*/