package Fragments;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.carparkappv1.CarparkFinder;
import com.example.android.carparkappv1.R;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.net.URL;

import utilities.NetworkUtils;


public class TestFrag extends Fragment {

    //PullData pullData = new PullData();
    CarparkFinder cpFinder;

    TextView mtest1;
    TextView mtest2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.test_frag, container, false);
        mtest1 = (TextView) v.findViewById(R.id.test1);
        mtest2 = (TextView) v.findViewById(R.id.test2);
        //makeSearchQuery();
        LatLng ll = new LatLng(1.3826272,103.9430888);
        cpFinder = new CarparkFinder(ll, getActivity());
        String a = cpFinder.getCpController().dbToString();
        String b = cpFinder.getCpController().testCarparks();

        Log.i("TestFrag", "String a : " + a);
        Log.i("TestFrag", "String b : " + b);

        return v;
    }

    /*
    public void makeSearchQuery() {
        URL url = NetworkUtils.buildUrl();
        mtest1.setText(url.toString());
        new Search().execute(url);

    }

    public class Search extends AsyncTask<URL, Void, String>{

        @Override
        protected String doInBackground(URL... urls) {
            URL searchUrl = urls[0];
            String searchResults = null;
            try {
                Log.i("Results", "inside");
                searchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);

                Log.i("Results", "Results: " + searchResults);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return searchResults;
        }

        @Override
        protected void onPostExecute(String searchResults) {
            mtest1.setText(searchResults);
        }
    }*/
}
