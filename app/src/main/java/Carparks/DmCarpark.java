package Carparks;

import MapProjectionConverter.LatLonCoordinate;
import MapProjectionConverter.SVY21Coordinate;

/**
 * Instances of this class represent Datamall Carparks
 */

public class DmCarpark implements Carpark {
    private SVY21Coordinate svyCoord;
    private LatLonCoordinate latLonCoord;
    private String CarparkNum;
    private String Area;
    private String Dev;
    private int lots;

    /**
     *
     * @param svyCoord svyCoord object that stores eastings and northings of carpark
     * @param latLonCoord LatlonCoord that stores latitude and longitude of carpark
     * @param carparkNum Represents carpark number given by Datamall
     * @param area Area of development / building
     * @param dev Shopping mall or major landmark where carpark is located
     * @param lots Number of lots available
     */
    public DmCarpark(SVY21Coordinate svyCoord, LatLonCoordinate latLonCoord, String carparkNum, String area, String dev, int lots) {
        this.svyCoord = svyCoord;
        this.latLonCoord = latLonCoord;
        this.CarparkNum = carparkNum;
        this.Area = area;
        this.Dev = dev;
        this.lots = lots;
    }

    /**
     * Display number of available lots
     * @return number of lots
     */
    @Override
    public String displayInfo() {
        String numLots = "Available lots: " + lots;
        return numLots;
    }

    /**
     * Get latitude and longitude coordinates
     * @return latitude and longitude coordinates
     */
    @Override
    public LatLonCoordinate getLatLonCoord() {
        return this.latLonCoord;
    }

    /**
     * Get eastings and northings coordinates
     * @return eastings and northings coordinates
     */
    @Override
    public SVY21Coordinate getSVY21Coord() {
        return this.svyCoord;
    }

    /**
     * Set latitude and longitude coordinates
     * @param ll latitude and longitude coordinates
     */
    @Override
    public void SetLatLonCoord(LatLonCoordinate ll) {
        latLonCoord = ll;
    }

    /**
     * Set eastings and northings coordinates
     * @param svy21 eastings and northings coordinate
     */
    @Override
    public void SetSVY21Coordinate(SVY21Coordinate svy21) {
        svyCoord = svy21;
    }

    /**
     * Title is used to display the Area(Eg: Orchard) and Type of development(Eg: The Cathay)
     * @return title to be used to display inside infowindow of markers
     */
    @Override
    public String title() {
        String title = Area + "\n" + Dev;
        return title;
    }
}

