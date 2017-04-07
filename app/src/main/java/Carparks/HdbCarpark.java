package Carparks;

import MapProjectionConverter.LatLonCoordinate;
import MapProjectionConverter.SVY21Coordinate;

/**
 * Instances of this class represent HDB Carparks
 */
public class HdbCarpark implements Carpark {
    private SVY21Coordinate svyCoord;
    private LatLonCoordinate latLonCoord;
    private String cpNum;
    private String cpType;
    private String typeOfParkingSystem;
    private String shortTermParking;
    private String freeParking;
    private String nightParking;
    private String address;

    /**
     *
     * @param svyCoord svyCoord object that stores eastings and northings of carpark
     * @param latLonCoord LatlonCoord that stores latitude and longitude of carpark
     * @param cpNum Represents carpark number given by HDB
     * @param cpType Type of carpark
     * @param typeOfParkingSystem Type of parking system
     * @param shortTermParking Information about short term parking
     * @param freeParking Information about free parking
     * @param nightParking Information about night parking
     * @param address Address of carpark
     */
    public HdbCarpark(SVY21Coordinate svyCoord, LatLonCoordinate latLonCoord, String cpNum, String cpType, String typeOfParkingSystem, String shortTermParking, String freeParking, String nightParking, String address) {
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

    public HdbCarpark(){}


    /**
     * Get carpark number
     * @return carpark number
     */
    public String getCpNum() {
        return cpNum;
    }

    /**
     * Set carpark number
     * @param cpNum
     */
    public void setCpNum(String cpNum) {
        this.cpNum = cpNum;
    }

    /**
     * Get carpark type
     * @return carpark type
     */
    public String getCpType() {
        return cpType;
    }

    /**
     * Set carpark type
     * @param cpType Type of carpark
     */
    public void setCpType(String cpType) {
        this.cpType = cpType;
    }

    /**
     * Get type of parking system
     * @return
     */
    public String getTypeOfParkingSystem() {
        return typeOfParkingSystem;
    }

    /**
     * Set type of parking system
     * @param typeOfParkingSystem
     */
    public void setTypeOfParkingSystem(String typeOfParkingSystem) {
        this.typeOfParkingSystem = typeOfParkingSystem;
    }

    /**
     * Get info about short term parking
     * @return
     */
    public String getShortTermParking() {
        return shortTermParking;
    }

    /**
     * Set info about short term parking
     * @param shortTermParking Information about short term parking
     */
    public void setShortTermParking(String shortTermParking) {
        this.shortTermParking = shortTermParking;
    }
    /**
     * Get info about free parking
     * @return
     */
    public String getFreeParking() {
        return freeParking;
    }

    /**
     * Set information about free parking
     * @param freeParking Information about free parking
     */
    public void setFreeParking(String freeParking) {
        this.freeParking = freeParking;
    }
    /**
     * Get info about night parking
     * @return
     */
    public String getNightParking() {
        return nightParking;
    }

    /**
     * Set information about night parking
     * @param nightParking Information about night parking
     */
    public void setNightParking(String nightParking) {
        this.nightParking = nightParking;
    }
    /**
     * Get info about carpark address
     * @return
     */
    public String getAddress() {
        return address;
    }

    /**
     * Set carpark address
     * @param address Carpark address
     */
    public void setAddress(String address) {
        this.address = address;
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
        this.latLonCoord = ll;
    }

    @Override
    public String title() {
        String title = cpNum + "\n" + address + "";
        return title;
    }

    @Override
    public void SetSVY21Coordinate(SVY21Coordinate svy21) {
        this.svyCoord = svy21;
    }

    @Override
    public String displayInfo() {
        String cpAddress = getAddress() + "\n";
        String cpType = getTypeOfParkingSystem() + "\n";
        String cpNum = getCpNum();
        String carpark_details = "Address: " + cpAddress
                + "Type of parking: " + typeOfParkingSystem+ "\n"
                + "Short Term parking: " + shortTermParking + "\n"
                + "Night parking: "+ nightParking;
        return carpark_details;
    }
}
