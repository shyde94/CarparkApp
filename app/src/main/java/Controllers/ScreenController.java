package Controllers;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import com.example.android.carparkappv1.R;

import Fragments.MapFragmentHolder;
import Fragments.MenuFragment;


public class ScreenController {
    private static ScreenController mInstance = null;
    private FragmentManager mFragmentManager;


    private ScreenController() {
    }

    public static ScreenController getInstance() {
        if (mInstance == null) {
            mInstance = new ScreenController();
        }
        return mInstance;
    }

    public enum Screen {
        MENU,
        MFH,

    }

    public void openScreen(Screen screen){
        Fragment fragment = getFragment(screen);
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,fragment);
        fragmentTransaction.commit();

    }

    public Fragment getFragment(Screen screen){
        Fragment frag = null;
        switch(screen){
            case MENU:
                frag = new MenuFragment();
                break;
            case MFH:
                frag = new MapFragmentHolder();
        }
        return frag;
    }
}
