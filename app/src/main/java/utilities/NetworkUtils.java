package utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Networkutils handles the sending of a httprequest object to collect data for live number of lots. It receives the reply in the form of a json object and sends it to ParseJson to handle
 */

public class NetworkUtils {

    private static final String LTALINK_BASE_URL = "http://datamall2.mytransport.sg/ltaodataservice/CarParkAvailability";

    private static final String HEADER_KEY = "AccountKey";

    private static final String KEY = "yW4hPlksSb2Wjx6rIFPU0g==";

    public static URL buildUrl() {
        Uri builtUri = Uri.parse(LTALINK_BASE_URL).buildUpon()
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestProperty(HEADER_KEY, KEY);
        urlConnection.setRequestMethod("GET");

        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

}
