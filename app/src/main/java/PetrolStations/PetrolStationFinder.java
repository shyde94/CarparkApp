package PetrolStations;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import Carparks.Carpark;
import Controllers.CarparkDBController;
import MapProjectionConverter.LatLonCoordinate;
import MapProjectionConverter.SVY21;
import MapProjectionConverter.SVY21Coordinate;

/**
 * Created by Shide on 6/4/17.
 */

public class PetrolStationFinder {
    private CarparkDBController cpController;
    private ArrayList<Carpark> stationList = new ArrayList<>();
    private LatLng currentLocation;


    private static final String TAG = "PetrolStationFinderClass";

    /**
     * PetrolStationFinder class constructor
     * @param context
     * @param currentLocation
     */
    public PetrolStationFinder(Context context, LatLng currentLocation) {
        this.cpController = CarparkDBController.getInstance(context);
        this.currentLocation = currentLocation;
    }

    /**
     * This method uses the current location to find the nearby petrol station
     * It will first search the petrol station database and it will find the carpark within a certain range of the current location
     * These selected carpark tuples from the database are then added to the array list stationList
     */
    public void retrieveStations() {
        Log.i(TAG, "Enter retrieve Carparks");
        SVY21Coordinate temp = getSVY21Coord(currentLocation);

        //CursorList will contain every row of carpark within the vicinity
        Cursor cursorList = cpController.queryRetrievePetrolKiosks(temp);
        cursorList.moveToFirst();
        while (cursorList.isAfterLast() == false) {
            //Create carpark object containing data from each row!
            double easting = cursorList.getDouble(cursorList.getColumnIndex(CarparkDBController.COLUMN_Xcoord));
            double northing = cursorList.getDouble(cursorList.getColumnIndex(CarparkDBController.COLUMN_Ycoord));

            SVY21Coordinate svy21 = new SVY21Coordinate(northing, easting);
            LatLonCoordinate latLon = SVY21.computeLatLon(svy21);
            String StationName = cursorList.getString(cursorList.getColumnIndex(CarparkDBController.COLUMN_STATION_NAME));
            String address = cursorList.getString(cursorList.getColumnIndex(CarparkDBController.COLUMN_address));
            String services = cursorList.getString(cursorList.getColumnIndex(CarparkDBController.COLUMN_SERVICES));
            Carpark stationTemp = new PStations(svy21,latLon, StationName, address, services);
            cursorList.moveToNext();

            stationList.add(stationTemp);
            //Log.i(TAG, "Carpark: " + cpNum + " " + svy21.getNorthing() + " " + svy21.getEasting());

        }
    }

    /**
     * This method Converts the coordinates to be used on google map
     * @param d
     * @return
     */
    public SVY21Coordinate getSVY21Coord(LatLng d) {
        SVY21Coordinate svyC = SVY21.computeSVY21(d.latitude, d.longitude);
        return svyC;
    }

    /**
     * This method Retunrns stationlist arraylist
     * @return
     */
    public ArrayList<Carpark> getStationList() {
        return stationList;
    }

    /**
     * This method Sets the stationlist arraylist
     * @param stationList
     */
    public void setStationList(ArrayList<Carpark> stationList) {
        this.stationList = stationList;
    }
}
