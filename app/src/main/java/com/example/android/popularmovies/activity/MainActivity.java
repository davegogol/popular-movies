package com.example.android.popularmovies.activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceFragment;
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
import com.example.android.popularmovies.utils.MoviesAPIClient;
import com.example.android.popularmovies.utils.MoviesJsonUtils;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        moviesGridRecyclerView = (RecyclerView) findViewById(R.id.rv_movies);
        GridLayoutManager gridLayoutManager =
                new GridLayoutManager(this, SPAN_COUNT);
        moviesGridRecyclerView.setLayoutManager(gridLayoutManager);

        moviesGridRecyclerView.setHasFixedSize(true);

        moviesAdapter = new MoviesAdapter(this);

        moviesGridRecyclerView.setAdapter(moviesAdapter);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        loadMoviesData();
    }

    private void loadMoviesData() {
        new FetchMoviesDataTask().execute();
    }

    private void showMoviesDataView() {
        moviesGridRecyclerView.setVisibility(View.VISIBLE);
    }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Context context = this;
            Intent intent = new Intent(context, SettingsActivity.class);
            startActivity(intent);
            return true;
        }else if(id == R.id.action_refresh){
            loadMoviesData();
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
                MoviesAPIClient moviesAPIClient = new MoviesAPIClient();
                String preferenceSorting = sharedPreferences.getString(PREF_SORTING_KEY, "");
                Log.d(TAG, "Preference selected: " + preferenceSorting);
                String jsonMoviesResponse;

                if(preferenceSorting.equals("") || "POPULARITY".equals(preferenceSorting)){
                    jsonMoviesResponse = moviesAPIClient.getPopularMovies();
                } else {
                    jsonMoviesResponse = moviesAPIClient.getTopRatedMovies();
                }
                moviesParsedData = MoviesJsonUtils
                        .getMoviesDataFromJson(jsonMoviesResponse);
            } catch (Exception e) {
                Log.e(TAG, "Exception thrown!", e);
                return null;
            }
            return moviesParsedData;
        }
        @Override
        protected void onPostExecute(List<Movie> moviesData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            showMoviesDataView();
            moviesAdapter.setMoviesData(moviesData);
        }
    }
}
