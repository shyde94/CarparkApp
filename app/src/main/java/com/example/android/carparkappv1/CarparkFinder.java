package com.example.android.carparkappv1;


import android.content.Context;
import android.database.Cursor;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import MapProjectionConverter.LatLonCoordinate;
import MapProjectionConverter.SVY21;
import MapProjectionConverter.SVY21Coordinate;

public class CarparkFinder {
    private ArrayList<Carpark> cpList;
    private LatLng destination;
    private CarparkDBController cpController;
    private Context context;


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
    public CarparkFinder(LatLng ll, CarparkDBController cpController, Context context){
        destination = ll;
        this.context = context;
        this.cpController = new CarparkDBController(context);
        cpList = new ArrayList<Carpark>();
    }


    /**
     * Take destination, convert into SVYCoordinate, call database query method, converts strings to give objects.
     * @return Arraylist<Carpark>
     */
    public ArrayList<Carpark> retrieveCarparks(){
        ArrayList<Carpark> cpObjectArray = new ArrayList<Carpark>();
        SVY21Coordinate temp = getSVY21Coord(destination);
        ArrayList<Cursor> cursorList = cpController.queryRetrieveNearbyCarparks(temp);
        //1. Define box to search for carparks within...
        // meaning you get 4 points as markers and search for any 2 points whose
        // coordinates fall inside this boundary.
        // Take first 2 digits of Easting and Northing? +5 -5?

        //TODO 1. How to determine the boundary box?
        //TODO 2. How to query for data based on the boundary box? THINKKKKKKK

        return cpObjectArray;
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

}
