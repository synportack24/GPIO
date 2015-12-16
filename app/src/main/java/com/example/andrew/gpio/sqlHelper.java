package com.example.andrew.gpio;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrew on 12/14/15.
 */
public class sqlHelper extends SQLiteOpenHelper {

    private static final String KEY_NAME = "name";
    private static final String DATABASE_NAME = "EnvironmentDb";
    private static final String DATABASE_TABLE = "Environment_TABLE";
    private static final int DATABASE_VERSION = 1;

    public static final String COLUMN0 = "slno";
    public static final String COLUMN1 = "time";
    public static final String COLUMN2 = "tempurature";

    public static final String KEY1 = "time";
    public static final String KEY2 = "tempurature";

    String SCRIPT_CREATE_TABLE = "CREATE TABLE " + DATABASE_TABLE + "(" +
            COLUMN0 + " INTEGER PRIMARY KEY, " +
            COLUMN1 + " TEXT, " +
            COLUMN2 + " REAL)";

    public sqlHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public sqlHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SCRIPT_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        onCreate(db);
    }

    public void addTempuraturestoTable(i2cValue std){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY1, std.getTimeDate());
        values.put(KEY2, std.getTempurature());

        db.insert(DATABASE_TABLE, null, values);
        db.close();

    }

    public List<i2cValue> getAllTempuratures(){
        List <i2cValue> tempList = new ArrayList<i2cValue>();

        String selectQuery = "SELECT * FROM " + DATABASE_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do {
                String date = cursor.getString(1);
                Double temp = Double.parseDouble(cursor.getString(2));
                i2cValue st = new i2cValue(date, temp);
                tempList.add(st);
            } while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return tempList;
    }

}