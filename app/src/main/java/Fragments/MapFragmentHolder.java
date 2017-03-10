package Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.carparkappv1.MyCustomMap;
import com.example.android.carparkappv1.R;
import com.google.android.gms.maps.MapFragment;

import Controllers.MapController;

public class MapFragmentHolder extends MapFragment {

    MapFragment mapFragment;
    MyCustomMap myMap;
    MapController mapController;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View view = layoutInflater.inflate(R.layout.map_fragment, viewGroup, false);
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment);
        myMap = new MyCustomMap(getActivity(), mapFragment);
        mapController = new MapController(myMap);

        if(mapController.initMap()){

        }



        return view;
    }
}
