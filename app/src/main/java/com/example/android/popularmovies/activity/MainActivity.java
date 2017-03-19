package com.example.android.popularmovies.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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
import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.domain.Movie;
import com.example.android.popularmovies.service.MovieServiceImpl;
import com.example.android.popularmovies.task.AsyncTaskCompleteListener;
import com.example.android.popularmovies.task.FetchMoviesDataTask;

/**
 * Main Activity displayed at start-up time.
 */
public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>{

    private static final int ID_MOVIE_LOADER = 44;

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
                 intent.putExtra("movie.id", moviesList.get(position).getId());
                 intent.putExtra("movie.title", moviesList.get(position).getName());
                 intent.putExtra("movie.poster", moviesList.get(position).getPosterPath());

                 if(moviesList.get(position).getReleaseDate() != null)
                     intent.putExtra("movie.release_date", moviesList.get(position).getReleaseDate());
                 if(moviesList.get(position).getOverview() != null)
                     intent.putExtra("movie.overview", moviesList.get(position).getOverview());
                 if(moviesList.get(position).getVoteAverage() != 0)
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

        if(shownSortingPreference.equals( getResources().getStringArray(R.array.listvalues)[2])){
            mLoadingIndicator.setVisibility(View.VISIBLE);
            gridView.setVisibility(View.INVISIBLE);
            movieAdapter.clear();
            //TODO: better on separated thread
            getSupportLoaderManager().restartLoader(ID_MOVIE_LOADER, null, this);
        } else
            new FetchMoviesDataTask(movieService, new FetchMyDataTaskCompleteListener()).
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


    /**
     * Called by the {@link android.support.v4.app.LoaderManagerImpl} when a new Loader needs to be
     * created. This Activity only uses one loader, so we don't necessarily NEED to check the
     * loaderId, but this is certainly best practice.
     *
     * @param loaderId The loader ID for which we need to create a loader
     * @param bundle   Any arguments supplied by the caller
     * @return A new Loader instance that is ready to start loading.
     */
    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {
        switch (loaderId) {
            case ID_MOVIE_LOADER:
                Uri movieQueryUri = MovieContract.FavouriteEntry.CONTENT_URI;
                return new CursorLoader(this,
                        movieQueryUri,
                        null,
                        null,
                        null,
                        null);
            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    /**
     * Called when a Loader has finished loading its data.
     *
     * NOTE: There is one small bug in this code. If no data is present in the cursor do to an
     * initial load being performed with no access to internet, the loading indicator will show
     * indefinitely, until data is present from the ContentProvider. This will be fixed in a
     * future version of the course.
     *
     * @param loader The Loader that has finished.
     * @param data   The data generated by the Loader.
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        gridView.setVisibility(View.VISIBLE);

        List<Movie> movies = new ArrayList<>();
        if(data != null){
            for(int i = 0; i < data.getCount(); i ++){
                Movie movie = new Movie();
                data.moveToPosition(i);
                movie.setName(data.getString(data.getColumnIndex(MovieContract.FavouriteEntry.COLUMN_MOVIE_NAME)));
                movie.setPosterPath(data.getString(data.getColumnIndex(MovieContract.FavouriteEntry.COLUMN_MOVIE_POSTER)));
                movie.setId(data.getString(data.getColumnIndex(MovieContract.FavouriteEntry.COLUMN_MOVIE_ID)));
                movies.add(movie);
            }
            moviesList = movies;
            movieAdapter.addAll(moviesList);
        }else
            movieAdapter.clear();
    }

    /**
     * Called when a previously created loader is being reset, and thus making its data unavailable.
     * The application should at this point remove any references it has to the Loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        movieAdapter.clear();
    }
}
