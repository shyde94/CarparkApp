package Fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import utilities.ParseJSON;
import com.example.android.carparkappv1.R;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

import utilities.NetworkUtils;


public class MenuFragment extends Fragment {
    Button button;
    Button mViewSaveLot;
    EditText mInputLocation;
    TextView mLocationDisplay;
    ParseJSON parser;

    OnSearchButtonClickedListener mListener;

    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_fragment, container, false);
        button = (Button) view.findViewById(R.id.search_button);
        mViewSaveLot = (Button) view.findViewById(R.id.view_saved_lot);
        mInputLocation = (EditText) view.findViewById(R.id.Search_location);
        mLocationDisplay = (TextView) view.findViewById(R.id.location_input);
        parser = new ParseJSON();
        makeSearchQuery();
        button.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        try {
                            buttonClicked(view);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
        mViewSaveLot.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        
                    }
                }
        );
        return view;
    }

    /**
     * This method
     * @param v
     * @throws IOException
     */
    public void buttonClicked(View v) throws IOException {
        String location = mInputLocation.getText().toString();
        if(!location.equals("")){
            mListener.onSearchedButtonClicked(mInputLocation.getText().toString());
        }
        try {
            parser.sortThisJson();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public interface OnSearchButtonClickedListener {
        public void onSearchedButtonClicked(String location) throws IOException;

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnSearchButtonClickedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnSearchButtonClickedListener");
        }
    }


    public void makeSearchQuery(){
        URL url = NetworkUtils.buildUrl();
        new Search().execute(url);

    }
    public class Search extends AsyncTask<URL, Void, String> {

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
            parser.setDataFromDM(searchResults);
        }
    }

}
