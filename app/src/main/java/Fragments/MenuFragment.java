package Fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.android.carparkappv1.R;
import com.example.android.carparkappv1.Shared;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

import utilities.NetworkUtils;
import utilities.ParseJSON;


public class MenuFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks{
    Button button;
    Button mViewSaveLot;
    Button mFindPetrolStation;
    //EditText mInputLocation;
    //TextView mLocationDisplay;
    ParseJSON parser;
    AutoCompleteTextView mAutocompleteTextView;
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private static final String TAG = "MenuFragment";
    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(1.2, 103), new LatLng(1.5, 104));
    OnSearchButtonClickedListener mListener;

    /**
     *This method creates and return a view for the menufragment
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_fragment, container, false);



        mAutocompleteTextView = (AutoCompleteTextView) view.findViewById(R.id.location_input);
        button = (Button) view.findViewById(R.id.search_button);
        mFindPetrolStation = (Button) view.findViewById(R.id.search_nearest_station);
        mViewSaveLot = (Button) view.findViewById(R.id.view_saved_lot);
       // mInputLocation = (EditText) view.findViewById(R.id.Search_location);
        //mLocationDisplay = (TextView) view.findViewById(R.id.location_input);
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .build();
        mGoogleApiClient.connect();

        mAutocompleteTextView.requestFocus();
        InputMethodManager imm = (InputMethodManager)
                getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null){
            Log.i(TAG, "imm?");
            //imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
            imm.showSoftInput(mAutocompleteTextView, InputMethodManager.SHOW_IMPLICIT);

        }
        mAutocompleteTextView.setThreshold(3);
        mAutocompleteTextView.setOnItemClickListener(mAutocompleteClickListener);
        mPlaceArrayAdapter = new PlaceArrayAdapter(getActivity(), android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, null);
        mAutocompleteTextView.setAdapter(mPlaceArrayAdapter);
        parser = new ParseJSON();
        makeSearchQuery();
        button.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        if(isNetworkAvailable()!=false){
                        try {
                            buttonClicked(view);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else
                        {
                            Toast.makeText(getActivity(), "No Network Detected", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
        mFindPetrolStation.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(isNetworkAvailable()!=false){
                        try {
                            findPetrolStations(view);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                        else
                        {
                            Toast.makeText(getActivity(), "No Network Detected", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );

        mViewSaveLot.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences sharedPref = getActivity().getSharedPreferences(SaveLotNumber.PREFS_NAME,Context.MODE_PRIVATE);
                        String lot = sharedPref.getString((getString(R.string.saved_lot_number)),"");
                        if(!(lot.equals(""))){
                            Toast.makeText(getActivity(), "Your car is parked at: "+lot, Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(getActivity(),"No lot saved", Toast.LENGTH_LONG).show();
                        }

                    }
                }
        );
        return view;
    }

    /**
     * This method is used when user clicks on one of the prediction
     * It will then update the field with the selected prediction details
     */
    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i(TAG, "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            Log.i(TAG, "Fetching details for ID: " + item.placeId);
        }
    };
    /**
     *This methods checks if the place query update is successful
     * If query is not successful, it will print a message in the log
     */
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(TAG, "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
            CharSequence attributions = places.getAttributions();

        }
    };
    /**
     * This method get the text from the autocompletetextview when the search button is clicked
     * If the text is not NULL it will take in that and disconnect from the google API
     * Else it will print out an error message
     * @param v
     * @throws IOException
     */
    public void buttonClicked(View v) throws IOException {
        Shared.choice = 0;
        try {
            parser.sortThisJson();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String location = mAutocompleteTextView.getText().toString();
        if(!location.equals("")){
            mGoogleApiClient.disconnect();
            mListener.onSearchedButtonClicked(mAutocompleteTextView.getText().toString());
        }
        else{
            Toast.makeText(getActivity(), "You have not entered a valid location", Toast.LENGTH_LONG).show();
        }

    }

    /**
     *This method finds the petrol station on the map
     * @param view
     * @throws IOException
     */
    private void findPetrolStations(View view) throws IOException {
        Shared.choice = 1;
        mListener.onSearchedButtonClicked("");

    }

    /**
     * This method executes the search when searchbutton is clicked
     */
    public interface OnSearchButtonClickedListener {
        public void onSearchedButtonClicked(String location) throws IOException;

    }

    /**
     * This method checks if the OnSearchButtonClickedListner has been implemented
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnSearchButtonClickedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnSearchButtonClickedListener");
        }
    }

    /**
     *This method executes a search based on the URL created
     */
    public void makeSearchQuery(){
        URL url = NetworkUtils.buildUrl();
        new Search().execute(url);

    }

    /**
     * This class is used to search for results
     */
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

    /**
     *This method turns on the connection to the google API
     * @param bundle
     */
    @Override
    public void onConnected(Bundle bundle) {
        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        Log.i(TAG, "Google Places API connected.");

    }

    /**
     * This method prints out a log entry when the the connection fails
     * @param connectionResult
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

        /*Toast.makeText(this,
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();*/
    }

    /**
     * This method turns off the google API client
     * @param i
     */
    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
        Log.e(TAG, "Google Places API connection suspended.");
    }

    /**
     * This methods checks if the device is connected to a network
     * This prevents the app from crashing when there is no network
     * It will return true if network is true else it will return false
     * @return
     */
    private boolean isNetworkAvailable() {// checks to see if there is network connected
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}

