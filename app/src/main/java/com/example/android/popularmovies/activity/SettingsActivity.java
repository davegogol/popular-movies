package com.example.android.popularmovies.activity;

import android.preference.PreferenceActivity;
import android.os.Bundle;
import com.example.android.popularmovies.R;

/**
 * This class represents the settings activity for setting up user preferences.
 */
public class SettingsActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
