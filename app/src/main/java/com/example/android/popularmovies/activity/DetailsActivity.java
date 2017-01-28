package com.example.android.popularmovies.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.android.popularmovies.R;


/**
 * Details Activity represents the Activity which empowers
 * the visualization of a single movie details.
 */
public class DetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
    }
}
