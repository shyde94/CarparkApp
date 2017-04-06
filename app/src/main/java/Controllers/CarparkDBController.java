package Controllers;


import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import MapProjectionConverter.SVY21Coordinate;

public class CarparkDBController extends SQLiteOpenHelper {

    private static CarparkDBController mInstance;

    private static final String TAG = "CarparkDBControllerClass";

    public static final String OWNER_HDB = "HDB";
    public static final String OWNER_DM = "DM";
    public static final String OWNER_SM = "SM";
    public static final String OWNER_URA = "URA";
    /*
    ER diagram approach.
    Carpark entity is the parent entity, subclass entity : HdbCarpark, SmCarpark
    Create static final string for parent entity Carpark

     */
    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Carpark.db";

    /////////////////////////////////////////////////////////////////////////////////////////////////////////
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
    /////////////////////////////////////////////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////////////////////////////////////////////
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
    /////////////////////////////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static final String TABLE_DATAMALL_CARPARK = "DMCarpark";
    public static final String COLUMN_AREA = "Area";
    public static final String COLUMN_DEV = "Development";
    public static final String COLUMN_LOTS = "Lots";

    public static final String CREATE_TABLE_DATA_MALL = "CREATE TABLE " + TABLE_DATAMALL_CARPARK + " ( "
            + COLUMN_ID + " INTEGER, "
            + COLUMN_CARPARKNUM + " TEXT, "
            + COLUMN_AREA + " TEXT, "
            + COLUMN_DEV + " TEXT, "
            + COLUMN_Xcoord + " DOUBLE, "
            + COLUMN_Ycoord + " DOUBLE, "
            + COLUMN_LOTS + " INTEGER "
            + ");";
    /////////////////////////////////////////////////////////////////////////////////////////////////////////

    /*
    "weekdayMin","geometries__coordinates","remarks","ppName","endTime","weekdayRate","startTime",
    "ppCode","sunPHRate","satdayMin","sunPHMin","parkingSystem","parkCapacity","vehCat","satdayRate"

    Columns for URA Carpark
     */
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static final String TABLE_URA_CARPARK = "URACarpark";
    public static final String COLUMN_WKDAYMIN = "WeekdayMin";
    public static final String COLUMN_REMARKS = "Remarks";
    public static final String COLUMN_CPNAME = "CpName";
    public static final String COLUMN_CPCODE = "CpCode";
    public static final String COLUMN_STARTTIME = "StartTime";
    public static final String COLUMN_ENDTIME = "EndTime";
    public static final String COLUMN_WKDAYRATE = "WeekdayRate";
    public static final String COLUMN_SUNPHRATE = "SunPHRate";
    public static final String COLUMN_SUNPHMIN = "SunPHMin";
    public static final String COLUMN_PARKINGSYS = "ParkingSystem";
    public static final String COLUMN_PARKCAP = "ParkCapacity";
    public static final String COLUMN_VEHCAT = "VehCat";
    public static final String COLUMN_SATDAYMIN = "SatDayMin";
    public static final String COLUMN_SATDAYRATE = "SatDayRate";


    public static final String CREATE_TABLE_URACARPARK = "CREATE TABLE " + TABLE_URA_CARPARK + " ( "
            + COLUMN_ID + " INTEGER, "
            + COLUMN_Xcoord + " DOUBLE, "
            + COLUMN_Ycoord + " DOUBLE, "
            + COLUMN_CPNAME + " TEXT, "
            + COLUMN_CPCODE + " TEXT, "
            + COLUMN_STARTTIME + " TEXT, "
            + COLUMN_ENDTIME + " TEXT, "
            + COLUMN_WKDAYMIN + " TEXT, "
            + COLUMN_WKDAYRATE + " TEXT, "
            + COLUMN_SUNPHRATE + " TEXT, "
            + COLUMN_SUNPHMIN + " TEXT, "
            + COLUMN_SATDAYMIN + " TEXT, "
            + COLUMN_SATDAYRATE + " TEXT, "
            + COLUMN_PARKINGSYS + " TEXT, "
            + COLUMN_PARKCAP + " TEXT, "
            + COLUMN_VEHCAT + " TEXT, "
            + COLUMN_REMARKS + " TEXT "
            + ");";

