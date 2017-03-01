package com.example.android.carparkappv1;


import android.content.Context;

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



    public ArrayList<Carpark> retrieveCarparks(){
        ArrayList<Carpark> cpObjectArray = new ArrayList<Carpark>();

        return cpObjectArray;
    }






    //Convert WSG84 (destination coordinates) to SVY21 to query database which is in SVY21
    public SVY21Coordinate getSVY21Coord(LatLng d){
        SVY21Coordinate svyC = SVY21.computeSVY21(destination.latitude, destination.longitude);
        return svyC;
    }


    //Convert SVY21 to WSG84 (to be displayed on google map api)
    public LatLonCoordinate getLatLonCoord(double N, double E){
        LatLonCoordinate llC = SVY21.computeLatLon(N,E);
        return llC;
    }


}
