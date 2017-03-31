package Fragments;


import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;

import android.content.Context;

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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.util.DirectionConverter;
import Carparks.Carpark;
import Carparks.CarparkFinder;
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
import com.google.android.gms.maps.model.Polyline;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Carparks.HdbCarpark;
import Carparks.SmCarpark;
import MapProjectionConverter.LatLonCoordinate;
import MapProjectionConverter.SVY21;
import MapProjectionConverter.SVY21Coordinate;


public class MyCustomMap extends Fragment implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener,GoogleMap.OnInfoWindowClickListener,GoogleMap.OnMarkerClickListener{

/**
 * MyCustomMap contains the GoogleMap object which initialises the main map being used. Contains destination set by user and
 * user's current location. Implements OnMapReadyCallBack, and GoogleApiClient interfaces.
 */
    private static final String TAG = "MyCustomMapClass";


    //Variables essential for mapfragment
    private static GoogleMap mGoogleMap;
    private GoogleApiClient mGoogleApiClient;
    private MapFragment mapFragment;
    private Location mCurrentLocation;
    private String destination = "";
    private boolean routeFlag = true;
    private Polyline route;

    Button arrivedbutton;
    OnArrivedButtonClickedListener mListener;


    HashMap<Marker, Carpark> markerToCarpark = new HashMap<>();

    private CarparkFinder cpFinder;

    /*public static MyCustomMap newInstance(String destination){

        MyCustomMap myMap =  new MyCustomMap();
        Bundle args = new Bundle();
        args.putString("destination", location);
        myMap.setArguments(args);
        return myMap;
    }*/
    public String getDestination(){

    /**
     *
     * @return destination
     */
        return destination;
    }

    /**
     *
     * @param destination is the location that the user keys. This location will be set as the users destination.
     */
    public void setDestination(String destination) {
        this.destination = destination;
    }

    /**
     *
     * @return Location object that contains coordinates of current location
     */
    public Location getmCurrentLocation() {
        return mCurrentLocation;
    }

    /**
     *
     * @return GoogleMap object
     */
    public GoogleMap getmGoogleMap() {
        return mGoogleMap;
    }


    /**
     *
     * @return MapFragment object that
     */
    public MapFragment getMapFragment() {
        return mapFragment;
    }

    /**
     * Required method in android when creating fragment.
     * @param inflater LayoutInflater object, contains method inflate which returns a view object
     * @param container Viewgroup container
     * @param savedInstanceState Bundle object, contains saved data to be passed to fragment
     * @return View object
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.map_fragment);
        View view = inflater.inflate(com.example.android.carparkappv1.R.layout.map_fragment, container, false);
        mapFragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment);
        //destination = mapFragment.getArguments().getString("Destination");
        try {
            googleMapStart();
        } catch (IOException e) {
            e.printStackTrace();
        }

        arrivedbutton = (Button) view.findViewById(R.id.arrived_button);

        arrivedbutton.setOnClickListener(
                new View.OnClickListener(){
                    /**
                     * Calls method buttonClicked(view)
                     * @param view View object that is being selected
                     */
                    @Override
                    public void onClick(View view) {
                        try {
                            buttonClicked(view);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );

        return view;
    }

    /**
     * Calls
     * @param v
     * @throws IOException
     */
    public void buttonClicked(View v) throws IOException {
        mListener.onArrivedButtonClicked();
    }

    public interface OnArrivedButtonClickedListener {
        public void onArrivedButtonClicked() throws IOException;

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnArrivedButtonClickedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnArrivedButtonClickedListener");
        }
    }

    /**
     * If googleServicesAvailable is available, creates CarparkFinder object and retrieves nearby carparks.
     * Calls initMap()
     * @throws IOException
     */
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


    /**
     * This method is called when google.asynch() is called
     * @param map GoogleMap object
     */
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
        map.setOnMarkerClickListener(this);
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

    /**
     * Calls getMapAsynch() which onMapReady()
     */
    public void initMap() {
        Log.i("MapFragmentHolderClass", "enter init map" );
        mapFragment.getMapAsync(this);
        Log.i(TAG, "InitMap success");
    }

    /**
     *
     */
    public void searchDestination(){
        if(!destination.equals("")){
            LatLng ll = searchLocation(destination);
            gotoLocationZoom(ll, 15);
            setMarker(destination,ll);
        }

    }

    /**
     * This method converts the name of a place into a LatLng object that can be displayed on the map using the method geoLocate()
     * @param location String that represents the location input by the user.
     * @return LatLng object of location input by user
     */
    //This returns a LatLng object, destination in CarparkFinder class is a LatLng object!
    public LatLng searchLocation(String location) {
        Log.i(TAG, "Enter search destination");
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

    /**
     * Shifts camera to location on map indicated by LatLng Object
     * @param ll LatLng object that represents point on map to focus on
     * @param zoom Integer value for camera to zoom in. Greater the integer, greater the zoom level.
     */
    public void gotoLocationZoom(LatLng ll, int zoom) {
        Log.i(TAG, "Enter gotoLocationZoom");
        CameraUpdate camUpdate = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        //This is the lind causing all the problem.
        //mGoogleMap.animateCamera(camUpdate);
        mGoogleMap.moveCamera(camUpdate);
    }

    /**
     * Takes in a location entered by the user and convert it from a String to a LatLng object, so the latitude and longitude
     * can be retrieved for further use.
     * @param location String location entered by user
     * @return LatLng object containing latitude and longitude of location input by user
     * @throws IOException
     */
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

    /**
     * Method to pin icon that represents a Carpark on the map
     * @param cp Carpark object
     * @param ll LatLng object containing coordinates of carpark
     */
    //marker for nearby carpark
    public void setMarkerForNearbyCp(Carpark cp, LatLng ll){
            MarkerOptions options = new MarkerOptions()
                    .title(cp.title())
                    .position(ll)
                    .snippet("Click for more information..")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.car2));
            //Edit Snippet to edit text in the info window. This snippet is the same one in infoWindowAdaptor
            Marker m = mGoogleMap.addMarker(options);
            markerToCarpark.put(m, cp);


    }

    //marker for destination searched

    /**
     * Method to pin icon that represents user's destination on the map
     * @param title Title of marker
     * @param ll LatLng object containing coordinates of destination
     */
    public void setMarker(String title, LatLng ll) {
        MarkerOptions options = new MarkerOptions()
                .title(title)
                .position(ll).snippet("This is the destination searched.")
                .position(ll).snippet("Destination")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        mGoogleMap.addMarker(options);
    }



