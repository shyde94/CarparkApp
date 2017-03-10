package com.example.android.carparkappv1;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import Controllers.ScreenController;
import Fragments.MapFragmentHolder;
import Fragments.MenuFragment;


public class MainActivity extends AppCompatActivity implements MenuFragment.OnSearchButtonClickedListener {

    ScreenController screenController;

    private CarparkFinder cpFinder;

    private static final String TAG = "MainActivityClass";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //this.deleteDatabase(CarparkDBController.DATABASE_NAME);
        screenController.openScreen(ScreenController.Screen.MENU);


    }

    //This gets called by searchLocationFragment when search button is clicked
    @Override
    public void onSearchedButtonClicked(String location) {
        screenController.openScreen(ScreenController.Screen.MFH);
        MapFragmentHolder mfh = (MapFragmentHolder) getFragmentManager().findFragmentById(R.id.mapFragment);
    }
}


    //Hide keyboard
    //InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    //mgr.hideSoftInputFromWindow(mInputLocation.getWindowToken(), 0);