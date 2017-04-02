package Fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.carparkappv1.R;
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
    EditText mInputLocation;
    TextView mLocationDisplay;
    ParseJSON parser;
    AutoCompleteTextView mAutocompleteTextView;
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private static final String TAG = "MenuFragment";
    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(-85, -180), new LatLng(85, 180));
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
        //mLocationDisplay = (TextView) view.findViewById(R.id.location_input);
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .build();
        mGoogleApiClient.connect();
        mAutocompleteTextView = (AutoCompleteTextView) view.findViewById(R.id.location_input);
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

            //mNameTextView.setText(Html.fromHtml(place.getName() + ""));
           /* mAddressTextView.setText(Html.fromHtml(place.getAddress() + ""));
            mIdTextView.setText(Html.fromHtml(place.getId() + ""));
            mPhoneTextView.setText(Html.fromHtml(place.getPhoneNumber() + ""));
            mWebTextView.setText(place.getWebsiteUri() + "");
            if (attributions != null) {
                mAttTextView.setText(Html.fromHtml(attributions.toString()));
            }*/
        }
    };
    /**
     * This method
     * @param v
     * @throws IOException
     */
    public void buttonClicked(View v) throws IOException {
        try {
            parser.sortThisJson();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String location = mInputLocation.getText().toString();
        if(!location.equals("")){
            mGoogleApiClient.disconnect();
            mListener.onSearchedButtonClicked(mInputLocation.getText().toString());
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
    @Override
    public void onConnected(Bundle bundle) {
        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        Log.i(TAG, "Google Places API connected.");

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

        /*Toast.makeText(this,
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();*/
    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
        Log.e(TAG, "Google Places API connection suspended.");
    }
}

