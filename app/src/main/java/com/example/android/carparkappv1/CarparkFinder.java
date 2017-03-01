package com.example.android.carparkappv1;


import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class CarparkFinder {
    private ArrayList<Carpark> cpList;
    private LatLng destination;

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



    public CarparkFinder(LatLng ll){
        destination = ll;
        cpList = new ArrayList<Carpark>();
    }

    /*
    public ArrayList<Carpark> retrieveCarparks(){

    }*/


}
