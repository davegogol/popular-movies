package com.example.android.popularmovies.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.android.popularmovies.R;
import com.squareup.picasso.Picasso;

/**
 * Details Activity represents the Activity which empowers
 * the visualization of a single movie details.
 */
public class DetailsActivity extends AppCompatActivity {
    private TextView movieTitleTextView;
    private TextView movieOverviewTextView;
    private TextView movieReleaseDateTextView;
    private TextView movieAverageRateTextView;
    private ImageView moviePosterImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        movieTitleTextView = (TextView) findViewById(R.id.details_movie_title);
        movieOverviewTextView = (TextView) findViewById(R.id.details_movie_overview);
        movieReleaseDateTextView = (TextView) findViewById(R.id.details_movie_release_date);
        movieAverageRateTextView = (TextView) findViewById(R.id.details_movie_average_rate);
        moviePosterImageView = (ImageView) findViewById(R.id.movie_single_img);
        Intent intent = getIntent();
        if(intent != null && intent.hasExtra("movie.title") && intent.hasExtra("movie.poster")) {
            String title = intent.getStringExtra("movie.title");
            String poster = intent.getStringExtra("movie.poster");
            String releaseDate = intent.getStringExtra("movie.release_date");
            String averageRate = intent.getStringExtra("movie.average_rate");
            String overview = intent.getStringExtra("movie.overview");
            movieTitleTextView.setText(title);
            movieOverviewTextView.setText(overview);
            movieReleaseDateTextView.setText(releaseDate);
            movieAverageRateTextView.setText(averageRate);
            Picasso.
                    with(this).
                    load("http://image.tmdb.org/t/p/w150"+ poster).
                    into(moviePosterImageView);

        }
    }
}
