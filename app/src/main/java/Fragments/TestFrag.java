package Fragments;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.carparkappv1.CarparkFinder;
import com.example.android.carparkappv1.R;
import com.google.android.gms.maps.model.LatLng;


public class TestFrag extends Fragment {

    CarparkFinder cpFinderTest;

    TextView mtest1;
    TextView mtest2;
    LatLng ll = new LatLng(30,30);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.test_frag, container, false);
        mtest1 = (TextView) v.findViewById(R.id.test1);
        mtest2 = (TextView) v.findViewById(R.id.test2);

        cpFinderTest = new CarparkFinder(ll, getActivity());
        String testing = cpFinderTest.getCpController().dbToString();
        Cursor cursor = cpFinderTest.getCpController().querySomething();
        mtest1.setText(testing);

        return v;
    }
}
