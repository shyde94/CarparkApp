package com.example.android.carparkappv1;


import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;


public class MyCustomMap implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener {

    private static final String TAG = "MyCustomMapClass";
    private static GoogleMap mGoogleMap;
    private GoogleApiClient mGoogleApiClient;
    private Context context;
    private MapFragment mapFragment;
    private Location mCurrentLocation;

    public Location getmCurrentLocation() {
        return mCurrentLocation;
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

    }

    @Override
    public void onMapReady(GoogleMap map) {
        mGoogleMap = map;
        Log.i(TAG, "onMapReady map success");
        setCurrentLocation();

    }


    public boolean googleServicesAvailable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int apiAvailable = api.isGooglePlayServicesAvailable(context);
        if (apiAvailable == ConnectionResult.SUCCESS) {
            Log.i(TAG, "google services available");
            return true;
        } else if (api.isUserResolvableError(apiAvailable)) {
            //Dialog dialog = api.getErrorDialog(context, apiAvailable,0);
            //dialog.show();
        } else {
            Log.i(TAG, "error occurred.");
            Toast.makeText(context, "An error occurred", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    public void initMap() {
        mapFragment.getMapAsync(this);
        Log.i(TAG, "InitMap success");
    }

    public LatLng searchLocation(String location) {
        Log.i(TAG, "Enter search location");
        Log.i(TAG, location);
        LatLng ll = null;
        if (!location.equals("")) try {
            ll = geoLocate(location);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ll;
    }


    public LatLng geoLocate(String location) throws IOException {
        Log.i(TAG, "Enter geoLocate");
        Log.i(TAG, location);
        double lat, lng;
        Geocoder gc = new Geocoder(context);
        List<Address> tempList = gc.getFromLocationName(location, 5);
        Address tempAddress = tempList.get(0);
        String locality = tempAddress.getLocality();

        Log.i(TAG, "Locality: " + locality);
        Toast.makeText(context, locality, Toast.LENGTH_SHORT).show();

        lat = tempAddress.getLatitude();
        lng = tempAddress.getLongitude();
        Log.i(TAG, "Lat:");
        Log.i(TAG, Double.toString(lat));
        Log.i(TAG, "Lng:");
        Log.i(TAG, Double.toString(lng));
        //gotoLocationZoom(lat,lng,15);
        LatLng ll = new LatLng(lat, lng);
        //setMarker(locality, ll);
        return ll;
    }


    public void gotoLocationZoom(double lat, double lng, int zoom) {
        Log.i(TAG, "Enter gotoLocationZoom");
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate camUpdate = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        //This is the lind causing all the problem.
        mGoogleMap.animateCamera(camUpdate);
    }

    public void gotoLocationZoom(LatLng ll, int zoom) {
        Log.i(TAG, "Enter gotoLocationZoom");
        CameraUpdate camUpdate = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        //This is the lind causing all the problem.
        mGoogleMap.animateCamera(camUpdate);
    }

    public void setMarker(String location, LatLng ll) {
        MarkerOptions options = new MarkerOptions()
                .title(location)
                .position(ll);
        mGoogleMap.addMarker(options);
    }

    public void setMarker(LatLng ll) {
        MarkerOptions options = new MarkerOptions()
                .position(ll);
        mGoogleMap.addMarker(options);
    }


    public void setCurrentLocation() {
        Log.i(TAG, "setCurrentLocation");
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "onConnected");
        LocationRequest mlocationRequest = LocationRequest.create();
        mlocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            String[] mPermission = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
            ActivityCompat.requestPermissions((Activity) context, mPermission, 1);
            Log.i(TAG, "Ask for permission");
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mlocationRequest, this);
        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        Log.i(TAG, "Current lat: " + mCurrentLocation.getLatitude());
        Log.i(TAG, "Current long: "+ mCurrentLocation.getLongitude());

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if(location == null){
            Toast.makeText(context, "Can't get current location", Toast.LENGTH_SHORT).show();
        }
        else{
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            gotoLocationZoom(ll, 15);
            setMarker(ll);
        }

    }

    //this function decides how much to zoom the camera by. centering destination and accomodating all carparks within vicinity.
    public int zoomFactor(){
        int zoomFactor = 0;

        return zoomFactor;
    }

}