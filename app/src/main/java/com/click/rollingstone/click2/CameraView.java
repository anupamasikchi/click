package com.click.rollingstone.click2;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;

public class CameraView extends Activity implements SurfaceHolder.Callback,
        OnClickListener {

    private Camera mCamera;
    private boolean mPreviewRunning = false;
    private String filename;

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        Click2Logging.getInstance().write("CameraView: On create called");

        filename = "";
        setContentView(R.layout.cameraview);

        mSurfaceView = (SurfaceView) findViewById(R.id.surface_camera);

        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);

        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mSurfaceHolder.setKeepScreenOn(true);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    protected void onResume() {
        Click2Logging.getInstance().write("CameraView: On Resume called");
        super.onResume();
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    protected void onStop() {
        Click2Logging.getInstance().write("CameraView: On Stop called");
        super.onStop();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {

        Click2Logging.getInstance().write("CameraView: SurfaceChanged called");

        if(mPreviewRunning) {
            mCamera.stopPreview();
        }

        Camera.Parameters p = mCamera.getParameters();

        mCamera.setParameters(p);

        mCamera.startPreview();
        mPreviewRunning = true;
        mCamera.takePicture(null, null, mPictureCallback);

    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        Click2Logging.getInstance().write("CameraView: SurfaceDestroyed called");
        stopCamera();
    }

    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;

    public void onClick(View v) {
        mCamera.takePicture(null, mPictureCallback, mPictureCallback);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        Click2Logging.getInstance().write("CameraView: SurfaceCreated called");

        Click2Logging.getInstance().write("CameraView: Getting frontfacing camera id");

        int i = findFrontFacingCamera();

        Click2Logging.getInstance().write("CameraView: Frontfacing camera id is - "+i);

        if(i > 0) ;
        while(true) {
            try {
                this.mCamera = Camera.open(i);
                try {
                    this.mCamera.setPreviewDisplay(holder);
                    return;
                } catch(IOException localIOException2) {
                    stopCamera();
                    return;
                }
            } catch(Exception ex) {
                Click2Logging.getInstance().write("Error while opening camera - "+ex.getMessage());
                if(this.mCamera == null) continue;
                stopCamera();
                this.mCamera = Camera.open(i);
                try {
                    this.mCamera.setPreviewDisplay(holder);
                    return;
                } catch(IOException localIOException1) {
                    stopCamera();
                    Click2Logging.getInstance().write("Error while opening camera - " + localIOException1.getMessage());
                    return;
                }

            }
        }
    }

    private void stopCamera() {
        Click2Logging.getInstance().write("CameraView: Stopping camera");
        if(this.mCamera != null) {

            //this.mCamera.stopPreview();
            this.mCamera.release();
            this.mCamera = null;

            this.mPreviewRunning = false;
        }
        Click2Logging.getInstance().write("CameraView: Camera Stopped");
    }

    private int findFrontFacingCamera() {
        int i = Camera.getNumberOfCameras();
        for(int j = 0 ; ; j++) {
            if(j >= i) return -1;
            Camera.CameraInfo localCameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(j, localCameraInfo);
            if(localCameraInfo.facing == 1) return j;
        }
    }

    Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {

        public void onPictureTaken(byte[] data, Camera camera) {
            if(data != null) {

                Click2Logging.getInstance().write("CameraView: Picture callback, Will save picture now");
                mCamera.stopPreview();
                mPreviewRunning = false;
                mCamera.release();

                try {
                    BitmapFactory.Options opts = new BitmapFactory.Options();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0,
                            data.length, opts);
                    bitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, false);
                    int width = bitmap.getWidth();
                    int height = bitmap.getHeight();
                    int newWidth = 300;
                    int newHeight = 300;

                    // calculate the scale - in this case = 0.4f
                    float scaleWidth = ((float) newWidth) / width;
                    float scaleHeight = ((float) newHeight) / height;

                    // createa matrix for the manipulation
                    Matrix matrix = new Matrix();
                    // resize the bit map
                    matrix.postScale(scaleWidth, scaleHeight);
                    // rotate the Bitmap
                    matrix.postRotate(-90);
                    Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                            width, height, matrix, true);

                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 40,
                            bytes);

                    File dir = new File(Click2Logging.getInstance().appPath);

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                    String currentDateandTime = sdf.format(new Date());
                    File f = new File(dir, "Image"+currentDateandTime+".jpg");
                    filename = f.getAbsolutePath();

                    System.out.println("File F : " + f);

                    f.createNewFile();
                    // write the bytes in file
                    FileOutputStream fo = new FileOutputStream(f);
                    fo.write(bytes.toByteArray());

                    // remember close de FileOutput
                    fo.close();
                    Click2Logging.getInstance().write("CameraView: Picture callback, Picture Saved");
                } catch(Exception e) {
                    Click2Logging.getInstance().write("CameraView: Picture callback, Error while saving picture - "+e.getMessage());
                }
                try {
                    SendMailTask sm = new SendMailTask(filename);
                    sm.setApplicationContext(getApplicationContext());
                    sm.execute();
                }catch (Exception e){
                    Click2Logging.getInstance().write(e.getStackTrace().toString());
                    Click2Logging.getInstance().write("CameraView: Email sending failed.");
                }
                // StoreByteImage(mContext, imageData, 50,"ImageName");
                // setResult(FOTO_MODE, mIntent);
                setResult(585);
                finish();
            }
        }
    };


}