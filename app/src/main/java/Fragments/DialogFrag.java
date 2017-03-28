package Fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.example.android.carparkappv1.Carpark;

/**
 * Created by jon92 on 23/3/2017.
 */

public class DialogFrag extends DialogFragment {

        private Carpark carpark;

        public void setCarpark(Carpark cp){
            carpark = cp;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient DialogFrag construction
            String carpark_details = "";
            String cpAddress = carpark.getAddress() + "\n";
            String cpRate = "";
            String cpType = carpark.getTypeOfParkingSystem() + "\n";
            String cpNum = carpark.getCpNum();
            carpark_details = cpAddress + cpRate + cpType;


            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(carpark_details).setTitle(cpNum)
                    .setPositiveButton("Select Carpark", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            double lat = carpark.getLatLonCoord().getLatitude();
                            double lng = carpark.getLatLonCoord().getLongitude();
                            String mapsApp = "http://maps.google.com/maps?daddr="+lat+","+lng;
                            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                    Uri.parse(mapsApp));
                            startActivity(intent);
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }