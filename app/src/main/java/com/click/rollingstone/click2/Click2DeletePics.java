package com.click.rollingstone.click2;

import android.content.Context;
import android.widget.Toast;

import java.io.File;

/**
 * Created by Aniruddha_Saundattik on 9/23/2015.
 */
public class Click2DeletePics {
    public void deletepictures(Context context){
        try {
            File dir = new File(android.os.Environment.getExternalStorageDirectory(), "Click");
            if (dir.isDirectory()) {
                String[] children = dir.list();
                for (String child : children) {
                    if (child.endsWith(".jpeg") || child.endsWith(".jpg"))
                        new File(dir, child).delete();

                }
            }
            Toast.makeText(context,"Pictures Deleted ",Toast.LENGTH_LONG).show();
        }
        catch (Exception e){
            e.printStackTrace();
            //get the logs here..
        }

    }
}
