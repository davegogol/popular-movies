package com.example.android.popularmovies.activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.adapter.MoviesAdapter;
import com.example.android.popularmovies.domain.Movie;
import com.example.android.popularmovies.service.MovieServiceImpl;
import com.example.android.popularmovies.service.exception.MovieServiceException;
import java.util.ArrayList;
import java.util.List;

/**
 * Main Activity displayed at start-up time.
 */
public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviesAdapterOnClickHandler {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int SPAN_COUNT = 3;
    private static final String PREF_SORTING_KEY = "PREF_SORTING";
    private RecyclerView moviesGridRecyclerView;
    private MoviesAdapter moviesAdapter;
    private ProgressBar mLoadingIndicator;
    private SharedPreferences sharedPreferences;
    private MovieServiceImpl movieService = new MovieServiceImpl();
    private List<Movie> moviesList;

    /**
     * Initial setup at the activity creation time.
     * @param savedInstanceState Bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        moviesGridRecyclerView = (RecyclerView) findViewById(R.id.rv_movies);
        GridLayoutManager gridLayoutManager =
                new GridLayoutManager(this, SPAN_COUNT);
        moviesGridRecyclerView.setLayoutManager(gridLayoutManager);
        moviesGridRecyclerView.setHasFixedSize(true);
        moviesAdapter = new MoviesAdapter(this);
        moviesGridRecyclerView.setAdapter(moviesAdapter);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        if(savedInstanceState == null || !savedInstanceState.containsKey("movies")) {
            Log.d(TAG, "Retrieve data from source");
            loadMoviesData();
        }else{
            Log.d(TAG, "Retrieve data from Bundle state");
            moviesList = savedInstanceState.getParcelableArrayList("movies");
            moviesAdapter.setMoviesData(moviesList);
        }
    }

    /**
     * Saves additional data when the bundle state is stored, between one activity and
     * another.
     * @param outState bundle
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "Bundle state stored");
        outState.putParcelableArrayList("movies", (ArrayList<Movie>) moviesList);
        super.onSaveInstanceState(outState);
    }

    private void loadMoviesData() {
        new FetchMoviesDataTask().execute();
    }

    /**
     * Handles recycler view single item clicks.
     * @param movie Movie
     */
    @Override
    public void onClick(Movie movie) {
        Context context = this;
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra("movie.title", movie.getName());
        intent.putExtra("movie.poster", movie.getPosterPath());
        intent.putExtra("movie.release_date", movie.getReleaseDate());
        intent.putExtra("movie.overview", movie.getOverview());
        intent.putExtra("movie.average_rate", String.valueOf(movie.getVoteAverage()));
        startActivity(intent);
    }

    /**
     * Inflates the menu items for the activity menu.
     * @param menu Activity menu.
     * @return operation status
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_refresh, menu);
        return true;
    }

    /**
     * Handles menu items clicks.
     * @param item Clicked item.
     * @return operation status
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId){
            case  R.id.action_settings : {
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true; }
            case R.id.action_refresh: loadMoviesData();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Custom task class for fetching movies data from a source.
     */
    private class FetchMoviesDataTask extends AsyncTask<String, Void, List<Movie>> {
        @Override
        protected void onPreExecute() {
            moviesGridRecyclerView.setVisibility(View.INVISIBLE);
            mLoadingIndicator.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }
        @Override
        protected List<Movie> doInBackground(String... params) {
            List<Movie> moviesParsedData;
            try {
                String preferenceSorting = sharedPreferences.getString(PREF_SORTING_KEY, "");
                Log.d(TAG, "Preference selected: " + preferenceSorting);
                if(preferenceSorting.equals("") || "POPULARITY".equals(preferenceSorting))
                    moviesParsedData = movieService.getPopularMovies();
                else
                    moviesParsedData = movieService.getTopRatedMovies();
            } catch (MovieServiceException e) {
                Log.e(TAG, "Exception thrown!", e);
                return null;
            }
            return moviesParsedData;
        }
        @Override
        protected void onPostExecute(List<Movie> moviesData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            moviesGridRecyclerView.setVisibility(View.VISIBLE);
            moviesList = moviesData;
            moviesAdapter.setMoviesData(moviesList);
        }
    }
}