    //Station Name,Address,Tel. No.,Available Services

    public static final String TABLE_SHELL_STATIONS = "ShellStations";
    public static final String COLUMN_STATION_NAME= "StationName";
    public static final String COLUMN_ADDRESS= "Address";
    public static final String COLUMN_TEL= "Tel";
    public static final String COLUMN_SERVICES= "Services";
    public static final String COLUMN_COMPANY = "Company";

    public static final String CREATE_TABLE_SHELL = "CREATE TABLE " + TABLE_SHELL_STATIONS + " ( "
            + COLUMN_ID + " INTEGER AUTO INCREMENT, "
            + COLUMN_STATION_NAME + " TEXT, "
            + COLUMN_ADDRESS + " TEXT, "
            + COLUMN_TEL + " TEXT, "
            + COLUMN_SERVICES + " TEXT, "
            + COLUMN_COMPANY + " TEXT,"
            + COLUMN_Xcoord + " DOUBLE, "
            + COLUMN_Ycoord + " DOUBLE"
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
        if (mInstance == null) {
            mInstance = new CarparkDBController(context.getApplicationContext());
        }
        return mInstance;
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
        sqLiteDatabase.execSQL(CREATE_TABLE_URACARPARK);
        sqLiteDatabase.execSQL(CREATE_TABLE_SHELL);

