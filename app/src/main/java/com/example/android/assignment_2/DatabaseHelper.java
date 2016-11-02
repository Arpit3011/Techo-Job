package com.example.android.assignment_2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "assignment2";
    // Table Name
    private static String TABLE_NAME = "";
    //coloumn names
    private static final String TIMESTAMP = "timestamp";
    private static final String ACC_X = "acc_x";
    private static final String ACC_Y = "acc_y";
    private static final String ACC_Z = "acc_z";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void addTable(String table_name){

        TABLE_NAME=table_name;

        SQLiteDatabase db=this.getWritableDatabase();

        /*then call 'execSQL()' on it..*/
        String CREATE_TABLE = "CREATE TABLE " + table_name + "("
                + TIMESTAMP + " LONG," + ACC_X + " FLOAT,"
                + ACC_Y + " FLOAT," + ACC_Z + " FLOAT" +")";

        db.execSQL(CREATE_TABLE);
    }

    //add accelerometer value in the database
    public void addAccValue(long timestamp, float x, float y, float z){

        Log.d("In Database Helper File","*");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TIMESTAMP, timestamp);
        values.put(ACC_X, x);
        values.put(ACC_Y, y);
        values.put(ACC_Z, z);

        //insert rows
        db.insert(TABLE_NAME, null, values);
        db.close();         // Closing database connection
    }

    //get the latest 10 acc entries
    public List<AccelerometerModel> getLatestAccEntries(){

        List<AccelerometerModel> entries = new ArrayList<>();

        //TODO: logic to extract 10 entries - check
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " order by " + TIMESTAMP +" desc limit 10";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                //int ts = Integer.parseInt(cursor.getString(0));
                float xval= Float.parseFloat(cursor.getString(1));
                float yval= Float.parseFloat(cursor.getString(2));
                float zval= Float.parseFloat(cursor.getString(3));

                AccelerometerModel accModel = new AccelerometerModel(xval, yval, zval);
                entries.add(accModel);


            } while (cursor.moveToNext());
        }

        db.close();
        return entries;

    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
    }
    @Override
    public void onOpen(SQLiteDatabase db){
    }
}
