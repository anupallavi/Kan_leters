package com.example.hp.kan_leters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by Hp on 5/17/2017.
 */

public class ContentDatabase extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "diary.db";
    public static final String TABLE_NAME = "diary";
    public static final String COL1 = "date";
    public static final String COL2 = "content";
    public static final String FILE_DIR = "diaryContent";


    public ContentDatabase(Context context) {

        // super(context, DATABASE_NAME, null, 1);
        super(context, Environment.getExternalStorageDirectory()
                + File.separator + FILE_DIR + File.separator + DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (date TEXT PRIMARY KEY, " +
                " content TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public String sendDate(int dayOfMonth, int month, int year)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String date = dayOfMonth + "/" + month + "/"+ year;
        //  ContentValues contentValues = new ContentValues();
        Cursor cursor = db.query(TABLE_NAME,new String[] {COL2}, COL1 + "=?",new String[] {date},null,null,null,null);
        // return date;
        if (cursor.moveToNext()) {
            String content = cursor.getString(0);
            Log.d("DATABASE OPERATION", "getting content");
            return content;
        }
        else {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL1, date);
            contentValues.put(COL2, "");
            db.insert(TABLE_NAME, null, contentValues);
            //String sql1 = "insert into " + TABLE_NAME + "(" + COL1 + "," + COL2+ ") values( date,'');";
            //db.execSQL(sql1);
            Log.d("DATABASE OPERATION", "New date inserted");
            return "";
        }
    }

    public void addData(String date, String content, SQLiteDatabase db) {

        ContentValues contentValues = new ContentValues();
        //contentValues.put(COL1,date);
        contentValues.put(COL2, content);
        db.update(TABLE_NAME, contentValues, COL1 + " = ?", new String[]{date});
        //long result = db.insert(TABLE_NAME, null, contentValues);
        // String strSQL = " UPDATE " + TABLE_NAME +"SET COL2 = "+ content + ")WHERE COL1 = "+ date ;
        // db.update(TABLE_NAME, contentValues, "COL1 = ?", new String[]{date});
        // db.execSQL(strSQL);
        //if date as inserted incorrectly it will return -1


    }
    public Cursor getListContents(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return data;
    }
}




