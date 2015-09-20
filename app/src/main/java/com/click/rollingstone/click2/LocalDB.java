package com.click.rollingstone.click2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aniruddha_Saundattik on 9/19/2015.
 */
public class LocalDB extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static final String EMAIL_TABLE_NAME = "emails";
    private static final String KEY_ID = "id";
    private static final String KEY_EMAIL = "email_to";
    private static final String KEY_SUBJECT = "subject";
    private static final String KEY_BODY = "body";
    private static final String KEY_FILENAME = "filename";
    private static final String EMAIL_TABLE_CREATE =
            "CREATE TABLE " + EMAIL_TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_EMAIL + " TEXT, " +
                    KEY_SUBJECT + " TEXT, " +
                    KEY_BODY + " TEXT, " +
                    KEY_FILENAME + " TEXT);";

    LocalDB(Context context) {
        super(context, "EMAIL_MANAGER", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(EMAIL_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + EMAIL_TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public void addEmail(Mail mail){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FILENAME, mail.get_filename());

        // Inserting Row
        db.insert(EMAIL_TABLE_NAME, null, values);
        //2nd argument is String containing nullColumnHack
        db.close();
    }

    public List<Mail> getAllEmails(){
        List<Mail> mailList = new ArrayList<Mail>();
        String selectQuery = "SELECT  * FROM " + EMAIL_TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Mail mail = new Mail();
                mail.set_id(Integer.parseInt(cursor.getString(0)));
                mail.set_filename(cursor.getString(4));
                mailList.add(mail);
            } while (cursor.moveToNext());
        }

        return mailList;
    }

    /*public int updateMail(Mail m){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FILENAME, m.get_filename());

        // updating row
        return db.update(EMAIL_TABLE_NAME, values, KEY_ID + " = ?",
                new String[] { String.valueOf(m.get_id()) });
    }*/

    public void deleteMail(Mail m){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(EMAIL_TABLE_NAME, KEY_ID + " = ?",
                new String[] { String.valueOf(m.get_id()) });
        db.close();
    }
}
