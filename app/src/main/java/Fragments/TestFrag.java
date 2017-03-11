package Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.carparkappv1.R;

/**
 * Created by Shide on 11/3/17.
 */

public class TestFrag extends Fragment {

    TextView mtest1;
    TextView mtest2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.test_frag, container, false);
        mtest1 = (TextView) v.findViewById(R.id.test1);
        mtest2 = (TextView) v.findViewById(R.id.test2);
        return v;
    }
}
