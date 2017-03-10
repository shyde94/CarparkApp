package Controllers;

import android.content.Context;
import android.util.Log;

import com.example.android.carparkappv1.CarparkFinder;
import com.example.android.carparkappv1.MyCustomMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import MapProjectionConverter.LatLonCoordinate;


/**
 * Created by Shide on 11/3/17.
 */

public class MapController {

    private final String TAG = "MapControllerClass";

    MyCustomMap myMap;

    public MapController(MyCustomMap myMap){
       this.myMap = myMap;
    }

    public boolean initMap(){
        if (myMap.googleServicesAvailable()) {
            myMap.initMap();
            return true;
        }
        return false;
    }

    public void showCarparks(CarparkFinder cpFinder){
        ArrayList<LatLonCoordinate> llcList = cpFinder.handleQuery();
        int i;
        for(i=0;i<llcList.size();i++){
            LatLonCoordinate temp = llcList.get(i);
            LatLng tempLL = new LatLng(temp.getLatitude(), temp.getLongitude());
            myMap.setMarker(tempLL);
            Log.i(TAG, "LOOK: " + tempLL.toString());
        }
    }

    public void searchLocation(){

    }
}
