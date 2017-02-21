package com.example.android.carparkappv1;


import android.content.pm.PackageManager;
import android.location.Address;
import android.content.Context;
import android.location.Geocoder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Objects;


public class MyCustomMap implements OnMapReadyCallback {

    private static final String TAG = "MyCustomMapClass";
    private String location;
    private static GoogleMap mGoogleMap;
    private Context context;
    private MapFragment mapFragment;

    public String getLocation() {
        return location;
    }

    public GoogleMap getmGoogleMap() {
        return mGoogleMap;
    }

    public Context getContext() {
        return context;
    }

    public MapFragment getMapFragment() {
        return mapFragment;
    }

    public MyCustomMap(Context context, MapFragment mapFragment) {
        this.context = context;
        this.mapFragment = mapFragment;
        //onMapReady(mGoogleMap);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mGoogleMap = map;
        Log.i(TAG, "onMapReady map success");
    }


    public boolean googleServicesAvailable(){
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int apiAvailable = api.isGooglePlayServicesAvailable(context);
        if (apiAvailable == ConnectionResult.SUCCESS){
            Log.i(TAG, "google services available");
            return true;
        }else if(api.isUserResolvableError(apiAvailable)){
            //Dialog dialog = api.getErrorDialog(context, apiAvailable,0);
            //dialog.show();
        }
        else{
            Log.i(TAG, "error occurred.");
            Toast.makeText(context,"An error occurred", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    public void initMap(){
        mapFragment.getMapAsync(this);
        Log.i(TAG, "InitMap success");
    }

    public LatLng searchLocation(String location){
        Log.i(TAG, "Enter search location");
        Log.i(TAG, location);
        LatLng ll= new LatLng(0,0);
        if(!location.equals("")) try {
            geoLocate(location);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ll;
    }

    public void geoLocate(String location) throws IOException {
        Log.i(TAG, "Enter geoLocate");
        Log.i(TAG, location);
        double lat, lng;
        Geocoder gc = new Geocoder(context);
        List<Address> tempList = gc.getFromLocationName(location, 5);
        Address tempAddress = tempList.get(0);
        String locality = tempAddress.getLocality();

        Log.i(TAG,"Locality: "+locality);
        Toast.makeText(context, locality, Toast.LENGTH_SHORT).show();

        lat = tempAddress.getLatitude();
        lng = tempAddress.getLongitude();
        Log.i(TAG, "Lat:");
        Log.i(TAG, Double.toString(lat));
        Log.i(TAG, "Lng:");
        Log.i(TAG, Double.toString(lng));
        gotoLocationZoom(lat,lng,15);
    }

    //This is the problem!! This function!
    public void gotoLocationZoom(double lat, double lng, int zoom){
        Log.i(TAG, "Enter gotoLocationZoom");
        LatLng ll = new LatLng(lat,lng);
        CameraUpdate camUpdate = CameraUpdateFactory.newLatLngZoom(ll,zoom);
        //This is the lind causing all the problem.
        mGoogleMap.animateCamera(camUpdate);
    }

}


