package com.example.android.quakereport;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by adityav on 8/26/17.
 */

public class UrlQueryHandler {

    public static final String LOG_TAG = "URLQueryHandler";


    public void URLQueryHandler(){
//        url = null;
//        stringURL = "";
    }

    public String getJSONResponseFromStringURL(String stringURL){
        URL url = createURL(stringURL);
        String jsonResponse = "";
        try{
            jsonResponse = makeHTTPsRequest(url);
        }catch(IOException exception){

        }
        return( jsonResponse);
    }

    public URL createURL(String stringURL){
        URL url = null;
        try{
            url = new URL(stringURL);
        }catch(MalformedURLException exception){
            Log.e(LOG_TAG,"Error with creating URL", exception);
            return null;
        }
        return url;
    }

    public String makeHTTPsRequest(URL url) throws IOException{
        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();
            if( urlConnection.getResponseCode() != 200 ){
                Log.e(LOG_TAG, "Status Code: " + urlConnection.getResponseCode() );
                return( jsonResponse );
            }
            inputStream = urlConnection.getInputStream();
            jsonResponse = readFromStream(inputStream);
        }catch(IOException exception){
            exception.printStackTrace();
        }finally {
            if( urlConnection != null )
                urlConnection.disconnect();
            if( inputStream != null )
                inputStream.close();
        }
        return(jsonResponse);
    }


    private String readFromStream(InputStream inputStream) throws IOException{
        StringBuffer stringBuffer = new StringBuffer();

        if( inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while( line != null ){
                stringBuffer.append(line);
                line = reader.readLine();
            }
        }
        return(stringBuffer.toString());
    }


}
