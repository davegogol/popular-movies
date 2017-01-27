package com.example.android.popularmovies;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.example.android.popularmovies.domain.Movie;
import com.example.android.popularmovies.utils.MoviesAPIClient;
import com.example.android.popularmovies.utils.MoviesJsonUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int SPAN_COUNT = 3;
    private RecyclerView moviesGridRecyclerView;
    private MoviesAdapter moviesAdapter;
    private List<Movie> movieList = new ArrayList<>();
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

        moviesAdapter = new MoviesAdapter(movieList);

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


    public class FetchMoviesDataTask extends AsyncTask<String, Void, List<Movie>> {
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
