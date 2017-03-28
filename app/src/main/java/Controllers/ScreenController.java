package Controllers;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.example.android.carparkappv1.R;
import com.example.android.carparkappv1.Shared;

import java.util.ArrayList;
import java.util.List;

import Fragments.MenuFragment;
import Fragments.MyCustomMap;
import Fragments.TestFrag;


public class ScreenController {
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

    public boolean onBack() {
        if (openedScreens.size() > 0) {
            Screen screenToRemove = openedScreens.get(openedScreens.size() - 1);
            openedScreens.remove(openedScreens.size() - 1);
            if (openedScreens.size() == 0) {
                return true;
            }
            Screen screen = openedScreens.get(openedScreens.size() - 1);
            openedScreens.remove(openedScreens.size() - 1);
            openScreen(screen);

            return false;
        }
        return true;
    }


    public enum Screen {
        MENU,
        MFH,
        TEST,

    }

    public void openScreen(Screen screen){
        mFragmentManager = Shared.activity.getFragmentManager();
        Fragment fragment = getFragment(screen);
        if(fragment!= null){
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,fragment, "CURRENT_FRAG");
            fragmentTransaction.commit();
            mFragmentManager.executePendingTransactions();
        }
        openedScreens.add(screen);

    }
    //overloaded method to pass in data for mapfragment
    public void openScreen(Screen screen, String location){
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
        }
        return frag;
    }
}
