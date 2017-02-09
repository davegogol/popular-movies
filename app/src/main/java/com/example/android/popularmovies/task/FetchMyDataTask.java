package com.example.android.popularmovies.task;

import android.os.AsyncTask;
import android.util.Log;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.android.popularmovies.domain.Movie;
import com.example.android.popularmovies.service.MovieService;
import com.example.android.popularmovies.service.exception.MovieServiceException;


/**
 * This class represents an AsyncTask definition for retrieving movie data from a source.
 */
public class FetchMyDataTask extends AsyncTask<String, Void, List<Movie>> {
    private static final String TAG = FetchMyDataTask.class.getSimpleName();

    private MovieService movieService;
    private static final int TIME_IN_MILLIS = 500;
    private Map<String, Object> properties = new HashMap<>();
    {
        properties.put("internetState", true);
    }

    private String selectedSortingPreference;
    private AsyncTaskCompleteListener<List<Movie>, Map<String, Object>> listener;

    public FetchMyDataTask(MovieService movieService,
                           AsyncTaskCompleteListener<List<Movie>, Map<String, Object>> listener) {
        this.movieService = movieService;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        properties.put("internetState", true);
        listener.onPreTaskExecute();
    }

    @Override
    protected List<Movie> doInBackground(String... params) {
        for(String param: params){
            Log.v(TAG, "param-> " + param);
        }
        selectedSortingPreference = params[0];

        try {
            Thread.sleep(TIME_IN_MILLIS);
        } catch (InterruptedException e) {
            Log.e(TAG, "Exception thrown!", e);
        }
        List<Movie> moviesParsedData;
        try {
            Log.d(TAG, "> Preference selected: " + selectedSortingPreference);
            if(selectedSortingPreference.equals("") || "POPULARITY".equals(selectedSortingPreference))
                moviesParsedData = movieService.getPopularMovies();
            else
                moviesParsedData = movieService.getTopRatedMovies();
        } catch (MovieServiceException e) {
            Log.e(TAG, "Exception thrown!", e);
            if(e.getTag().equals("NO_INTERNET")) properties.put("internetState", false);
            return null;
        }
        return moviesParsedData;
    }
    @Override
    protected void onPostExecute(List<Movie> movieList) {
        super.onPostExecute(movieList);
        listener.onTaskComplete(movieList, properties);
    }

}
