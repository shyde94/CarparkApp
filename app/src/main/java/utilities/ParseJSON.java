package utilities;

import android.util.Log;

import com.example.android.carparkappv1.Shared;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import Controllers.CarparkDBController;
import MapProjectionConverter.SVY21;
import MapProjectionConverter.SVY21Coordinate;

/**
 * ParseJson takes in a String object and uses the data in the string to update the table DMCarparks
 */

public class ParseJSON {
    public static ParseJSON mInstance = null;
    private String dataFromDM;
    private JSONObject obj;

    public static ParseJSON getmInstance(){
        if(mInstance == null){
            return new ParseJSON();
        }
        else{
            return mInstance;
        }
    }

    public String getDataFromDM() {
        return dataFromDM;
    }

    public void setDataFromDM(String dataFromDM) {
        this.dataFromDM = dataFromDM;
    }

    public JSONObject getObj() {
        return obj;
    }

    public void setObj(JSONObject obj) {
        this.obj = obj;
    }


    public void sortThisJson() throws JSONException, IOException {
        /*
        File path = Shared.context.getFilesDir();
        File file = new File(path,"DM_data.csv");
        FileOutputStream stream = new FileOutputStream(file);
        Log.i("Parser", path.getAbsolutePath());*/


        obj = new JSONObject(dataFromDM);
        JSONArray jArray = obj.getJSONArray("value");
        //FileWriter fileWriter = new FileWriter(file);

        //Need Lots and CarParkID.
        //Everytime "Search!" button is clicked, lots in each carpark will be extracted.
        //Use CarParkID to update number of Lots in DB.
        for(int i=0;i<jArray.length();i++){
            JSONObject obj2 = jArray.getJSONObject(i);
            String cpNum = obj2.getString("CarParkID");
            int lots = obj2.getInt("Lots");


            //Don't really need this
            String area = obj2.getString("Area");
            String dev = obj2.getString("Development");
            double lat = obj2.getDouble("Latitude");
            double lng = obj2.getDouble("Longitude");
            SVY21Coordinate svy21 = SVY21.computeSVY21(lat,lng);
            double easting = svy21.getEasting();
            double northing = svy21.getNorthing();

            CarparkDBController.getInstance(Shared.context).queryUpdateTableDMCarparkLots(cpNum, lots);
            CarparkDBController.getInstance(Shared.context).close();

            String line = cpNum+","+area+","+dev+","+easting+","+northing+","+lots;
            Log.i("Parser", line);
            //Log.i("Parser","CarparkID: " + carparkID + " Area: " + area + " Dev : " + dev + " Lat: " + lat + " lng: " + lng + " lots: " + lots);
            //stream.write(line.getBytes());
        }

        //Log.i("Parser", obj.toString());
    }
}
