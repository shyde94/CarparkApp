package Controllers;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;

import com.example.android.carparkappv1.R;
import com.example.android.carparkappv1.Shared;

import java.util.ArrayList;
import java.util.List;

import Fragments.MenuFragment;
import Fragments.MyCustomMap;
import Fragments.SaveLotNumber;
import Fragments.TestFrag;


/**
 * Acts as facade to load different screens.
 */
public class ScreenController {
    private static final String TAG = "ScreenControllerClass";
    public static ScreenController mInstance = null;
    private static List<Screen> openedScreens = new ArrayList<Screen>();
    private FragmentManager mFragmentManager;



    public static ScreenController getInstance() {
        if (mInstance == null) {
            mInstance = new ScreenController();
        }
        return mInstance;
    }

    public static Screen getLastScreen() {
        return openedScreens.get(openedScreens.size() - 1);
    }

    public enum Screen {
        MENU,
        MFH,
        TEST,
        SAVELOT,

    }

    public void openScreen(Screen screen){

        Log.i(TAG, "Opening normal screen");
        mFragmentManager = Shared.activity.getFragmentManager();
        Fragment fragment = getFragment(screen);
        if(fragment!= null){
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,fragment, "CURRENT_FRAG");
            fragmentTransaction.commit();
            mFragmentManager.executePendingTransactions();
        }
        openedScreens.add(screen);
        Log.i(TAG," Open Screen queue: " + openedScreens.toString());

    }
    //overloaded method to pass in data for mapfragment
    public void openScreen(Screen screen, String location){

        Log.i(TAG, "Opening myCustomMap screen");
        mFragmentManager = Shared.activity.getFragmentManager();
        MyCustomMap fragment = (MyCustomMap) getFragment(screen);
        fragment.setDestination(location);
        if(fragment!= null){
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,fragment, "CURRENT_FRAG");
            fragmentTransaction.commit();
            mFragmentManager.executePendingTransactions();
        }
        openedScreens.add(screen);
        Log.i(TAG,"Open Screen queue: " + openedScreens.toString());
    }

    public boolean onBack() {
        Log.i(TAG, "onBack pressed");
        Log.i(TAG,"Screen queue: " + openedScreens.toString());
        if (openedScreens.size() > 0) {
            openedScreens.remove(openedScreens.size() - 1);
            if (openedScreens.size() == 0) {
                return true;
            }
            Screen screen = openedScreens.get(openedScreens.size() - 1);
            openedScreens.remove(openedScreens.size() - 1);
            if(screen.equals(Screen.MFH)){
                openScreen(Screen.MFH, Shared.destination);
            }
            else{
                openScreen(screen);
            }
            Log.i(TAG,"Post queue: " + openedScreens.toString());
            return false;
        }
        return true;
    }

    public void revertToPreviousScreen(){
        Log.i(TAG, "Revert to previous scree");
        Log.i(TAG,"Screen queue: " + openedScreens.toString());
        if(openedScreens.size()>0){
            openedScreens.remove(openedScreens.size()-1);
            Screen screen = openedScreens.get(openedScreens.size()-1);
            openedScreens.remove(screen);
            if(screen.equals(Screen.MFH)){
                openScreen(Screen.MFH,Shared.destination);
            }
            else{
                openScreen(screen);
            }
        }

    }

    public Fragment getFragment(Screen screen){
        Fragment frag = null;
        switch(screen){
            case MENU:
                frag = new MenuFragment();
                break;
            case MFH:
                frag = new MyCustomMap();
                break;
            case TEST:
                frag = new TestFrag();
                break;
            case SAVELOT:
                frag = new SaveLotNumber();
        }
        return frag;
    }
}
