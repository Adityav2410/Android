/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static android.provider.LiveFolders.INTENT;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<EqInfo>> {
    private ArrayList<EqInfo> earthquakes;
    private ListView earthquakeListView;
    EqAdapter eqAdapter;
    private TextView mEmptyStateTextView ;
    private ProgressBar mProgressSpinner;
    private String mQueryURL = "https://earthquake.usgs.gov/fdsnws/event/1/query";

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    protected boolean isNetworkConnected(Context context){
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    protected String getPreferenceQueryURL(){
        Uri baseUri = Uri.parse(mQueryURL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String minMagnitude = sharedPrefs.getString( getString(R.string.min_magnitude_key),getString(R.string.min_magnitude_default_value));
        String orderBy = sharedPrefs.getString( getString(R.string.settings_order_by_key), getString(R.string.settings_order_by_default) );

        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", "50");
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", "time");
        uriBuilder.appendQueryParameter("orderby", orderBy);
        return uriBuilder.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        // Find a reference to the {@link ListView} in the layout
        earthquakeListView = (ListView) findViewById(R.id.list_view);
        mEmptyStateTextView = (TextView) findViewById(R.id.tv_emptyListState);
        mProgressSpinner = (ProgressBar) findViewById(R.id.progress_spinner);

        // Establish Adapter with the ListView
        eqAdapter = new EqAdapter(this, new ArrayList<EqInfo>());
        earthquakeListView.setAdapter(eqAdapter);
        earthquakeListView.setEmptyView(mEmptyStateTextView);


        // Initialize loader to retrieve data from internet and accordingly update the adapter
        if( isNetworkConnected(this) ){
            getLoaderManager().initLoader(0,null,this).forceLoad();
        }else{
            mEmptyStateTextView.setText("No Internet Connection");
            mProgressSpinner.setVisibility(View.GONE);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<ArrayList<EqInfo>> onCreateLoader(int i, Bundle bundle) {
        System.out.println("DEZBUG: Inside Create Loader" );
        mQueryURL = getPreferenceQueryURL();
        return new EarthquakeLoader(EarthquakeActivity.this,mQueryURL );
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<EqInfo>> loader, ArrayList<EqInfo> eqInfos) {
        // establish adapter link with the data and update the listview
        mProgressSpinner.setVisibility(View.GONE);
        System.out.println("DEZBUG: Inside Loader Finished" );
        earthquakes = eqInfos;
        eqAdapter.setData(eqInfos);
        mEmptyStateTextView.setText("No Earthquake data found");
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                openUrlForRelevantEarthquake(position);
            }
        });
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<EqInfo>> loader) {
        System.out.println("DEZBUG: Inside Loader Reset" );
        eqAdapter.setData(new ArrayList<EqInfo>());
        earthquakes = new ArrayList<EqInfo>();
    }

    private void openUrlForRelevantEarthquake(int position){
        EqInfo  currEqInfo  =   earthquakes.get(position);
        String  currUrl     =   currEqInfo.getUrl();
        Uri     webpage     =   Uri.parse(currUrl);
        Intent intent       =   new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }

    }

}
