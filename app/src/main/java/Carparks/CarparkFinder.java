package Carparks;


import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.Switch;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import Carparks.Carpark;
import Carparks.HdbCarpark;
import Controllers.CarparkDBController;
import MapProjectionConverter.LatLonCoordinate;
import MapProjectionConverter.SVY21;
import MapProjectionConverter.SVY21Coordinate;

public class CarparkFinder implements ObjectAccessInterface {
    private static final String TAG = "CarparkFinderClass";

    private ArrayList<Carpark> cpList = new ArrayList<>(); //this list contains the carparks found from the database base on the destination!
    private LatLng destination;
    private CarparkDBController cpController;


    private Context context;

    public CarparkDBController getCpController() {
        return cpController;
    }

    //Getters and setters
    public LatLng getDestination() {
        return destination;
    }

    public void setDestination(LatLng destination) {
        this.destination = destination;
    }

    public ArrayList<Carpark> getCpList() {
        return cpList;
    }

    public void setCpList(ArrayList<Carpark> cpList) {
        this.cpList = cpList;
    }


    //Constructor
    public CarparkFinder(LatLng ll, Context context) {
        Log.i(TAG, "Created new CarparkFinder object");
        destination = ll;
        this.context = context;
        this.cpController = new CarparkDBController(context);
    }

    public CarparkFinder(){
    }


    /**
     * Take destination, convert into SVYCoordinate, call database query method, converts strings to give objects.
     *
     * @return Arraylist<Carpark>
     */
    public void retrieveCarparks() {
        Log.i(TAG, "Enter retrieve Carparks");
        SVY21Coordinate temp = getSVY21Coord(destination);

        //CursorList will contain every row of carpark within the vicinity
        Cursor cursorList = cpController.queryRetrieveNearbyCarparks(temp);
        cursorList.moveToFirst();
        while (cursorList.isAfterLast() == false) {
            //Create carpark object containing data from each row!
            /*double easting = cursorList.getDouble(cursorList.getColumnIndex(CarparkDBController.COLUMN_Xcoord));
            double northing = cursorList.getDouble(cursorList.getColumnIndex(CarparkDBController.COLUMN_Ycoord));

            SVY21Coordinate svy21 = new SVY21Coordinate(northing, easting);
            LatLonCoordinate latLon = SVY21.computeLatLon(svy21);*/
            String cpOwner = cursorList.getString(cursorList.getColumnIndex(CarparkDBController.COLUMN_OWNER_TYPE));
            int id = cursorList.getInt(cursorList.getColumnIndex(CarparkDBController.COLUMN_ID));
            ObjectCreater(cpOwner, id);
            cursorList.moveToNext();


            //Log.i(TAG, "Carpark: " + cpNum + " " + svy21.getNorthing() + " " + svy21.getEasting());

        }
    }

    public void ObjectCreater(String owner, int id) {
        switch (owner) {
            case CarparkDBController.OWNER_HDB:
                createHDBCarparkObjects(id);
                break;
            case CarparkDBController.OWNER_DM:
                createDMCarparkObject(id);
                break;
            case CarparkDBController.OWNER_URA:
                createURACarparkObject(id);
            default:
                Log.i(TAG, "Unidentified carpark");
        }
    }

    public void createHDBCarparkObjects(int id) {
        Log.i(TAG, "Creating HDB Carpark Objects");
        Cursor cursorList = cpController.queryGetCarparkInfo(id, CarparkDBController.TABLE_HDB_CARPARKS);
        cursorList.moveToFirst();
        double easting = cursorList.getDouble(cursorList.getColumnIndex(CarparkDBController.COLUMN_Xcoord));
        double northing = cursorList.getDouble(cursorList.getColumnIndex(CarparkDBController.COLUMN_Ycoord));

        SVY21Coordinate svy21 = new SVY21Coordinate(northing, easting);
        LatLonCoordinate latLon = SVY21.computeLatLon(svy21);
        String cpNum = cursorList.getString(cursorList.getColumnIndex(CarparkDBController.COLUMN_CARPARKNUM));
        String cpType = cursorList.getString(cursorList.getColumnIndex(CarparkDBController.COLUMN_CPTYPE));
        String typeOfParkingSystem = cursorList.getString(cursorList.getColumnIndex(CarparkDBController.COLUMN_TYPE_PARKING_SYS));
        String shortTermParking = cursorList.getString(cursorList.getColumnIndex(CarparkDBController.COLUMN_TYPE_PARKING_SYS));
        String freeParking = cursorList.getString(cursorList.getColumnIndex(CarparkDBController.COLUMN_FP));
        String nightParking = cursorList.getString(cursorList.getColumnIndex(CarparkDBController.COLUMN_NP));
        String address = cursorList.getString(cursorList.getColumnIndex(CarparkDBController.COLUMN_address));
        Carpark carparkTemp = new HdbCarpark(svy21, latLon, cpNum, cpType, typeOfParkingSystem, shortTermParking, freeParking, nightParking, address);
        Log.i(TAG, "Carpark: " + cpNum + " " + svy21.getNorthing() + " " + svy21.getEasting());
        cpList.add(carparkTemp);

        cursorList.close();
    }


    public void createDMCarparkObject(int id) {
        Log.i(TAG, "Creating DMCarpark Objects");
        Cursor cursorList = cpController.queryGetCarparkInfo(id, CarparkDBController.TABLE_DATAMALL_CARPARK);
        cursorList.moveToFirst();
        double easting = cursorList.getDouble(cursorList.getColumnIndex(CarparkDBController.COLUMN_Xcoord));
        double northing = cursorList.getDouble(cursorList.getColumnIndex(CarparkDBController.COLUMN_Ycoord));
        SVY21Coordinate svy21 = new SVY21Coordinate(northing, easting);
        LatLonCoordinate latLon = SVY21.computeLatLon(svy21);
        String CarparkNum = cursorList.getString(cursorList.getColumnIndex(CarparkDBController.COLUMN_CARPARKNUM));
        String Area = cursorList.getString(cursorList.getColumnIndex(CarparkDBController.COLUMN_AREA));
        String Dev = cursorList.getString(cursorList.getColumnIndex(CarparkDBController.COLUMN_DEV));
        int lots = cursorList.getInt(cursorList.getColumnIndex(CarparkDBController.COLUMN_LOTS));
        Log.i(TAG, "Carpark: " + CarparkNum + " " + svy21.getNorthing() + " " + svy21.getEasting() + " Lots: " + lots);
        Carpark carparkTemp = new DmCarpark(svy21, latLon, CarparkNum, Area, Dev, lots);
        cpList.add(carparkTemp);
        cursorList.close();
    }


    public void createURACarparkObject(int id) {
        Log.i(TAG, "Creating URA Carpark Objects");
        Cursor cursorList = cpController.queryGetCarparkInfo(id, CarparkDBController.TABLE_URA_CARPARK);
        cursorList.moveToFirst();
        double easting = cursorList.getDouble(cursorList.getColumnIndex(CarparkDBController.COLUMN_Xcoord));
        double northing = cursorList.getDouble(cursorList.getColumnIndex(CarparkDBController.COLUMN_Ycoord));
        SVY21Coordinate svy21 = new SVY21Coordinate(northing, easting);
        LatLonCoordinate latLon = SVY21.computeLatLon(svy21);
        String weekdayMin = cursorList.getString(cursorList.getColumnIndex(CarparkDBController.COLUMN_WKDAYMIN));
        String remarks = cursorList.getString(cursorList.getColumnIndex(CarparkDBController.COLUMN_REMARKS));
        String carparkName = cursorList.getString(cursorList.getColumnIndex(CarparkDBController.COLUMN_CPNAME));
        String carparkCode = cursorList.getString(cursorList.getColumnIndex(CarparkDBController.COLUMN_CPCODE));
        String startTime = cursorList.getString(cursorList.getColumnIndex(CarparkDBController.COLUMN_STARTTIME));
        String endTime = cursorList.getString(cursorList.getColumnIndex(CarparkDBController.COLUMN_ENDTIME));
        String weekdayRate = cursorList.getString(cursorList.getColumnIndex(CarparkDBController.COLUMN_WKDAYRATE));
        String sunPHRate = cursorList.getString(cursorList.getColumnIndex(CarparkDBController.COLUMN_SUNPHRATE));
        String sunPHMin = cursorList.getString(cursorList.getColumnIndex(CarparkDBController.COLUMN_SUNPHMIN));
        String satdayMin = cursorList.getString(cursorList.getColumnIndex(CarparkDBController.COLUMN_SATDAYMIN));
        String satdayRate = cursorList.getString(cursorList.getColumnIndex(CarparkDBController.COLUMN_SATDAYRATE));
        String parkingSys = cursorList.getString(cursorList.getColumnIndex(CarparkDBController.COLUMN_PARKINGSYS));
        String parkingCap = cursorList.getString(cursorList.getColumnIndex(CarparkDBController.COLUMN_PARKCAP));
        String vehCat = cursorList.getString(cursorList.getColumnIndex(CarparkDBController.COLUMN_VEHCAT));

        Carpark carparkTemp = new UraCarpark(svy21, latLon, weekdayMin, remarks, carparkName, carparkCode, startTime, endTime, weekdayRate, sunPHRate, sunPHMin, satdayMin, satdayRate, parkingSys, parkingCap, vehCat);
        Log.i(TAG, "URA, Carpark: " + carparkCode + " " + svy21.getNorthing() + " " + svy21.getEasting());
        cpList.add(carparkTemp);
        cursorList.close();
    }


    //Convert WSG84 (destination coordinates) to SVY21 to query database which is in SVY21
    public SVY21Coordinate getSVY21Coord(LatLng d) {
        SVY21Coordinate svyC = SVY21.computeSVY21(d.latitude, d.longitude);
        return svyC;
    }


    //Convert SVY21 to WSG84 (to be displayed on google map api)
    public LatLonCoordinate getLatLonCoord(double N, double E) {
        LatLonCoordinate llC = SVY21.computeLatLon(N, E);
        return llC;
    }


    /*public Carpark cursorToCarpark(Cursor c){
        Carpark cp = new HdbCarpark();
        SVY21Coordinate svy21C = new SVY21Coordinate(c.getDouble(c.getColumnIndex(CarparkDBController.COLUMN_Xcoord)), c.getDouble(c.getColumnIndex(CarparkDBController.COLUMN_Ycoord)));
        cp.setCpNum(c.getString(c.getColumnIndex(CarparkDBController.COLUMN_CARPARKNUM)));
        cp.setSvyCoord(svy21C);
        return cp;
    }*/


}
