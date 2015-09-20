package com.click.rollingstone.click2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.List;

import javax.xml.transform.Result;

/**
 * Created by Aniruddha_Saundattik on 9/19/2015.
 */
public class MailManager extends BroadcastReceiver {

    Context context;
    @Override
    public void onReceive(final Context context, final Intent intent) {
        this.context = context;
        final ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (isNetworkConnected()) {
            LocalDB localDB = new LocalDB(context);
            List<Mail> mailList = localDB.getAllEmails();
            for (Mail mail : mailList){
                Click2Logging.getInstance().write("MailManager: id - " + mail.get_id());
                Click2Logging.getInstance().write("MailManager: filename - " + mail.get_filename());
                SendMailTask sm = new SendMailTask(mail.get_filename());
                localDB.deleteMail(mail);
                sm.setApplicationContext(context);
                sm.execute();
            }
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        // Check the current state of the Network Information
        if (networkInfo == null)
            return false;
        if (networkInfo.isConnected() == false)
            return false;
        if (networkInfo.isAvailable() == false)
            return false;
        return true;
    }
}
