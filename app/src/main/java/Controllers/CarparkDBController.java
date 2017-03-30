package Controllers;


import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import MapProjectionConverter.SVY21Coordinate;

public class CarparkDBController extends SQLiteOpenHelper {

    private static CarparkDBController sInstance;

    private static final String TAG = "CarparkDBControllerClass";

    public static final String OWNER_HDB = "HDB";
    public static final String OWNER_DM = "DM";
    public static final String OWNER_SM = "SM";
    /*
    ER diagram approach.
    Carpark entity is the parent entity, subclass entity : HdbCarpark, SmCarpark
    Create static final string for parent entity Carpark

     */
    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Carpark.db";


    //Carpark Entity
    public static final String TABLE_CARPARKS = "Carparks";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_Xcoord = "X_Coord";
    public static final String COLUMN_Ycoord = "Y_Coord";
    public static final String COLUMN_OWNER_TYPE = "Type_of_Carpark";

    //Create Table for Carpark Entity
    public static final String CREATE_TABLE_CARPARK = "CREATE TABLE " + TABLE_CARPARKS + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_Xcoord + " DOUBLE, "
            + COLUMN_Ycoord + " DOUBLE, "
            + COLUMN_OWNER_TYPE + " TEXT);";


    public static final String TABLE_HDB_CARPARKS = "HdbCarparks";
    public static final String COLUMN_CARPARKNUM = "Carpark_num";
    public static final String COLUMN_address = "Address";
    public static final String COLUMN_CPTYPE = "Carpark_type";
    public static final String COLUMN_TYPE_PARKING_SYS = "Type_Of_Parking_System";
    public static final String COLUMN_STP = "short_term_parking";
    public static final String COLUMN_FP = "free_parking";
    public static final String COLUMN_NP ="night_parking";

    //Create table for HDB Carparks
    public static final String CREATE_TABLE_HDB_CARPARK = "CREATE TABLE " + TABLE_HDB_CARPARKS + " ("
            + COLUMN_ID + " INTEGER, "
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
    public static final String TABLE_DATAMALL_CARPARK = "DMCarpark";
    public static final String COLUMN_AREA = "Area";
    public static final String COLUMN_DEV = "Development";
    public static final String COLUMN_LOTS = "Lots";

