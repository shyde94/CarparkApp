package Fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import Carparks.Carpark;
import Carparks.HdbCarpark;

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
            String carpark_details = ""; String cpNum = "";
            if(carpark instanceof HdbCarpark){
                String cpAddress = ((HdbCarpark)carpark).getAddress() + "\n";
                String cpRate = "";
                String cpType = ((HdbCarpark)carpark).getTypeOfParkingSystem() + "\n";
                cpNum = ((HdbCarpark)carpark).getCpNum();
                carpark_details = cpAddress + cpRate + cpType;

            }


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
                    })
                    .setNegativeButton("Arrived at Carpark", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }