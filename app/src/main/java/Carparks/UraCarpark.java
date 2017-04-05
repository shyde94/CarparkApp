package Carparks;

import MapProjectionConverter.LatLonCoordinate;
import MapProjectionConverter.SVY21Coordinate;

/**
 * Created by Shide on 31/3/17.
 */

public class UraCarpark implements Carpark {
    private SVY21Coordinate svyCoord;
    private LatLonCoordinate latLonCoord;
    private String weekdayMin;
    private String remarks;
    private String carparkName;
    private String carparkCode;
    private String startTime;
    private String endTime;
    private String weekdayRate;
    private String sunPHRate;
    private String sunPHMin;
    private String satdayMin;
    private String satdayRate;
    private String parkingSys;
    private String parkingCap;
    private String vehCat;


    public UraCarpark(SVY21Coordinate svyCoord, LatLonCoordinate latLonCoord, String weekdayMin, String remarks, String carparkName, String carparkCode, String startTime, String endTime, String weekdayRate, String sunPHRate, String sunPHMin, String satdayMin, String satdayRate, String parkingSys, String parkingCap, String vehCat) {
        this.svyCoord = svyCoord;
        this.latLonCoord = latLonCoord;
        this.weekdayMin = weekdayMin;
        this.remarks = remarks;
        this.carparkName = carparkName;
        this.carparkCode = carparkCode;
        this.startTime = startTime;
        this.endTime = endTime;
        this.weekdayRate = weekdayRate;
        this.sunPHRate = sunPHRate;
        this.sunPHMin = sunPHMin;
        this.satdayMin = satdayMin;
        this.satdayRate = satdayRate;
        this.parkingSys = parkingSys;
        this.parkingCap = parkingCap;
        this.vehCat = vehCat;
    }

    @Override
    public String displayInfo() {
        String info = "Weekday rate: " + weekdayRate + "\n"
                + "Parking system: " + parkingSys + "\n"
                + "Start time: " + startTime + "\n"
                + "End time: " + endTime + "\n"
                + "Sun, PH Rate: " + sunPHRate + "\n"
                +"Remarks: " +remarks + "\n";
        return info;
    }

    @Override
    public String title() {
        String title = carparkCode + "\n" + carparkName + "\n" + "Vehicle Cat: " + vehCat;
        return title;
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
        this.latLonCoord = ll;
    }

    @Override
    public void SetSVY21Coordinate(SVY21Coordinate svy21) {
        this.svyCoord = svy21;
    }
}
