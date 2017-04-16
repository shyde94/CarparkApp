package com.example.android.carparkappv1;


import android.app.Activity;
import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

import Carparks.Carpark;


/**
 * Variables of shared class are static variables, so that they can be referenced in methods of other classes
 */

public class Shared {
    public static Context context;
    public static Activity activity; // it's fine for this app, but better move to weak reference
    public static String destination;
    public static LatLng latLngDestination;
    public static Carpark carpark;
    public static int choice=0;
}
