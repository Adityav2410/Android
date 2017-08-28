package com.example.android.quakereport;

import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class SettingsActivity extends AppCompatActivity {

    private static final String LOG_TAG = "SettingsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }


    public static class EarthquakePreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener{

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Log.d(LOG_TAG,"DEZBUG \t OnCreate Method");

            // Preference stuff
            addPreferencesFromResource(R.xml.settings_main);
            Preference minMagnitude = findPreference(getString(R.string.settings_min_magnitude_key));
            Preference orderBy = findPreference(getString(R.string.settings_order_by_key));

            bindPreferenceSummaryToValue(minMagnitude, orderBy);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {

            String stringValue =  value.toString();
            if( preference instanceof ListPreference){
                int prefIndex = ((ListPreference) preference).findIndexOfValue(stringValue);
                if(prefIndex >= 0){
                    CharSequence [] labels = ((ListPreference) preference).getEntries();
                    preference.setSummary(labels[prefIndex]);
                }
            }else{
                preference.setSummary(stringValue);
            }
            Log.d(LOG_TAG,"DEZBUG \t Updating summary with value = " + stringValue);
            return true;
        }

        private void bindPreferenceSummaryToValue(Preference preferenceMinValue, Preference preferenceOrderBy){
            Log.d(LOG_TAG,"DEZBUG \t Inside bindPreferencceSummary");

            preferenceMinValue.setOnPreferenceChangeListener(this);
            preferenceOrderBy.setOnPreferenceChangeListener(this);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preferenceMinValue.getContext());
            String preferenceString = preferences.getString(preferenceMinValue.getKey(),"No Initial Value set");
            onPreferenceChange(preferenceMinValue, preferenceString);

            preferenceString = preferences.getString(preferenceOrderBy.getKey(),"No Initial Value set");
            onPreferenceChange(preferenceOrderBy, preferenceString);

        }


    }
}
