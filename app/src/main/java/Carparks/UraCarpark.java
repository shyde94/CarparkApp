package Carparks;

import MapProjectionConverter.LatLonCoordinate;
import MapProjectionConverter.SVY21Coordinate;

/**
 * Instances of this class represent URA Carparks
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

    /**
     *
     * @param svyCoord svyCoord object that stores eastings and northings of carpark
     * @param latLonCoord LatlonCoord that stores latitude and longitude of carpark
     * @param weekdayMin The maximum duration of the rate for weekday. (Eg: weekdayRate per weekdayMin)
     * @param remarks Remarks for the carpark
     * @param carparkName Name of carpark
     * @param carparkCode Code of carpark
     * @param startTime Effective start time of parking rate
     * @param endTime Effective end time of parking rate
     * @param weekdayRate Carpark rates on Weekday
     * @param sunPHRate Carpark rates on Sunday and Public Holidays
     * @param sunPHMin The maximum duration of the rate on Sunday and Public Holidays
     * @param satdayMin The maximum duration of the rate on Saturday
     * @param satdayRate Carpark rates on Saturday
     * @param parkingSys The type of parking system the car park is in use: "C"-Coupon "B"-Electronic
     * @param parkingCap Number of carpark lots
     * @param vehCat Vehicle Category: "C"-Car, "M"-Motorcycle, "H"-Heavy Vehicle
     */

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

    /**
     * Relevant data to be displayed inside dialog of carpark marker
     * @return info to be displayed in dialog of carpaark marker
     */
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

    /**
     *
     * @return title to be displayed inside infowindow of carpark marker
     */
    @Override
    public String title() {
        String title = carparkCode + "\n" + carparkName + "\n" + "Vehicle Cat: " + vehCat;
        return title;
    }

    /**
     * Get latitude and longitude coordinates
     * @return latitude and longitude coordinates
     */
    @Override
    public LatLonCoordinate getLatLonCoord() {
        return latLonCoord;
    }

    /**
     * Get eastings and northings coordinates
     * @return eastings and northings coordinates
     */
    @Override
    public SVY21Coordinate getSVY21Coord() {
        return svyCoord;
    }

    /**
     * Set latitude and longitude coordinates
     * @param ll latitude and longitude coordinates
     */
    @Override
    public void SetLatLonCoord(LatLonCoordinate ll) {
        this.latLonCoord = ll;
    }

    /**
     * Set eastings and northings coordinates
     * @param svy21 eastings and northings coordinate
     */
    @Override
    public void SetSVY21Coordinate(SVY21Coordinate svy21) {
        this.svyCoord = svy21;
    }
}
