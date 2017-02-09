package com.example.android.popularmovies.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.adapter.MovieAdapter;
import com.example.android.popularmovies.domain.Movie;
import com.example.android.popularmovies.service.MovieServiceImpl;
import com.example.android.popularmovies.task.AsyncTaskCompleteListener;
import com.example.android.popularmovies.task.FetchMyDataTask;


/**
 * Main Activity displayed at start-up time.
 */
public class MainActivity extends AppCompatActivity{
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String PREF_SORTING_KEY = "PREF_SORTING";
    private ProgressBar mLoadingIndicator;
    private SharedPreferences sharedPreferences;
    private MovieServiceImpl movieService = new MovieServiceImpl();
    private List<Movie> moviesList;
    private MovieAdapter movieAdapter;
    private GridView gridView;
    private ImageView errorImageView;
    private String shownSortingPreference = "";

    /**
     * Initial setup at the activity creation time.
     * @param savedInstanceState Bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        gridView = (GridView) findViewById(R.id.movies_grid);
        errorImageView = (ImageView) findViewById(R.id.error);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        movieAdapter = new MovieAdapter(this);
        gridView.setAdapter(movieAdapter);

        if(savedInstanceState == null || !savedInstanceState.containsKey("movies")) {
            Log.d(TAG, "Retrieve data from source");
            loadMoviesData();
        }else{
            Log.d(TAG, "Retrieve data from Bundle state");
            moviesList = savedInstanceState.getParcelableArrayList("movies");
            if(moviesList != null)
                movieAdapter.addAll(moviesList);
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
                 intent.putExtra("movie.title", moviesList.get(position).getName());
                 intent.putExtra("movie.poster", moviesList.get(position).getPosterPath());
                 intent.putExtra("movie.release_date", moviesList.get(position).getReleaseDate());
                 intent.putExtra("movie.overview", moviesList.get(position).getOverview());
                 intent.putExtra("movie.average_rate", String.valueOf(moviesList.get(position).getVoteAverage()));
                 startActivity(intent);
             }
        });
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


    @Override
    public void onResume() {
        super.onResume();
        String preferenceSorting = sharedPreferences.getString(PREF_SORTING_KEY, "");
        if(!preferenceSorting.equals(shownSortingPreference) && moviesList != null){
            Log.d(TAG, "Sorting preference was changed.");
            loadMoviesData();
        }
    }

    private void loadMoviesData() {
        String selectedSortingPreference = sharedPreferences.getString(PREF_SORTING_KEY, "");
        shownSortingPreference = selectedSortingPreference;
        new FetchMyDataTask(movieService, new FetchMyDataTaskCompleteListener()).
                execute(selectedSortingPreference);
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


    public class FetchMyDataTaskCompleteListener implements AsyncTaskCompleteListener<List<Movie>,
            Map<String,Object>> {
        @Override
        public void onPreTaskExecute() {
            gridView.setVisibility(View.INVISIBLE);
            errorImageView.setVisibility(View.INVISIBLE);

            mLoadingIndicator.setVisibility(View.VISIBLE);
            movieAdapter.clear();
        }

        @Override
        public void onTaskComplete(List<Movie> listMovieResult, Map<String,Object> properties ) {
            if(listMovieResult != null) {
                gridView.setVisibility(View.VISIBLE);
                moviesList = listMovieResult;
                movieAdapter.addAll(moviesList);
            }
            if(!(Boolean) properties.get("internetState")){
                errorImageView.setVisibility(View.VISIBLE);
            }
            mLoadingIndicator.setVisibility(View.INVISIBLE);
        }
    }
}
