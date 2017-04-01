package Carparks;

import com.akexorcist.googledirection.model.Info;
import com.google.android.gms.maps.model.LatLng;

import MapProjectionConverter.LatLonCoordinate;
import MapProjectionConverter.SVY21Coordinate;

public class Carpark {
    private SVY21Coordinate svyCoord;
    private LatLonCoordinate latLonCoord;

    private String DurationToDestination;
    private String DistanceToDestination;



    public Carpark(){
    }

    public Carpark(SVY21Coordinate svyCoord, LatLonCoordinate latLonCoord) {
        this.svyCoord = svyCoord;
        this.latLonCoord = latLonCoord;
    }

    public String getDurationToDestination() {
        return DurationToDestination;
    }

    public void setDurationToDestination(String durationToDestination) {
        DurationToDestination = durationToDestination;
    }

    public String getDistanceToDestination() {
        return DistanceToDestination;
    }

    public void setDistanceToDestination(String distanceToDestination) {
        DistanceToDestination = distanceToDestination;
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
}


//"car_park_no","address","x_coord","y_coord","car_park_type","type_of_parking_system","short_term_parking","free_parking","night_parking"