package Controllers;

import android.content.Context;

import com.example.android.carparkappv1.MyCustomMap;
import com.google.android.gms.maps.MapFragment;


/**
 * Created by Shide on 11/3/17.
 */

public class MapController {
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
}
