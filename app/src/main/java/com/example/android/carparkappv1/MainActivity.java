package com.example.android.carparkappv1;


import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;

import Controllers.ScreenController;
import Fragments.MapFragmentHolder;
import Fragments.MenuFragment;
import Fragments.MyCustomMap;

import static Controllers.ScreenController.Screen.MFH;


public class MainActivity extends AppCompatActivity implements MenuFragment.OnSearchButtonClickedListener {

    public ScreenController screenController = new ScreenController();;

    private CarparkFinder cpFinder;

    private static final String TAG = "MainActivityClass";

    public Activity activity = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //this.deleteDatabase(CarparkDBController.DATABASE_NAME);
        Shared.activity = MainActivity.this;
        screenController.openScreen(ScreenController.Screen.MENU);
    }

    @Override
    public void onBackPressed() {
        if (ScreenController.getInstance().onBack()) {
            super.onBackPressed();
        }
    }

    //This gets called by searchLocationFragment when search button is clicked
    @Override
    public void onSearchedButtonClicked(String location) throws IOException {
        screenController.openScreen(MFH);
        Log.i(TAG, "Location? : " + location);
        MyCustomMap myMap = new MyCustomMap();
        myMap.setDestination(location);
        //screenController.openScreen(ScreenController.Screen.TEST);

    }
}


    //Hide keyboard
    //InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    //mgr.hideSoftInputFromWindow(mInputLocation.getWindowToken(), 0);