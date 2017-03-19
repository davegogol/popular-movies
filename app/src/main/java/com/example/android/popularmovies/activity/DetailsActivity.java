package com.example.android.popularmovies.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.adapter.ReviewAdapter;
import com.example.android.popularmovies.adapter.TrailerAdapter;
import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.domain.Movie;
import com.example.android.popularmovies.domain.Review;
import com.example.android.popularmovies.domain.Trailer;
import com.example.android.popularmovies.service.MovieServiceImpl;
import com.example.android.popularmovies.service.exception.MovieServiceException;
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
public class DetailsActivity extends AppCompatActivity implements TrailerAdapter.TrailerAdapterOnClickHandler {
    public static final String YOUTUBE_URL = "http://www.youtube.com/watch?v=";
    private final String LOG = this.getClass().getSimpleName();
    private static final String IMAGE_API = "http://image.tmdb.org/t/p/w150";
    private TextView movieTitleTextView;
    private TextView movieOverviewTextView;
    private TextView movieReleaseDateTextView;
    private TextView movieAverageRateTextView;
    private ImageView moviePosterImageView;

    private ToggleButton favouriteToggleButton;

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
    private Movie movie = new Movie();

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
        favouriteToggleButton = (ToggleButton) findViewById(R.id.favouriteToggleButton);

        mTrailersError = (ImageView) findViewById(R.id.trailers_error);
        mReviewsError = (ImageView) findViewById(R.id.reviews_error);

        Intent intent = getIntent();

        if(intent != null && intent.hasExtra("movie.title") && intent.hasExtra("movie.poster")
                && intent.hasExtra("movie.release_date") && intent.hasExtra("movie.overview") &&
                intent.hasExtra("movie.average_rate")) {

            movie.setName(intent.getStringExtra("movie.title"));
            movie.setPosterPath(intent.getStringExtra("movie.poster"));
            movie.setReleaseDate(intent.getStringExtra("movie.release_date"));
            movie.setVoteAverage(Double.valueOf(intent.getStringExtra("movie.average_rate")));
            movie.setOverview(intent.getStringExtra("movie.overview"));
            movie.setId(intent.getStringExtra("movie.id"));

            movieTitleTextView.setText(movie.getName());
            movieOverviewTextView.setText(movie.getOverview());
            movieReleaseDateTextView.setText(movie.getReleaseDate());
            movieAverageRateTextView.setText(String.valueOf(movie.getVoteAverage()));

            Picasso.
                    with(this).
                    load(IMAGE_API + movie.getPosterPath()).
                    into(moviePosterImageView);

            Log.d(LOG, "Movie details retrieved by Intent");

        }else if (intent != null && intent.hasExtra("movie.id")){
            try {
                String movieId = intent.getStringExtra("movie.id");
                movie = movieService.getMovieById(movieId);

                movieTitleTextView.setText(movie.getName());
                movieOverviewTextView.setText(movie.getName());
                movieReleaseDateTextView.setText(movie.getReleaseDate());
                movieAverageRateTextView.setText(String.valueOf(movie.getVoteAverage()));

            } catch (MovieServiceException e) {
                Log.e(LOG, e.getMessage());
                return;
            }

            Log.d(LOG, "Movie details retrieved by API");

        }else
            return;

        initializeFavourite();

        mTrailersRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_trailers);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mTrailersRecyclerView.setLayoutManager(layoutManager);
        mTrailersRecyclerView.setHasFixedSize(true);

        mTrailerAdapter = new TrailerAdapter(this);
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

    /**
     * Initializes favourite button according to the favourites list of the user.
     * Defines also the listener on the button to check/uncheck the current movie as
     * favourite.
     */
    private void initializeFavourite() {

        Cursor cursor =
                getContentResolver().query(
                MovieContract.FavouriteEntry.CONTENT_URI.buildUpon().appendPath(movie.getId()).build(),
                null,
                null,
                new String[]{movie.getId()},
                null);

        boolean state = cursor.getCount() == 1;
        favouriteToggleButton.setChecked(state);

        if(state)
            favouriteToggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(),
                R.drawable.favourite));
        else
            favouriteToggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(),
                    R.drawable.nofavourite));

        favouriteToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    favouriteToggleButton.setBackgroundDrawable(
                            ContextCompat.getDrawable(getApplicationContext(), R.drawable.favourite));

                    insertData(movie.getId(), movie.getName(), movie.getPosterPath());
                    Log.d(LOG, "Checked Favourite successful");

                } else {
                    favouriteToggleButton.setBackgroundDrawable(
                            ContextCompat.getDrawable(getApplicationContext(), R.drawable.nofavourite));

                    getContentResolver().delete(
                            MovieContract.FavouriteEntry.CONTENT_URI.buildUpon().appendPath(movie.getId()).build(),
                            null,
                            new String[]{movie.getId()}
                    );
                    Log.d(LOG, "Unchecked Favourite successful");
                }
            }
        });
    }

    /**
     * Stores the movie details in the favourites
     * @param movieId
     * @param movieTitle
     * @param moviePoster
     */
    private void insertData(String movieId, String movieTitle, String moviePoster){
        Log.d(LOG, "> Save favourite");

        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.FavouriteEntry.COLUMN_MOVIE_ID, Integer.valueOf(movieId));
        contentValues.put(MovieContract.FavouriteEntry.COLUMN_MOVIE_NAME, movieTitle);
        contentValues.put(MovieContract.FavouriteEntry.COLUMN_MOVIE_POSTER, moviePoster);
        getContentResolver().
                insert(MovieContract.FavouriteEntry.CONTENT_URI.buildUpon().appendPath(movieId).build(),
                        contentValues);
        Log.d(LOG, "< Save favourite successful");
    }

    /**
     * Loads trailers movie related data.
     */
    private void loadTrailersData() {
        Log.d(LOG, "> Trailers to be loaded");
        new FetchTrailerDataTask(movieService, new FetchMovieTrailersTaskCompleteListener()).
                execute(movie.getId());
    }

    /**
     * Loads reviews movie related data.
     */
    private void loadReviewsData() {
        Log.d(LOG, "> Reviews to be loaded");
        new FetchReviewDataTask(movieService, new FetchMovieReviewsTaskCompleteListener()).
                execute(movie.getId());
    }

    /**
     * Listener for loading trailers movie data.
     */
    public class FetchMovieTrailersTaskCompleteListener implements AsyncTaskCompleteListener<List<Trailer>,
            Map<String,Object>> {
        @Override
        public void onPreTaskExecute() {
            mTrailersError.setVisibility(View.INVISIBLE);
            mLoadingTrailersIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        public void onTaskComplete(List<Trailer> listMovieTrailersResult, Map<String,Object> properties ) {
            Log.d(LOG, "< Trailers loaded");

            trailersList = listMovieTrailersResult;
            mTrailerAdapter.setTrailersData(trailersList);
            if(trailersList != null && trailersList.size() == 0) mNoTrailers.setVisibility(View.VISIBLE);
            mLoadingTrailersIndicator.setVisibility(View.INVISIBLE);

            if(!(Boolean) properties.get("internetState")){
                mTrailersError.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * Listener for loading reviews movie data.
     */
    public class FetchMovieReviewsTaskCompleteListener implements AsyncTaskCompleteListener<List<Review>,
            Map<String,Object>> {
        @Override
        public void onPreTaskExecute() {
            mReviewsError.setVisibility(View.INVISIBLE);
            mLoadingReviewsIndicator.setVisibility(View.VISIBLE);
        }
        @Override
        public void onTaskComplete(List<Review> listMovieReviewsResult, Map<String,Object> properties ) {
            Log.d(LOG, "< Reviews loaded");

            reviewsList = listMovieReviewsResult;
            mReviewAdapter.setReviewsListData(reviewsList);
            if(reviewsList != null && reviewsList.size() == 0) mNoReviews.setVisibility(View.VISIBLE);

            if(!(Boolean) properties.get("internetState")){
                mReviewsError.setVisibility(View.VISIBLE);
            }
            mLoadingReviewsIndicator.setVisibility(View.INVISIBLE);
        }
    }



    @Override
    public void onClick(String trailerYoutubeCode) {
        startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse(YOUTUBE_URL + trailerYoutubeCode)));
    }
}
