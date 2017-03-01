package com.example.android.carparkappv1;


import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

public class CarparkDBController extends SQLiteOpenHelper {

    private static final String TAG = "CarparkDBControllerClass";

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Carpark.db";
    private static final String TABLE_CARPARKS = "Carparks";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_CARPARKNUM = "Carpark_num";
    private static final String COLUMN_address = "Address";
    private static final String COLUMN_Xcoord = "X_Coord";
    private static final String COLUMN_Ycoord = "Y_Coord";
    private static final String COLUMN_CPTYPE = "Carpark_type";
    private static final String COLUMN_TYPE_PARKING_SYS = "Type_Of_Parking_System";
    private static final String COLUMN_STP = "short_term_parking";
    private static final String COLUMN_FP = "free_parking";
    private static final String COLUMN_NP ="night_parking";



    public CarparkDBController(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.i(TAG, "onCreate database!");
        String query = "CREATE TABLE " + TABLE_CARPARKS + " ("
                        + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + COLUMN_CARPARKNUM + " TEXT, "
                        + COLUMN_address + " TEXT, "
                        + COLUMN_Xcoord + " DOUBLE, "
                        + COLUMN_Ycoord + " DOUBLE, "
                        + COLUMN_CPTYPE + " TEXT, "
                        + COLUMN_TYPE_PARKING_SYS + " TEXT, "
                        + COLUMN_STP + " TEXT, "
                        + COLUMN_FP + " TEXT, "
                        + COLUMN_NP + " TEXT "
                        + ");";

        sqLiteDatabase.execSQL(query);
        Log.i(TAG, "created table_carparks");
        try {
            Log.i(TAG, "adding csv information...");
            addCSVintoDB("assets/hdb-carpark-information.csv");
            Log.i(TAG, "adding csv information success");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        Log.i(TAG, "onOpen database!");
    }

    //Add new row into temp table
    public void addRow(){

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CARPARKNUM, "ACB");
        values.put(COLUMN_Xcoord, 30314.7936);
        values.put(COLUMN_Ycoord, 31490.4942);


        db.insert(TABLE_CARPARKS, null, values);
        db.close();

    }


    //load values from csv file.
    public void addCSVintoDB(String filename) throws IOException {
        Log.i(TAG,  "addCSVintoDB method");
        Log.i(TAG, "filename");
        SQLiteDatabase db = getWritableDatabase();

        FileReader file = new FileReader(filename);
        BufferedReader buffer = new BufferedReader(file);
        String line = "";

        //INSERT INTO Carparks (COLUMN_CARPARKNUM, COLUMN_Xcoord, COLUMN_Ycoord) VALUES (... ... ...)
        while((line = buffer.readLine()) != null){
            Log.i(TAG, "inside while loop");
            String [] columns = line.split(",");
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_CARPARKNUM, columns[0]);
            cv.put(COLUMN_address, columns[1]);
            cv.put(COLUMN_Xcoord, columns[2]);
            cv.put(COLUMN_Ycoord, columns[3]);
            cv.put(COLUMN_CPTYPE, columns[4]);
            cv.put(COLUMN_TYPE_PARKING_SYS, columns[5]);
            cv.put(COLUMN_STP, columns[6]);
            cv.put(COLUMN_FP, columns[7]);
            cv.put(COLUMN_NP, columns[8]);
            db.insert(TABLE_CARPARKS, null, cv);
        }
        db.close();
    }

    public String dbToString(){
        Log.i(TAG, "enter dbToString");
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_CARPARKS + " WHERE 1;";
        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();
        dbString += c.getString(c.getColumnIndex(COLUMN_CARPARKNUM));
        dbString += c.getString(c.getColumnIndex(COLUMN_address));
        dbString += c.getString(c.getColumnIndex(COLUMN_Xcoord));
        dbString += c.getString(c.getColumnIndex(COLUMN_Ycoord));
        /*while(!c.isAfterLast()){
            i++;
            Log.i(TAG, "inside while loop of dbToString, i: " + i);
            if(c.getString(c.getColumnIndex(COLUMN_CARPARKNUM)) !=null){
                dbString += c.getString(c.getColumnIndex(COLUMN_CARPARKNUM));
                dbString += "\n";
            }
        }*/

        db.close();
        c.close();
        Log.i(TAG, "dbString: " + dbString);
        return dbString;

    }
}
