package com.example.android.carparkappv1;

import com.google.android.gms.maps.model.LatLng;

import MapProjectionConverter.LatLonCoordinate;
import MapProjectionConverter.SVY21Coordinate;

public class Carpark {
    private SVY21Coordinate svyCoord;
    private LatLonCoordinate latLonCoord;
    private String cpNum;
    private String cpType;
    private String typeOfParkingSystem;
    private String shortTermParking;
    private String freeParking;
    private String nightParking;
    private String address;

    public Carpark(){
    }

    public Carpark(SVY21Coordinate svyCoord, LatLonCoordinate latLonCoord, String cpNum, String cpType, String typeOfParkingSystem, String shortTermParking, String freeParking, String nightParking, String address) {
        this.svyCoord = svyCoord;
        this.latLonCoord = latLonCoord;
        this.cpNum = cpNum;
        this.cpType = cpType;
        this.typeOfParkingSystem = typeOfParkingSystem;
        this.shortTermParking = shortTermParking;
        this.freeParking = freeParking;
        this.nightParking = nightParking;
        this.address = address;
    }

    public SVY21Coordinate getSvyCoord() {
        return svyCoord;
    }

    public void setSvyCoord(SVY21Coordinate svyCoord) {
        this.svyCoord = svyCoord;
    }

    public LatLonCoordinate getLatLonCoord() {
        return latLonCoord;
    }

    public void setLatLonCoord(LatLonCoordinate latLonCoord) {
        this.latLonCoord = latLonCoord;
    }

    public String getCpNum() {
        return cpNum;
    }

    public void setCpNum(String cpNum) {
        this.cpNum = cpNum;
    }

    public String getCpType() {
        return cpType;
    }

    public void setCpType(String cpType) {
        this.cpType = cpType;
    }

    public String getTypeOfParkingSystem() {
        return typeOfParkingSystem;
    }

    public void setTypeOfParkingSystem(String typeOfParkingSystem) {
        this.typeOfParkingSystem = typeOfParkingSystem;
    }

    public String getShortTermParking() {
        return shortTermParking;
    }

    public void setShortTermParking(String shortTermParking) {
        this.shortTermParking = shortTermParking;
    }

    public String getFreeParking() {
        return freeParking;
    }

    public void setFreeParking(String freeParking) {
        this.freeParking = freeParking;
    }

    public String getNightParking() {
        return nightParking;
    }

    public void setNightParking(String nightParking) {
        this.nightParking = nightParking;
    }

    public String getAddress(){ return address; }

    public void setAddress(String address){ this.address = address; }

}


//"car_park_no","address","x_coord","y_coord","car_park_type","type_of_parking_system","short_term_parking","free_parking","night_parking"