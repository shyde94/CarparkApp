package com.example.android.carparkappv1;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends AppCompatActivity  {
    Button button;
    EditText mInputLocation;
    TextView mLocationDisplay;
    MyCustomMap myMap;
    MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button)findViewById(R.id.search_button);
        mInputLocation = (EditText) findViewById(R.id.Search_location);
        mLocationDisplay = (TextView) findViewById(R.id.location_input);
        mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.mapFragment);
        myMap = new MyCustomMap(this, mapFragment);

        if(myMap.googleServicesAvailable()){
            myMap.initMap();
        }
        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String location = mInputLocation.getText().toString();
                       LatLng ll = myMap.searchLocation(location);

                    }
                }
        );
        /*if(googleServicesAvailable()){
            Toast.makeText(this,"Great", Toast.LENGTH_SHORT);
            initMap();
        }*/

    }
    /*
    private void initMap(){
        MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
    }

    private void gotoLocationZoom(double lat, double lng, float zoom){
        LatLng ll = new LatLng(lat,lng);
        CameraUpdate camUpdate = CameraUpdateFactory.newLatLngZoom(ll,zoom);
        mGoogleMap.animateCamera(camUpdate);
    }

    public boolean googleServicesAvailable(){
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int apiAvailable = api.isGooglePlayServicesAvailable(this);
        if(apiAvailable == ConnectionResult.SUCCESS){
            return true;
        }
        else if(api.isUserResolvableError(apiAvailable)){
            Dialog dialog = api.getErrorDialog(this, apiAvailable, 0);
            dialog.show();
        }
        else{
            Toast.makeText(this, "Can't connect to play services", Toast.LENGTH_SHORT).show();
        }
        return false;
    }


    public void searchLocation(View v){
        String location = null;
        location = mInputLocation.getText().toString();
        mLocationDisplay.setText(location);
        if(!location.equals("")){
            try{
                geoLocate(location);
            }catch(IOException e){
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
    }

    public void geoLocate(String location) throws IOException {
        double lat, lng;
        Geocoder gc = new Geocoder(this);
        List<Address> list = gc.getFromLocationName(location,5);
        Address address = list.get(0);
        String locality = address.getLocality();
        //Log.i("Debug",locality);
        Toast.makeText(this, locality, Toast.LENGTH_LONG).show();
        lat = address.getLatitude();
        lng = address.getLongitude();

        gotoLocationZoom(lat,lng,15);



;
    }*/
}
