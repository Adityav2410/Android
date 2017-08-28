package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adityav on 8/26/17.
 */

public class EarthquakeLoader extends AsyncTaskLoader<ArrayList<EqInfo>>{

    private String mQueryURL = "";
    public EarthquakeLoader(Context context, String queryURL){
        super(context);
        mQueryURL = queryURL;
    }
    @Override
    public ArrayList<EqInfo> loadInBackground() {
        ArrayList<EqInfo> eqInfos = QueryUtils.extractEarthquakes(mQueryURL);
        return eqInfos;
    }
}
