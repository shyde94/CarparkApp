package utilities;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Shide on 30/3/17.
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


    public void sortThisJson() throws JSONException {
        obj = new JSONObject(dataFromDM);
        Log.i("Parser", obj.toString());
    }
}
