package com.click.rollingstone.click2;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

/**
 * Created by Ayush_M on 9/17/2015.
 */
public class Click2Logging {

    private BufferedWriter writer;
    private static Click2Logging logger;
    public String appPath;

    private Click2Logging(){
        appPath = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/Click";
    }

    public synchronized static Click2Logging getInstance(){
        if(logger==null){
            logger = new Click2Logging();
        }
         return logger;
    }

    private void exists(){
        File dir = new File(appPath);
        if (!dir.exists())
            dir.mkdirs();
    }
    public synchronized void write(String data){
        exists();
        Log.d("Click2",data);
        File  logFile = new File(appPath, "Log.txt");
        FileWriter logWriter = null;
        try {
            logWriter = new FileWriter(logFile,true);

        writer = new BufferedWriter(logWriter);
        Date date = new Date();
        writer.write(data +"\n Logged at" + String.valueOf(date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds() + "\n"));
        writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
