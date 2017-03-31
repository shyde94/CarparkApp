package com.example.android.carparkappv1;


import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;

import Controllers.CarparkDBController;
import Controllers.ScreenController;
import Fragments.MenuFragment;
import Fragments.MyCustomMap;

import static Controllers.ScreenController.Screen.MFH;

import static Controllers.ScreenController.Screen.SAVELOT;


public class MainActivity extends AppCompatActivity implements MenuFragment.OnSearchButtonClickedListener, MyCustomMap.OnArrivedButtonClickedListener {

    public ScreenController screenController = new ScreenController();

    private static final String TAG = "MainActivityClass";

    public Activity activity = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.deleteDatabase(CarparkDBController.DATABASE_NAME);
        Shared.activity = MainActivity.this;

        screenController.openScreen(ScreenController.Screen.MENU);
        //screenController.openScreen(ScreenController.Screen.TEST);



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
        screenController.openScreen(MFH, location);
        Shared.destination = location;

    }

    public void onArrivedButtonClicked() throws IOException {
        screenController.openScreen(SAVELOT);
    }
}


    //Hide keyboard
    //InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    //mgr.hideSoftInputFromWindow(mInputLocation.getWindowToken(), 0);