    public static final String CREATE_TABLE_DATA_MALL = "CREATE TABLE " + TABLE_DATAMALL_CARPARK + " ( "
            + COLUMN_ID + " INTEGER, "
            + COLUMN_AREA + " TEXT, "
            + COLUMN_DEV + " TEXT, "
            + COLUMN_Xcoord + " DOUBLE, "
            + COLUMN_Ycoord + " DOUBLE, "
            + COLUMN_LOTS + " INTEGER "
            + ");";
    /**
     *Use the application context, which will ensure that user don't accidentally leak an Activity's context.
     * @param context
     * @return
     */
    public static synchronized CarparkDBController getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new CarparkDBController(context.getApplicationContext());
        }
        return sInstance;
    }

    private Context context;

    /**
     * Object Constructor for CarparkDBController object
     * @param context
     */
    public CarparkDBController(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    /**
     * This method creates database if no database exists.
     * Table for hdb carparks created
     * CSV file containing hdb information is populated into table TABLE_CARPARK
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.i(TAG, "onCreate database!");
        sqLiteDatabase.execSQL(CREATE_TABLE_CARPARK);   //Create parent carpark table
        sqLiteDatabase.execSQL(CREATE_TABLE_HDB_CARPARK); //Create subclass HDB carpark table
        sqLiteDatabase.execSQL(CREATE_TABLE_DATA_MALL); //Create subclass DataMall carparks

        //If more types of carparks are available, just need to define columns and create them here.
        Log.i(TAG, "created table_carparks");
        try {
            //Add data into HDB carpark table and carpark table. Make 1 more method for DM carparks.
            addCSVintoDB(sqLiteDatabase);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    /**
     *
     * @param db
     */
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        Log.i(TAG, "onOpen database!");

    }

    //load values from csv file.

    /**
     * Loads data from the CSV file and add it into the database.
     * The CSV file is currently fixed.
     * Data is retrieved row by row in the CSV file until all entries are loaded
     * @param db The desired database in which the data are to be added into.
     * @throws IOException
     */
    public void addCSVintoDB(SQLiteDatabase db) throws IOException {
        Log.i(TAG,  "addCSVintoDB method");
        String filename = "hdb-carpark-information.csv";
        AssetManager am = context.getAssets();
        InputStream inputStream = null;
        inputStream = am.open(filename);
        String line = "";
        BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream));

        //Adds data from hdb csv file to 2 tables.
        while((line = buffer.readLine()) != null){
            Log.i(TAG, "Adding into Carpark DB");
            String [] columns = line.split(",");

            ContentValues cv1 = new ContentValues();
            cv1.put(COLUMN_Xcoord, columns[2].replace("\"",""));
            cv1.put(COLUMN_Ycoord, columns[3].replace("\"",""));
            cv1.put(COLUMN_OWNER_TYPE, OWNER_HDB);
            db.insert(TABLE_CARPARKS, null, cv1);


            //Doing this is inefficient, but somehow SELECT MAX(COLUMN_IN) doesnt work...
            String query = "SELECT "+ COLUMN_ID + " FROM " + TABLE_CARPARKS + " WHERE 1;";
            Cursor c = db.rawQuery(query,null);
            c.moveToLast();
            int id = c.getInt(c.getColumnIndex(COLUMN_ID));
            c.close();

            ContentValues cv2 = new ContentValues();
            cv2.put(COLUMN_ID, id); //THIS IS THE KEY! How to link the id from carpark table to HDBCarpark
            cv2.put(COLUMN_CARPARKNUM, columns[0].replace("\"",""));
            cv2.put(COLUMN_address, columns[1].replace("\"",""));
            cv2.put(COLUMN_Xcoord, columns[2].replace("\"",""));
            cv2.put(COLUMN_Ycoord, columns[3].replace("\"",""));
            cv2.put(COLUMN_CPTYPE, columns[4].replace("\"",""));
            cv2.put(COLUMN_TYPE_PARKING_SYS, columns[5].replace("\"",""));
            cv2.put(COLUMN_STP, columns[6].replace("\"",""));
            cv2.put(COLUMN_FP, columns[7].replace("\"",""));
            cv2.put(COLUMN_NP, columns[8].replace("\"",""));
            db.insert(TABLE_HDB_CARPARKS, null, cv2);
        }
    }

    //For Data mall carparks and other potential carparks that have live feeds, how to organise information?
    /*
    Each time we pull data down then we create the table to store the data? or we fix all the static values
    then only insert in the non-static values? hmmmmm.....
     */
    public void addDMintoCarparkDB(){

    }

    //To be removed
    public String dbToString(){
        Log.i(TAG, "enter dbToString");
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_HDB_CARPARKS + " WHERE 1;";

        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();
        while(!(c.isAfterLast())){
            if(c.getCount()!=0){
                dbString += " " + c.getString(c.getColumnIndex(COLUMN_ID));
                dbString += " " + c.getString(c.getColumnIndex(COLUMN_CARPARKNUM));
                dbString += " " + c.getString(c.getColumnIndex(COLUMN_address));
                dbString += " " + c.getString(c.getColumnIndex(COLUMN_Xcoord));
                dbString += " " + c.getString(c.getColumnIndex(COLUMN_Ycoord));
            }
            c.moveToNext();
        }

        db.close();
        //c.close();
        Log.i(TAG, "dbString: " + dbString);
        return dbString;

    }
    public String testCarparks(){
        Log.i(TAG, "enter dbToString");
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT "+ COLUMN_ID + " FROM " + TABLE_CARPARKS + " WHERE 1;";

        Cursor c = db.rawQuery(query,null);
        c.moveToLast();
        while(!(c.isAfterLast())){
            if(c.getCount()!=0){
                dbString += " " + c.getString(c.getColumnIndex(COLUMN_ID));
                //dbString += " " + c.getString(c.getColumnIndex(COLUMN_OWNER_TYPE));
                //dbString += " " + c.getString(c.getColumnIndex(COLUMN_Xcoord));
                //dbString += " " + c.getString(c.getColumnIndex(COLUMN_Ycoord));
            }
            c.moveToNext();
        }

        db.close();
        //c.close();
        Log.i(TAG, "dbString: " + dbString);
        return dbString;

    }

    /**
     * This method queries the database to get carparks with coordinates within vicinity of destination
     * Using the coordinates from the input it will compare it with those in the database
     * All entries within a 25KM radius of the carpark will be added into the cursor object cpListInfo
     * If less than 5 entries are found, the range is then increase by 5 km.
     * The range will be increased until 5 or more entries are found.
     * At the end of the method, it returns the cursor object
     * @param svy21C The coordinates of the current destination
     * @return
     */
    //This method queries the database to get carparks with coordinates within vicinity of destination
    public Cursor queryRetrieveNearbyCarparks(SVY21Coordinate svy21C){

        Log.i(TAG, "Enter queryRetrieveNearbyHDBCarparks");
        double easting = svy21C.getEasting();
        double northing = svy21C.getNorthing();
        Cursor cpListInfo = null;


        SQLiteDatabase db = getWritableDatabase();
        String query="";
        //Set boarder range to get carparks
        double boarderRange = 200;
        int count = 0;
        //TEST BOX RANGE FOUND...SO CAN USE TO FIND QUERY!
        try{
            do{
                double xBoxMin = easting - boarderRange;
                double xBoxMax = easting + boarderRange;
                double yBoxMin = northing - boarderRange;
                double yBoxMax = northing + boarderRange;
                query = "SELECT * FROM " + TABLE_CARPARKS +
                        " WHERE " + COLUMN_Xcoord + " BETWEEN " + xBoxMin + " AND " +  xBoxMax
                        + " AND " + COLUMN_Ycoord + " BETWEEN " + yBoxMin + " AND " + yBoxMax;
                cpListInfo = db.rawQuery(query, null);
                boarderRange+=25;
                count ++;

            }while(cpListInfo.getCount() <=5);
        }catch(Exception e){
            e.printStackTrace();
        }
        String tempString = "";
        return cpListInfo;
    }

    public Cursor queryGetHDBCarparkInfo(int id){
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_HDB_CARPARKS + " WHERE " + COLUMN_ID + "=" + id + ";";
        Cursor C = db.rawQuery(query,null);
        return C;
    }


    public Cursor querySomething(){// test method
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_HDB_CARPARKS + " WHERE " + COLUMN_address + " = 'BLK 270/271 ALBERT CENTRE BASEMENT CAR PARK';";
        //String query =  "SELECT * FROM " + TABLE_CARPARKS + " WHERE 1;";
        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();
        if(c.getCount() != 0){
            Log.i(TAG, "cursor has stuff");
            String x = c.getString(c.getColumnIndex(COLUMN_Xcoord));
            Log.i(TAG, "value of x: " + x);
        }
        else{
            Log.i(TAG, "cursor empty");
        }
         return c;
    }




}
