package Fragments;

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

import java.io.IOException;

/**
 * Created by Eimoh on 28/3/2017.
 */

public class SaveLotNumber extends Fragment{
    Button save, skip;
    EditText mInputLotNumber;
    TextView mLotNumberDisplay;

    public String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/aaTutorial";
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.save_lot_number, container, false);

        mInputLotNumber = (EditText) view.findViewById(R.id.lot_number_text);
        mLotNumberDisplay = (TextView) view.findViewById(R.id.skip_lot_button);




        return view;
    }




}