//    public void setMarkerForRoute(String title, LatLng ll) {
//        MarkerOptions options = new MarkerOptions()
//                .position(ll)
//                .title(title);
//        mGoogleMap.addMarker(options);
//    }

    public void setMarker(LatLng ll) {
        MarkerOptions options = new MarkerOptions()
                .position(ll)
                .title("Carpark");
        mGoogleMap.addMarker(options);
    }

    public void setCurrentLocationMarker(LatLng ll) {
        MarkerOptions options = new MarkerOptions()
                .position(ll)
                .title("Carpark")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        mGoogleMap.addMarker(options);
    }

    /**
     * Method used to record user's current location.
     */
    public void setCurrentLocation() {
        Log.i(TAG, "setCurrentLocation");
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();
    }

    /**
     * Method to be implemented from interface onMapReadyCallback
     * @param bundle
     */
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
            Toast.makeText(getActivity(), "Can't get current destination", Toast.LENGTH_SHORT).show();
        }
        else{
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            try {
                float[] temp = measureDistanceBetween(ll, geoLocate(destination));
                if(temp[0] < 500){
                    gotoLocationZoom(ll, 15);
                    setCurrentLocationMarker(ll);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    public float[] measureDistanceBetween(LatLng start, LatLng end){
        float[] temp = new float[1];
        Location.distanceBetween(start.longitude, start.latitude, end.longitude, end.latitude, temp);
        return temp;
    }

    //this function decides how much to zoom the camera by. centering destination and accomodating all carparks within vicinity.
    public int zoomFactor(){
        int zoomFactor = 0;

        return zoomFactor;
    }

    //This method will set the markers of the nearby carparks on the map AND DISPLAY ROUTE
    /**
     * This method retrieves the carpark objects from CarparkFinder and displays the position of the carparks on the map.
     *
     */
    //This method will set the markers of the nearby carparks on the map
    public void displayNearbyCarparks(){
        Log.i(TAG, "Enter displayNearbyCarparks");
        for(Carpark cp : cpFinder.getCpList()){
            Log.i(TAG, "Enter for loop");
            double lat = cp.getLatLonCoord().getLatitude();
            double lng = cp.getLatLonCoord().getLongitude();
            LatLng latlng = new LatLng(lat, lng);
            setMarkerForNearbyCp(cp, latlng);
        }
    }
    //After clicking marker
    @Override
    public boolean onMarkerClick(Marker marker) {

        Carpark cp = markerToCarpark.get(marker);
        marker.showInfoWindow();

        //to display route on maps
        LatLng origin = null; //origin is the location searched in the main page
        if (cp!=null){
            try {
                origin = geoLocate(getDestination());
            } catch (IOException e) {
                e.printStackTrace();
            }

            //latlng of nearby carparks
            double lat = cp.getLatLonCoord().getLatitude();
            double lng = cp.getLatLonCoord().getLongitude();
            LatLng cpLocation = new LatLng(lat, lng);

            GoogleDirection.withServerKey(getString(R.string.GOOGLE_MAPS_DIRECTIONS_API_KEY))
                    .from(origin)
                    .to(cpLocation)
                    .transportMode(TransportMode.WALKING)
                    .execute(new DirectionCallback() {

                        @Override
                        public void onDirectionSuccess(Direction direction, String rawBody) {

                            if (direction.isOK() && routeFlag == true) {
                                ArrayList<LatLng> directionPositionList = direction.getRouteList().get(0).getLegList().get(0).getDirectionPoint();
                                route = mGoogleMap.addPolyline(DirectionConverter.createPolyline(getActivity(), directionPositionList, 3, Color.RED));

                                //to ensure that it is only clicked once
                                routeFlag = false;
                            }

                            else if(direction.isOK() && routeFlag == false){
                                route.remove();
                                ArrayList<LatLng> directionPositionList = direction.getRouteList().get(0).getLegList().get(0).getDirectionPoint();
                                route = mGoogleMap.addPolyline(DirectionConverter.createPolyline(getActivity(), directionPositionList, 3, Color.RED));
                            }
                        }

                        @Override
                        public void onDirectionFailure(Throwable t) {
                            Log.i(TAG,"ROUTING FAILED");
                        }
                    });

            Log.i(TAG, "MarkerClicked");
        }

        return true;
    }/*Recently added*/

    /**
     * Method to test range to search for carparks. Used for debugging and testing
     * @param xRange for testing boundary in X-axis direction
     * @param yRange for testing boundary in Y-axis direction
     */
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