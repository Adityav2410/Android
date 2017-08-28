package com.example.android.quakereport;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    /** Sample JSON response for a USGS query */
    private static final String EqURLString = " https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=5&limit=50";
    private static String mQueryURL = EqURLString;
    private static String JSON_RESPONSE = "";
    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Return a list of {@link EqInfo} objects that has been built up from
     * parsing a JSON response.
     */
    private static void getLocationParts(String []locations, String loc){
        int index = 0 ;
        if((index = loc.indexOf("of")) == -1 ){
            locations[0] = "Near";
            locations[1] = loc;
            return;
        }
        locations[0] = loc.substring(0,index+2);
        locations[1] = loc.substring(index+2);
    }

    public static ArrayList<EqInfo> extractEarthquakes(String queryURL) {
        mQueryURL = queryURL;

        JSON_RESPONSE = new UrlQueryHandler().getJSONResponseFromStringURL(mQueryURL);
        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<EqInfo> earthquakes = new ArrayList<>();
        SimpleDateFormat formatDate = new SimpleDateFormat("LLL dd, yyyy", Locale.US);
        SimpleDateFormat formatTime = new SimpleDateFormat("h:mm a");


        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.


        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            JSONObject  jsonObject = new JSONObject(JSON_RESPONSE);
            JSONArray   features    = jsonObject.optJSONArray("features");
            for(int i = 0; i < features.length(); ++i){
                JSONObject  properties  =   features.getJSONObject(i).optJSONObject("properties");

                // Magnitude
                Double      magnitude   =   properties.getDouble("mag");

                // Location - in the desired format.
                String      location       =   properties.getString("place");
                String      []locations     =   new String[2];
                getLocationParts(locations,location);

                //Get url
                String eq_url = properties.getString("url");

                // Time in the desired format
                Long        time        =   properties.getLong("time");
                Date        date        =   new Date(time);
                String      date_string =   formatDate.format(date);
                String      time_string =   formatTime.format(date);

                // Assign the constructor.
                earthquakes.add(new EqInfo(magnitude, locations[0], locations[1], date_string,time_string, eq_url));
            }
            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }

}