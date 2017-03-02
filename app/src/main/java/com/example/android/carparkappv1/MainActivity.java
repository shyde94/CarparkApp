package com.example.android.carparkappv1;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

import MapProjectionConverter.LatLonCoordinate;
import MapProjectionConverter.SVY21;
import MapProjectionConverter.SVY21Coordinate;


public class MainActivity extends AppCompatActivity {
    Button button;
    EditText mInputLocation;
    TextView mLocationDisplay;
    MyCustomMap myMap;
    MapFragment mapFragment;

    private static final String TAG = "MainActivityClass";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //this.deleteDatabase(CarparkDBController.DATABASE_NAME);
        button = (Button) findViewById(R.id.search_button);
        mInputLocation = (EditText) findViewById(R.id.Search_location);
        mLocationDisplay = (TextView) findViewById(R.id.location_input);
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment);
        myMap = new MyCustomMap(this, mapFragment);

        if (myMap.googleServicesAvailable()) {
            myMap.initMap();
        }
        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String location = mInputLocation.getText().toString();
                        if (!location.equals("")) {
                            LatLng ll = myMap.searchLocation(location);
                            myMap.gotoLocationZoom(ll, 15);
                            myMap.setMarker(location, ll);

                            //To test to find boundary box of carparks.
                            SVY21Coordinate destSVY21 = SVY21.computeSVY21(ll.latitude, ll.longitude);
                            Log.i(TAG, "Lat: " + ll.latitude);
                            Log.i(TAG, "Lng: " + ll.longitude);
                            Log.i(TAG, "Easting: " + destSVY21.getEasting());
                            Log.i(TAG, "Northing: " + destSVY21.getNorthing());


                            //for testing purposes
                            double x = 27576.755922097967;
                            double y = 28018.944427629576;
                            LatLonCoordinate llC = SVY21.computeLatLon(y,x); //Northing then Easting!
                            LatLng ll2 = new LatLng(llC.getLatitude(), llC.getLongitude());
                            myMap.setMarker(ll2);


                            Log.i(TAG, "test lat: " + llC.getLatitude());
                            Log.i(TAG, "test long: " + llC.getLongitude());

                        }
                        //Hide keyboard
                        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        mgr.hideSoftInputFromWindow(mInputLocation.getWindowToken(), 0);
                    }
                }
        );
    }

}
