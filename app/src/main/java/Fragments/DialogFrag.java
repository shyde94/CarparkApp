package Fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.example.android.carparkappv1.Shared;

import Carparks.Carpark;
import Carparks.HdbCarpark;

/**
 * Created by jon92 on 23/3/2017.
 */


public class DialogFrag extends DialogFragment {

    private Carpark carpark;

    /**
     * Sets the carpark object that is related to the selected carpark
     * @param cp Carpark object
     */
    public void setCarpark(Carpark cp){
            carpark = cp;
        }

    /**
     *Creates a Dialog object
     *Uses Carpark object CP to decide the text that will be used for the dialog box
     *It will then set the text as the mesasge for the dialog box and also create the necessary buttons
     * @param savedInstanceState
     * @return Returns created Dialog with the desired information
     */
    @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient DialogFrag construction
            String carpark_details = ""; String cpNum = "";
            String displayInfo = carpark.displayInfo();
            String title = carpark.title();


            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(displayInfo).setTitle(title)
                    .setPositiveButton("Select Carpark", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            double lat = carpark.getLatLonCoord().getLatitude();
                            double lng = carpark.getLatLonCoord().getLongitude();
                            String mapsApp = "http://maps.google.com/maps?daddr="+lat+","+lng;
                            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                    Uri.parse(mapsApp));
                            startActivity(intent);
                            Shared.carpark = carpark;
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }