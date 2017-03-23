package com.example.android.carparkappv1;


import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import Controllers.CarparkDBController;
import Controllers.ScreenController;
import MapProjectionConverter.LatLonCoordinate;
import MapProjectionConverter.SVY21;
import MapProjectionConverter.SVY21Coordinate;

public class CarparkFinder {
    private static final String TAG = "CarparkFinderClass";

    private ArrayList<Carpark> cpList; //this list contains the carparks found from the database base on the destination!
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
    public CarparkFinder(LatLng ll, Context context){
        Log.i(TAG, "Created new CarparkFinder object");
        destination = ll;
        this.context = context;
        this.cpController = new CarparkDBController(context);
        cpList = new ArrayList<Carpark>();
    }


    /**
     * Take destination, convert into SVYCoordinate, call database query method, converts strings to give objects.
     * @return Arraylist<Carpark>
     */
    public void retrieveCarparks(){
        Log.i(TAG, "Enter retrieve Carparks");
        ArrayList<Carpark> cpObjectArray = new ArrayList<Carpark>();
        SVY21Coordinate temp = getSVY21Coord(destination);

        //CursorList will contain every row of carpark within the vicinity
        Cursor cursorList = cpController.queryRetrieveNearbyCarparks(temp);
        cursorList.moveToFirst();
        while(cursorList.isAfterLast() == false){
            //Create carpark object containing data from each row!
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
            Carpark carparkTemp = new Carpark(svy21, latLon, cpNum, cpType, typeOfParkingSystem, shortTermParking, freeParking, nightParking, address);
            cpObjectArray.add(carparkTemp);

            cursorList.moveToNext();

            Log.i(TAG, "Carpark: " + cpNum + " " + svy21.getNorthing() + " " + svy21.getEasting());
        }
        cpList = cpObjectArray;
    }






    //Convert WSG84 (destination coordinates) to SVY21 to query database which is in SVY21
    public SVY21Coordinate getSVY21Coord(LatLng d){
        SVY21Coordinate svyC = SVY21.computeSVY21(d.latitude, d.longitude);
        return svyC;
    }


    //Convert SVY21 to WSG84 (to be displayed on google map api)
    public LatLonCoordinate getLatLonCoord(double N, double E){
        LatLonCoordinate llC = SVY21.computeLatLon(N,E);
        return llC;
    }


    public Carpark cursorToCarpark(Cursor c){
        Carpark cp = new Carpark();
        SVY21Coordinate svy21C = new SVY21Coordinate(c.getDouble(c.getColumnIndex(CarparkDBController.COLUMN_Xcoord)), c.getDouble(c.getColumnIndex(CarparkDBController.COLUMN_Ycoord)));
        cp.setCpNum(c.getString(c.getColumnIndex(CarparkDBController.COLUMN_CARPARKNUM)));
        cp.setSvyCoord(svy21C);
        return cp;
    }

    public ArrayList<LatLonCoordinate> handleQuery(){
        ArrayList<LatLonCoordinate> tempList = new ArrayList<LatLonCoordinate>();
        Cursor c = cpController.querySomething();
        c.moveToFirst();
        if (c.getCount() == 0) {
            Log.i(TAG, "c count = 0");
            return null;
        } else {
            while (c.moveToNext()) {
                Log.i(TAG, "enter while loop");
                String xCoordS = c.getString(c.getColumnIndex(CarparkDBController.COLUMN_Xcoord));
                String yCoordS = c.getString(c.getColumnIndex(CarparkDBController.COLUMN_Ycoord));
                Log.i(TAG, "xCoord: " + xCoordS + "yCoord: " + yCoordS);
                xCoordS = xCoordS.substring(1,xCoordS.length()-1);
                yCoordS = yCoordS.substring(1,yCoordS.length()-1);
                double xCoord = Double.parseDouble(xCoordS);
                double yCoord = Double.parseDouble(yCoordS);
                LatLonCoordinate llc = SVY21.computeLatLon(yCoord, xCoord);
                tempList.add(llc);
            }
        }
        c.close();
        return tempList;

    }



}
