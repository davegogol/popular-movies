package com.example.android.popularmovies.activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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

    private static final int SPAN_COUNT = 3;
    private RecyclerView moviesGridRecyclerView;
    private MoviesAdapter moviesAdapter;
    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        showMoviesDataView();
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

    /**
     * Custom task class for fetching movies data from a source.
     */
    private class FetchMoviesDataTask extends AsyncTask<String, Void, List<Movie>> {
        @Override
        protected void onPreExecute() {
            mLoadingIndicator.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }
        @Override
        protected List<Movie> doInBackground(String... params) {
            List<Movie> moviesParsedData = null;
            try {
                MoviesAPIClient moviesAPIClient = new MoviesAPIClient();
                String jsonMoviesResponse = moviesAPIClient.getPopularMovies();
                moviesParsedData = MoviesJsonUtils
                        .getMoviesDataFromJson(jsonMoviesResponse);
            } catch (Exception e) {
                e.printStackTrace();
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
