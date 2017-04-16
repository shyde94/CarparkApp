package com.example.android.carparkappv1;


import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
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

    /**
     * Method used to start activity
     * @param savedInstanceState State of the application that is saved in a bundle which can be passed back to onCreate if the activity needs to be recreated
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //this.deleteDatabase(CarparkDBController.DATABASE_NAME);
        Shared.activity = MainActivity.this;
        Shared.context = getApplicationContext();
        screenController.openScreen(ScreenController.Screen.MENU);
        //screenController.openScreen(ScreenController.Screen.TEST);
        (new LoadTable()).start();


    }

    /**
     * Method used when the Back button on phone is pressed
     */
    @Override
    public void onBackPressed() {
        if (ScreenController.getInstance().onBack()) {
            super.onBackPressed();
        }
    }

    /**
     * This gets called by MenuFragment when search button is clicked
     */
    //This gets called by MenuFragment when search button is clicked
    @Override
    public void onSearchedButtonClicked(String location) throws IOException {
        screenController.openScreen(MFH, location);
        Shared.destination = location;

    }

    /**
     * This gets called by MyCustomMap when arrived button is clicked
     */
    public void onArrivedButtonClicked() throws IOException {
        screenController.openScreen(SAVELOT);
    }

    /**
     * Method used to load table in database
     */
    public class LoadTable extends Thread{
        @Override
        public void run() {
            CarparkDBController.getInstance(Shared.context).getReadableDatabase();
        }
    }
}


    //Hide keyboard
    //InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    //mgr.hideSoftInputFromWindow(mInputLocation.getWindowToken(), 0);