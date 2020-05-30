package com.example.spotifytopsongs.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";
    private static final String TABLE_NAME_YESTERDAY = "top_songs_yesterday";
    private static final String TABLE_NAME_TODAY = "top_songs_today";
    private static final String COL1 = "number";
    private static final String COL2 = "songID";

    public DatabaseHelper(Context context) {
        super(context, TABLE_NAME_TODAY, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable1 = "CREATE TABLE " + TABLE_NAME_YESTERDAY + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + COL2 + " TEXT)";
        String createTable2 = "CREATE TABLE " + TABLE_NAME_TODAY + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + COL2 + " TEXT)";
        db.execSQL(createTable1);
        db.execSQL(createTable2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME_YESTERDAY);
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME_TODAY);
        onCreate(db);
    }

    public void clearToday(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME_TODAY,null,null);
    }

    public void clearYesterday(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME_YESTERDAY,null,null);
    }

    public boolean addDataToday(String item){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, item);

        Log.d(TAG, "addData: Adding... " + item + " to " + TABLE_NAME_TODAY);
        long result = db.insert(TABLE_NAME_TODAY, null, contentValues);
        if(result == -1){
            return false;
        } else
            return true;
    }

    public boolean addDataYesterday(String item){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, item);

        Log.d(TAG, "addData: Adding... " + item + " to " + TABLE_NAME_YESTERDAY);
        long result = db.insert(TABLE_NAME_YESTERDAY, null, contentValues);
        if(result == -1){
            return false;
        } else
            return true;
    }

    public void moveFromTodayToYesterday(){
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TABLE_NAME_YESTERDAY,null,null);
        String query = "SELECT * FROM " + TABLE_NAME_TODAY;
        Cursor data = db.rawQuery(query,null);
        while(data.moveToNext()){
            addDataYesterday(data.getString(1));
        }
    }

    public Cursor getYesterdayData(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_YESTERDAY;
        Cursor data = db.rawQuery(query,null);
        return data;
    }

}
