package com.example.android.popularmovies.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.adapter.ReviewAdapter;
import com.example.android.popularmovies.adapter.TrailerAdapter;
import com.example.android.popularmovies.domain.Review;
import com.example.android.popularmovies.domain.Trailer;
import com.example.android.popularmovies.service.MovieServiceImpl;
import com.example.android.popularmovies.task.AsyncTaskCompleteListener;
import com.example.android.popularmovies.task.FetchReviewDataTask;
import com.example.android.popularmovies.task.FetchTrailerDataTask;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

/**
 * Details Activity represents the Activity which empowers
 * the visualization of a single movie details.
 */
public class DetailsActivity extends AppCompatActivity {
    private static final String IMAGE_API = "http://image.tmdb.org/t/p/w150";
    private TextView movieTitleTextView;
    private TextView movieOverviewTextView;
    private TextView movieReleaseDateTextView;
    private TextView movieAverageRateTextView;
    private ImageView moviePosterImageView;

    private ProgressBar mLoadingReviewsIndicator;
    private ProgressBar mLoadingTrailersIndicator;

    private TrailerAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;

    private RecyclerView mTrailersRecyclerView;
    private RecyclerView mReviewsRecyclerView;
    private List<Trailer> trailersList;
    private List<Review> reviewsList;

    private TextView mNoTrailers;
    private TextView mNoReviews;

    private ImageView mTrailersError;
    private ImageView mReviewsError;

    private MovieServiceImpl movieService = new MovieServiceImpl();
    private String movieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_details);
        movieTitleTextView = (TextView) findViewById(R.id.details_movie_title);
        movieOverviewTextView = (TextView) findViewById(R.id.details_movie_overview);
        movieReleaseDateTextView = (TextView) findViewById(R.id.details_movie_release_date);
        movieAverageRateTextView = (TextView) findViewById(R.id.details_movie_average_rate);
        moviePosterImageView = (ImageView) findViewById(R.id.movie_single_img);
        mNoReviews = (TextView)  findViewById(R.id.no_reviews);
        mNoTrailers = (TextView)  findViewById(R.id.no_trailers);
        mLoadingReviewsIndicator = (ProgressBar) findViewById(R.id.pb_loading_reviews_indicator);
        mLoadingTrailersIndicator = (ProgressBar)findViewById(R.id.pb_loading_trailers_indicator);

        mTrailersError = (ImageView) findViewById(R.id.trailers_error);
        mReviewsError = (ImageView) findViewById(R.id.reviews_error);

        Intent intent = getIntent();
        if(intent != null && intent.hasExtra("movie.title") && intent.hasExtra("movie.poster")) {
            String title = intent.getStringExtra("movie.title");
            String poster = intent.getStringExtra("movie.poster");
            String releaseDate = intent.getStringExtra("movie.release_date");
            String averageRate = intent.getStringExtra("movie.average_rate");
            String overview = intent.getStringExtra("movie.overview");
            movieId = intent.getStringExtra("movie.id");
            movieTitleTextView.setText(title);
            movieOverviewTextView.setText(overview);
            movieReleaseDateTextView.setText(releaseDate);
            movieAverageRateTextView.setText(averageRate);
            Picasso.
                    with(this).
                    load(IMAGE_API + poster).
                    into(moviePosterImageView);
        }

        mTrailersRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_trailers);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mTrailersRecyclerView.setLayoutManager(layoutManager);
        mTrailersRecyclerView.setHasFixedSize(true);

        mTrailerAdapter = new TrailerAdapter();
        mTrailersRecyclerView.setAdapter(mTrailerAdapter);

        loadTrailersData();

        mReviewsRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_reviews);
        LinearLayoutManager layoutManager2
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mReviewsRecyclerView.setLayoutManager(layoutManager2);
        mReviewsRecyclerView.setHasFixedSize(true);

        mReviewAdapter = new ReviewAdapter();
        mReviewsRecyclerView.setAdapter(mReviewAdapter);

        loadReviewsData();
    }

    private void loadTrailersData() {
        new FetchTrailerDataTask(movieService, new FetchMovieTrailersTaskCompleteListener()).
                execute(movieId);
    }

    private void loadReviewsData() {
        new FetchReviewDataTask(movieService, new FetchMovieReviewsTaskCompleteListener()).
                execute(movieId);
    }

    public class FetchMovieTrailersTaskCompleteListener implements AsyncTaskCompleteListener<List<Trailer>,
            Map<String,Object>> {
        @Override
        public void onPreTaskExecute() {
            mTrailersError.setVisibility(View.INVISIBLE);
            mLoadingTrailersIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        public void onTaskComplete(List<Trailer> listMovieTrailersResult, Map<String,Object> properties ) {
            trailersList = listMovieTrailersResult;
            mTrailerAdapter.setTrailersData(trailersList);
            if(trailersList != null && trailersList.size() == 0) mNoTrailers.setVisibility(View.VISIBLE);
            mLoadingTrailersIndicator.setVisibility(View.INVISIBLE);

            if(!(Boolean) properties.get("internetState")){
                mTrailersError.setVisibility(View.VISIBLE);
            }
        }
    }

    public class FetchMovieReviewsTaskCompleteListener implements AsyncTaskCompleteListener<List<Review>,
            Map<String,Object>> {
        @Override
        public void onPreTaskExecute() {
            mReviewsError.setVisibility(View.INVISIBLE);
            mLoadingReviewsIndicator.setVisibility(View.VISIBLE);
        }
        @Override
        public void onTaskComplete(List<Review> listMovieReviewsResult, Map<String,Object> properties ) {
            reviewsList = listMovieReviewsResult;
            mReviewAdapter.setReviewsListData(reviewsList);
            if(reviewsList != null && reviewsList.size() == 0) mNoReviews.setVisibility(View.VISIBLE);

            if(!(Boolean) properties.get("internetState")){
                mReviewsError.setVisibility(View.VISIBLE);
            }
            mLoadingReviewsIndicator.setVisibility(View.INVISIBLE);
        }
    }
}