        //If more types of carparks are available, just need to define columns and create them here.
        Log.i(TAG, "created table_carparks");
        try {
            //Add data into HDB carpark table and carpark table. Make 1 more method for DM carparks.
            addCSVintoDB(sqLiteDatabase);
            addDMintoCarparkDB(sqLiteDatabase);
            addURAintoCarparkDB(sqLiteDatabase);
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
    public void addDMintoCarparkDB(SQLiteDatabase db) throws IOException {
        Log.i(TAG, "adding DM table");
        String filename = "dm_carpark.csv";
        AssetManager am = context.getAssets();
        InputStream inputStream = null;
        inputStream = am.open(filename);
        String line = "";
        BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream));
        while((line = buffer.readLine()) != null){
            Log.i(TAG, "Adding into Carpark DB");
            String [] columns = line.split(",");

            ContentValues cv1 = new ContentValues();
            cv1.put(COLUMN_Xcoord, columns[3].replace("\"",""));    //column[3] represents easting coordinates of carpark
            cv1.put(COLUMN_Ycoord, columns[4].replace("\"",""));    //column[4] represents northing coordinates of carpark
            cv1.put(COLUMN_OWNER_TYPE, OWNER_DM);
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
            cv2.put(COLUMN_AREA, columns[1].replace("\"",""));
            cv2.put(COLUMN_DEV, columns[2].replace("\"",""));
            cv2.put(COLUMN_Xcoord, columns[3].replace("\"",""));
            cv2.put(COLUMN_Ycoord, columns[4].replace("\"",""));
            db.insert(TABLE_DATAMALL_CARPARK, null, cv2);
        }

    }

    public void addURAintoCarparkDB(SQLiteDatabase db) throws IOException {
        Log.i(TAG, "adding DM table");
        String filename = "ura_carpark.csv";
        AssetManager am = context.getAssets();
        InputStream inputStream = null;
        inputStream = am.open(filename);
        String line = "";
        BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream));
        while((line = buffer.readLine()) != null){
            Log.i(TAG, "Adding into URA carpark CarparkDB");
            Log.i(TAG, line);
            String [] columns = line.split(",");

            ContentValues cv1 = new ContentValues();
            cv1.put(COLUMN_Xcoord, columns[1].replace("\"",""));    //column[1] represents easting coordinates of carpark
            cv1.put(COLUMN_Ycoord, columns[2].replace("\"",""));    //column[2] represents northing coordinates of carpark
            cv1.put(COLUMN_OWNER_TYPE, OWNER_URA);
            db.insert(TABLE_CARPARKS, null, cv1);


            //Doing this is inefficient, but somehow SELECT MAX(COLUMN_IN) doesnt work...
            String query = "SELECT "+ COLUMN_ID + " FROM " + TABLE_CARPARKS + " WHERE 1;";
            Cursor c = db.rawQuery(query,null);
            c.moveToLast();
            int id = c.getInt(c.getColumnIndex(COLUMN_ID));
            c.close();

            ContentValues cv2 = new ContentValues();
            cv2.put(COLUMN_ID, id); //THIS IS THE KEY! How to link the id from carpark table to HDBCarpark
            cv2.put(COLUMN_WKDAYMIN, columns[0].replace("\"",""));
            cv2.put(COLUMN_Xcoord, columns[1].replace("\"",""));
            cv2.put(COLUMN_Ycoord, columns[2].replace("\"",""));
            cv2.put(COLUMN_REMARKS, columns[3].replace("\"",""));
            cv2.put(COLUMN_CPNAME, columns[4].replace("\"",""));
            cv2.put(COLUMN_ENDTIME, columns[5].replace("\"",""));
            cv2.put(COLUMN_WKDAYRATE, columns[6].replace("\"",""));
            cv2.put(COLUMN_STARTTIME, columns[7].replace("\"",""));
            cv2.put(COLUMN_CPCODE, columns[8].replace("\"",""));
            cv2.put(COLUMN_SUNPHRATE, columns[9].replace("\"",""));
            cv2.put(COLUMN_SATDAYMIN, columns[10].replace("\"",""));
            cv2.put(COLUMN_SUNPHMIN, columns[11].replace("\"",""));
            cv2.put(COLUMN_PARKINGSYS, columns[12].replace("\"",""));
            cv2.put(COLUMN_PARKCAP, columns[13].replace("\"",""));
            cv2.put(COLUMN_VEHCAT, columns[14].replace("\"",""));
            //cv2.put(COLUMN_SATDAYRATE, columns[15].replace("\"",""));

            db.insert(TABLE_URA_CARPARK, null, cv2);
        }
    }

    

    //To be removed
    public String dbToString(){
        Log.i(TAG, "enter dbToString");
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_URA_CARPARK + " WHERE 1;";

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
        String query = "SELECT * "+" FROM " + TABLE_URA_CARPARK + " WHERE 1;";

        Cursor c = db.rawQuery(query,null);
        c.moveToLast();
        while(!(c.isAfterLast())){
            if(c.getCount()!=0){

                dbString += "Column_id: " + c.getString(c.getColumnIndex(COLUMN_ID));
                dbString += "Column_xcoord: " + c.getString(c.getColumnIndex(COLUMN_Xcoord));
                dbString += "Column_ycoord: " + c.getString(c.getColumnIndex(COLUMN_Ycoord));
            }
            c.moveToNext();
            Log.i(TAG, "dbString: " + dbString);
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

        Log.i(TAG, "Enter queryRetrieveNearbyCarparks");
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


    public Cursor queryGetCarparkInfo(int id, String TableName){
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TableName + " WHERE " + COLUMN_ID + "=" + id + ";";
        Cursor C = db.rawQuery(query,null);
        return C;
    }

    public void queryUpdateTableDMCarparkLots(String cpNum, int lots){
        Log.i(TAG, "Update number of lots in DM Carpark. " + "Lots: " + lots);
        SQLiteDatabase db = getWritableDatabase();
        String query = "UPDATE " + TABLE_DATAMALL_CARPARK + " SET "+ COLUMN_LOTS + "=" + lots+" WHERE " + COLUMN_CARPARKNUM + " = " + cpNum + ";";
        db.execSQL(query);
    }


}
