package Fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android.carparkappv1.R;

import java.io.IOException;


public class MenuFragment extends Fragment {
    Button button;
    EditText mInputLocation;
    TextView mLocationDisplay;

    OnSearchButtonClickedListener mListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_fragment, container, false);
        button = (Button) view.findViewById(R.id.search_button);
        mInputLocation = (EditText) view.findViewById(R.id.Search_location);
        mLocationDisplay = (TextView) view.findViewById(R.id.location_input);



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




        return view;
    }
    public void buttonClicked(View v) throws IOException {
        String location = mInputLocation.getText().toString();
        if(!location.equals("")){
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
}
