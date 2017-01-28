package com.example.android.popularmovies.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import com.example.android.popularmovies.R;

/**
 * Details Activity represents the Activity which empowers
 * the visualization of a single movie details.
 */
public class DetailsActivity extends AppCompatActivity {
    private TextView movieTitleTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        movieTitleTextView = (TextView) findViewById(R.id.details_movie_title);
        Intent intent = getIntent();
        if(intent != null && intent.hasExtra("movie.title") && intent.hasExtra("movie.poster")) {
            String title = intent.getStringExtra("movie.title");
            String poster = intent.getStringExtra("movie.poster");
            String releaseDate = intent.getStringExtra("movie.release_date");
            String averageRate = intent.getStringExtra("movie.average_rate");
            String overview = intent.getStringExtra("movie.overview");
            movieTitleTextView.setText(title + poster + releaseDate + averageRate + overview);
        }
    }
}
