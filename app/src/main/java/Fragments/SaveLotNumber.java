package Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.Manifest;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.pm.PackageManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.carparkappv1.MainActivity;
import com.example.android.carparkappv1.R;
import com.google.android.gms.maps.MapFragment;

import java.io.FileOutputStream;
import java.io.IOException;

import Controllers.ScreenController;

/**
 *
 */

public class SaveLotNumber extends Fragment{
    Button save, skip;
    EditText mInputLotNumber;
    TextView mLotNumberDisplay;

    public static final String PREFS_NAME = "saved_lot_number";

    public String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/aaTutorial";
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.save_lot_number, container, false);

        mInputLotNumber = (EditText) view.findViewById(R.id.lot_number_text);
        mLotNumberDisplay = (TextView) view.findViewById(R.id.skip_lot_button);
        save = (Button) view.findViewById(R.id.save_lot_button);
        skip = (Button) view.findViewById(R.id.skip_lot_button);

        save.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String lotNumber = mInputLotNumber.getText().toString();
                        if(!(lotNumber).equals("")){
                            SharedPreferences sharedPref = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString(getString(R.string.saved_lot_number), lotNumber);
                            editor.commit();
                            Toast.makeText(getActivity(), "Your carpark lot number has been saved", Toast.LENGTH_LONG).show();
                            ScreenController.getInstance().revertToPreviousScreen();
                        }
                        else{
                            Toast.makeText(getActivity(), "You have not entered anything", Toast.LENGTH_LONG).show();
                        }


                    }
                }
        );

        skip.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //If user skips this step, clear this!
                        SharedPreferences sharedPref = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(getString(R.string.saved_lot_number), "");
                        editor.commit();
                        ScreenController.getInstance().revertToPreviousScreen();
                    }
                }
        );

        return view;
    }




}
