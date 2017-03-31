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

import com.example.android.carparkappv1.R;
import com.google.android.gms.maps.MapFragment;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Eimoh on 28/3/2017.
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

        save.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String lotNumber = mInputLotNumber.getText().toString();
                        SharedPreferences sharedPref = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(getString(R.string.saved_lot_number), lotNumber);
                        editor.commit();
                    }
                }
        );

        return view;
    }




}
