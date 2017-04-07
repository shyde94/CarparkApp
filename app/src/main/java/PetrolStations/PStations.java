package PetrolStations;

import Carparks.Carpark;
import MapProjectionConverter.LatLonCoordinate;
import MapProjectionConverter.SVY21Coordinate;

/**
 * Created by Shide on 6/4/17.
 */

public class PStations implements Carpark {
    private SVY21Coordinate svyCoord;
    private LatLonCoordinate latLonCoord;
    private String StationName;
    private String Address;
    private String Services;

    @Override
    public String displayInfo() {
        String display = "Station Name: " + StationName + "\n"
                + " Address: " + Address + "\n"
                + " Services: " + Services+ "\n";
        return display;
    }

    @Override
    public String title() {
        return StationName;
    }

    @Override
    public LatLonCoordinate getLatLonCoord() {
        return latLonCoord;
    }

    @Override
    public SVY21Coordinate getSVY21Coord() {
        return svyCoord;
    }

    @Override
    public void SetLatLonCoord(LatLonCoordinate ll) {
        latLonCoord = ll;
    }

    @Override
    public void SetSVY21Coordinate(SVY21Coordinate svy21) {
        svyCoord = svy21;
    }

    public PStations(SVY21Coordinate svyCoord, LatLonCoordinate latLonCoord, String stationName, String address, String services) {
        this.svyCoord = svyCoord;
        this.latLonCoord = latLonCoord;
        StationName = stationName;
        Address = address;
        Services = services;
    }
}
