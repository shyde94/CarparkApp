package com.example.android.carparkappv1;


import android.app.Activity;
import android.content.Context;


/**
 * Created by Shide on 11/3/17.
 */

public class Shared {
    public static Context context;
    public static Activity activity; // it's fine for this app, but better move to weak reference
}
