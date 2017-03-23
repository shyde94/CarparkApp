package Fragments;


import android.Manifest;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.carparkappv1.Carpark;
import com.example.android.carparkappv1.CarparkFinder;
import com.example.android.carparkappv1.R;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import MapProjectionConverter.LatLonCoordinate;
import MapProjectionConverter.SVY21;
import MapProjectionConverter.SVY21Coordinate;


public class MyCustomMap extends Fragment implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener,GoogleMap.OnInfoWindowClickListener{

    private static final String TAG = "MyCustomMapClass";

    //Variables essential for mapfragment
    private static GoogleMap mGoogleMap;
    private GoogleApiClient mGoogleApiClient;
    private MapFragment mapFragment;
    private Location mCurrentLocation;
    private String destination = "";

    HashMap<Marker, Carpark> markerToCarpark = new HashMap<>();

    private CarparkFinder cpFinder;

    /*public static MyCustomMap newInstance(String destination){
        MyCustomMap myMap =  new MyCustomMap();
        Bundle args = new Bundle();
        args.putString("Destination", destination);
        myMap.setArguments(args);
        return myMap;
    }*/

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Location getmCurrentLocation() {
        return mCurrentLocation;
    }

    public GoogleMap getmGoogleMap() {
        return mGoogleMap;
    }



    public MapFragment getMapFragment() {
        return mapFragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(com.example.android.carparkappv1.R.layout.map_fragment, container, false);
        mapFragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment);
        //destination = mapFragment.getArguments().getString("Destination");
        try {
            googleMapStart();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return view;
    }

    public void googleMapStart() throws IOException {
        if(googleServicesAvailable()){
            Log.i(TAG, "Enter googleMapStart");
            LatLng ll = searchLocation(destination);
            cpFinder = new CarparkFinder(ll, getActivity());
            cpFinder.retrieveCarparks();
            initMap();
            //gotoLocationZoom(ll, 15);

        }
    }


    //So..I should call all the methods here. basically everything related to the functional requirements of the carpark
    //search should appear after "Search" is clicked.
    @Override
    public void onMapReady(GoogleMap map) {
        mGoogleMap = map;
        Log.i(TAG, "onMapReady map success");
        setCurrentLocation();
        searchDestination();
        displayNearbyCarparks();
        //bBoxTest(500,500);

        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {/*Edits the layout of the info window*/


                LinearLayout info = new LinearLayout(getContext());
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(getContext());
                title.setTextColor(Color.RED);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(getContext());
                snippet.setTextColor(Color.BLACK);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });

