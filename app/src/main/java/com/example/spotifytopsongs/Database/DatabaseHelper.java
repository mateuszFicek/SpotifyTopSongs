package com.example.spotifytopsongs.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Class that connects application with SQLite database.
 * Here are saved top songs to database.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";
    private static final String TABLE_NAME_YESTERDAY = "top_songs_yesterday";
    private static final String TABLE_NAME_TODAY = "top_songs_today";
    private static final String COL1 = "position";
    private static final String COL2 = "songID";

    public DatabaseHelper(Context context) {
        super(context, TABLE_NAME_TODAY, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable1 = "CREATE TABLE " + TABLE_NAME_YESTERDAY + " (" + COL1 +" INTEGER, " + COL2 + " TEXT)";
        String createTable2 = "CREATE TABLE " + TABLE_NAME_TODAY + " (" + COL1 + " INTEGER, " + COL2 + " TEXT)";
        db.execSQL(createTable1);
        db.execSQL(createTable2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME_YESTERDAY);
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME_TODAY);
        onCreate(db);
    }

    /**
     * Function to clear data from today's database.
     */
    public void clearToday(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME_TODAY);
        db.delete(TABLE_NAME_TODAY,null,null);
    }

    public void clearYesterday(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME_YESTERDAY);
        db.delete(TABLE_NAME_YESTERDAY,null,null);
    }

    public boolean addDataToday(int pos, String item){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, pos);
        contentValues.put(COL2, item);

        long result = db.insert(TABLE_NAME_TODAY, null, contentValues);
        if(result == -1){
            return false;
        } else
            return true;
    }

    public boolean addDataYesterday(int pos, String item){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, pos);
        contentValues.put(COL2, item);

        long result = db.insert(TABLE_NAME_YESTERDAY, null, contentValues);
        if(result == -1){
            return false;
        } else
            return true;
    }

    /**
     * Function to move data from one database to another.
     */
    public void moveFromTodayToYesterday(){
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TABLE_NAME_YESTERDAY,null,null);
        String query = "SELECT * FROM " + TABLE_NAME_TODAY;
        Cursor data = db.rawQuery(query,null);
        int counter = 0;
        while(data.moveToNext()){
            addDataYesterday(counter, data.getString(1));
            counter++;
        }
    }

    public Cursor getYesterdayData(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_YESTERDAY;
        Cursor data = db.rawQuery(query,null);
        return data;
    }

}
