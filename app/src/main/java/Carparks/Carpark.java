package Carparks;

import com.akexorcist.googledirection.model.Info;
import com.google.android.gms.maps.model.LatLng;

import MapProjectionConverter.LatLonCoordinate;
import MapProjectionConverter.SVY21Coordinate;

/**
 * Strategy pattern interface. Can include more methods for more functionality.
 */
public interface Carpark {
    String displayInfo();

    String title();

    LatLonCoordinate getLatLonCoord();

    SVY21Coordinate getSVY21Coord();

    void SetLatLonCoord(LatLonCoordinate ll);

    void SetSVY21Coordinate(SVY21Coordinate svy21);
}



//"car_park_no","address","x_coord","y_coord","car_park_type","type_of_parking_system","short_term_parking","free_parking","night_parking"