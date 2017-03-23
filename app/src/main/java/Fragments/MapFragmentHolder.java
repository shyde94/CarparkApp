package Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.carparkappv1.R;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

import Controllers.MapController;

public class MapFragmentHolder extends Fragment {

    private final String TAG = "MapFragmentHolderClass";

    TextView test;
    MapFragment mapFragment;
    MyCustomMap myMap;
    public static MapController mapController;
    private String location;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapFragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment);
        myMap = new MyCustomMap();

        myMap.initMap();

    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        Log.i(TAG, "Create MapFragmentHolder");
        View view = layoutInflater.inflate(R.layout.map_fragment, viewGroup, false);
        //mapController = new MapController(myMap);
        test = (TextView) view.findViewById(R.id.testing);




        return view;
    }



    //Carry out all functions of the map!
    public void start(){

        Log.i(TAG, "inside MapHolderFragment start method");
        if(myMap != null){
            if(myMap.googleServicesAvailable()){
                myMap.initMap();
                LatLng ll = myMap.searchLocation(location);
                myMap.gotoLocationZoom(ll,15);
            }
        }


    }


}
