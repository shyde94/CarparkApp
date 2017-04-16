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

    /**
     * This method Returns string containing information such as station name, address and services
     * @return
     */
    @Override
    public String displayInfo() {
        String display = "Station Name: " + StationName + "\n"
                + " Address: " + Address + "\n"
                + " Services: " + Services+ "\n";
        return display;
    }

    /**
     * This method returns station name
     * @return
     */
    @Override
    public String title() {
        return StationName;
    }

    /**
     * This method returns the lat and long coordinates
     * @return
     */
    @Override
    public LatLonCoordinate getLatLonCoord() {
        return latLonCoord;
    }

    /**
     * This method returns the converted coordinates
     * @return
     */
    @Override
    public SVY21Coordinate getSVY21Coord() {
        return svyCoord;
    }

    /**
     * This method Sets the lat and long coordinates
     * @param ll
     */
    @Override
    public void SetLatLonCoord(LatLonCoordinate ll) {
        latLonCoord = ll;
    }

    /**
     * This method sets the converted coordinates
     * @param svy21
     */
    @Override
    public void SetSVY21Coordinate(SVY21Coordinate svy21) {
        svyCoord = svy21;
    }

    /**
     * Class constructor
     * @param svyCoord      Converted conordinates
     * @param latLonCoord  Lat and Long Coordinates
     * @param stationName   Station Name
     * @param address       Station Address
     * @param services       Types of service available
     */
    public PStations(SVY21Coordinate svyCoord, LatLonCoordinate latLonCoord, String stationName, String address, String services) {
        this.svyCoord = svyCoord;
        this.latLonCoord = latLonCoord;
        StationName = stationName;
        Address = address;
        Services = services;
    }
}