        map.setOnInfoWindowClickListener(this);/*Recently added. Waits for the marker to be clicked*/



    }


    @Override
    public void onInfoWindowClick(Marker marker) {
        FragmentManager fm = getActivity().getFragmentManager();
        Carpark cp = markerToCarpark.get(marker);
        DialogFrag dialog = new DialogFrag();
        dialog.setCarpark(cp);
        dialog.show(fm, "Test");//Shows DialogFrag
        marker.showInfoWindow();

        Log.i(TAG, "InfoWin Clicked");
    }/*Recently added*/

    public boolean googleServicesAvailable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int apiAvailable = api.isGooglePlayServicesAvailable(getActivity());
        if (apiAvailable == ConnectionResult.SUCCESS) {
            Log.i(TAG, "google services available");
            return true;
        } else if (api.isUserResolvableError(apiAvailable)) {
            //Dialog DialogFrag = api.getErrorDialog(context, apiAvailable,0);
            //DialogFrag.show();
        } else {
            Log.i(TAG, "error occurred.");
            Toast.makeText(getActivity(), "An error occurred", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    public void initMap() {
        Log.i("MapFragmentHolderClass", "enter init map" );
        mapFragment.getMapAsync(this);
        Log.i(TAG, "InitMap success");
    }

    public void searchDestination(){
        if(!destination.equals("")){
            LatLng ll = searchLocation(destination);
            gotoLocationZoom(ll, 15);
            setMarker(destination,ll);
        }

    }


    //This returns a LatLng object, destination in CarparkFinder class is a LatLng object!
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

    public void gotoLocationZoom(double lat, double lng, int zoom) {
        Log.i(TAG, "Enter gotoLocationZoom");
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate camUpdate = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        //This is the lind causing all the problem.
        //mGoogleMap.animateCamera(camUpdate);
        mGoogleMap.moveCamera(camUpdate);
    }

    public void gotoLocationZoom(LatLng ll, int zoom) {
        Log.i(TAG, "Enter gotoLocationZoom");
        CameraUpdate camUpdate = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        //This is the lind causing all the problem.
        //mGoogleMap.animateCamera(camUpdate);
        mGoogleMap.moveCamera(camUpdate);
    }

    public LatLng geoLocate(String location) throws IOException {
        Log.i(TAG, "Enter geoLocate");
        Log.i(TAG, location);
        double lat, lng;
        Geocoder gc = new Geocoder(getActivity());
        List<Address> tempList = gc.getFromLocationName(location, 5);
        Address tempAddress = tempList.get(0);
        String locality = tempAddress.getLocality();

        Log.i(TAG, "Locality: " + locality);
        Toast.makeText(getActivity(), locality, Toast.LENGTH_SHORT).show();

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

    //marker for nearby carpark
    public void setMarkerForNearbyCp(Carpark cp, LatLng ll){

        MarkerOptions options = new MarkerOptions()
                .title(cp.getCpNum() + "\n" + cp.getAddress())
                .position(ll)
                .snippet("Click for more information..")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.car2));
        //Edit Snippet to edit text in the info window. This snippet is the same one in infoWindowAdaptor
        Marker m = mGoogleMap.addMarker(options);
        markerToCarpark.put(m, cp);
    }

    //marker for destination searched
    public void setMarker(String title, LatLng ll) {
        MarkerOptions options = new MarkerOptions()
                .title(title)
                .position(ll).snippet("This is the location searched.")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        mGoogleMap.addMarker(options);
    }

    public void setMarker(LatLng ll) {
        MarkerOptions options = new MarkerOptions()
                .position(ll).title("Carpark");
        mGoogleMap.addMarker(options);
    }


    public void setCurrentLocation() {
        Log.i(TAG, "setCurrentLocation");
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "onConnected");
        LocationRequest mlocationRequest = LocationRequest.create();
        mlocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            String[] mPermission = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
            ActivityCompat.requestPermissions((Activity) getActivity(), mPermission, 1);
            Log.i(TAG, "Ask for permission");
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mlocationRequest, this);
        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(mCurrentLocation != null){
            Log.i(TAG, "Current lat: " + mCurrentLocation.getLatitude());
            Log.i(TAG, "Current long: "+ mCurrentLocation.getLongitude());
        }


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
            Toast.makeText(getActivity(), "Can't get current location", Toast.LENGTH_SHORT).show();
        }
        else{
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            //gotoLocationZoom(ll, 15);
            //setMarker(ll);
        }

    }

    //this function decides how much to zoom the camera by. centering destination and accomodating all carparks within vicinity.
    public int zoomFactor(){
        int zoomFactor = 0;

        return zoomFactor;
    }


    //This method will set the markers of the nearby carparks on the map
    public void displayNearbyCarparks(){
        Log.i(TAG, "Enter displayNearbyCarparks");
        for(Carpark cp : cpFinder.getCpList()){
            double lat = cp.getLatLonCoord().getLatitude();
            double lng = cp.getLatLonCoord().getLongitude();

            String cpNum = cp.getCpNum();
            String cpType = cp.getCpType();
            String cpFreeParking = cp.getFreeParking();
            String cpNightParking = cp.getNightParking();
            String cpAddress = cp.getAddress();

            LatLng latlng = new LatLng(lat, lng);

            setMarkerForNearbyCp(cp, latlng);

            Log.i(TAG, "Cp: " + cpNum + "lat: " + lat + "lng: " + lng);
        }
    }


    public void bBoxTest(double xRange, double yRange){
        LatLng ll = new LatLng(1.3720937, 103.9473728);
        LatLonCoordinate llC = new LatLonCoordinate(ll.latitude, ll.longitude);
        SVY21Coordinate svy21 = SVY21.computeSVY21(llC);
        double easting = svy21.getEasting();
        double northing = svy21.getNorthing();

        double xBoxMin = easting - xRange;
        double xBoxMax = easting + xRange;
        double yBoxMin = northing - yRange;
        double yBoxMax = northing + yRange;

        ArrayList<LatLonCoordinate> llcArrayList = new ArrayList<LatLonCoordinate>();

        llcArrayList.add(SVY21.computeLatLon(yBoxMax, xBoxMax));
        llcArrayList.add(SVY21.computeLatLon(yBoxMax, xBoxMin));
        llcArrayList.add(SVY21.computeLatLon(yBoxMin, xBoxMin));
        llcArrayList.add(SVY21.computeLatLon(yBoxMin, xBoxMax));

        for(LatLonCoordinate  llc : llcArrayList){
            LatLng temp = new LatLng(llc.getLatitude(), llc.getLongitude());
            setMarker(temp);
        }
    }




}