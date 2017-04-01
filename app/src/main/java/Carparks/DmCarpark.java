package Carparks;

import MapProjectionConverter.LatLonCoordinate;
import MapProjectionConverter.SVY21Coordinate;

/**
 * Created by Shide on 31/3/17.
 */

public class DmCarpark implements Carpark {
    private SVY21Coordinate svyCoord;
    private LatLonCoordinate latLonCoord;
    private String CarparkNum;
    private String Area;
    private String Dev;
    private int lots;


    public DmCarpark(SVY21Coordinate svyCoord, LatLonCoordinate latLonCoord, String carparkNum, String area, String dev, int lots) {
        this.svyCoord = svyCoord;
        this.latLonCoord = latLonCoord;
        this.CarparkNum = carparkNum;
        this.Area = area;
        this.Dev = dev;
        this.lots = lots;
    }

    @Override
    public String displayInfo() {
        String numLots = "Available lots: " + lots;
        return numLots;
    }

    @Override
    public LatLonCoordinate getLatLonCoord() {
        return this.latLonCoord;
    }

    @Override
    public SVY21Coordinate getSVY21Coord() {
        return this.svyCoord;
    }

    @Override
    public void SetLatLonCoord(LatLonCoordinate ll) {
        latLonCoord = ll;
    }

    @Override
    public void SetSVY21Coordinate(SVY21Coordinate svy21) {
        svyCoord = svy21;
    }

    @Override
    public String title() {
        String title = Area + "\n" + Dev;
        return title;
    }
}

