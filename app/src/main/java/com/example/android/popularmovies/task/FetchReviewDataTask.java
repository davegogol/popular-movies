package com.example.android.popularmovies.task;

import android.os.AsyncTask;
import android.util.Log;

import com.example.android.popularmovies.domain.Review;
import com.example.android.popularmovies.domain.Trailer;
import com.example.android.popularmovies.service.MovieService;
import com.example.android.popularmovies.service.exception.MovieServiceException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * This class represents an AsyncTask definition for retrieving movie reviews data from a source.
 */
public class FetchReviewDataTask extends AsyncTask<String, Void, List<Review>> {
    private static final String TAG = FetchReviewDataTask.class.getSimpleName();
    private MovieService movieService;
    private static final int TIME_IN_MILLIS = 500;
    private Map<String, Object> properties = new HashMap<>();
    {
        properties.put("internetState", true);
    }

    private AsyncTaskCompleteListener<List<Review>, Map<String, Object>> listener;

    public FetchReviewDataTask(MovieService movieService,
                               AsyncTaskCompleteListener<List<Review>, Map<String, Object>> listener) {
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
    protected List<Review> doInBackground(String... params) {
        for(String param: params){
            Log.v(TAG, "param-> " + param);
        }
        String movieId = params[0];
        try {
            Thread.sleep(TIME_IN_MILLIS);
        } catch (InterruptedException e) {
            Log.e(TAG, "Exception thrown!", e);
        }
        List<Review> moviesReviews;
        try {
            moviesReviews = movieService.getReviewsByMovieId(movieId);
        } catch (MovieServiceException e) {
            Log.e(TAG, "Exception thrown!", e);
            if(e.getTag().equals("NO_INTERNET")) properties.put("internetState", false);
            return null;
        }
        return moviesReviews;
    }

    @Override
    protected void onPostExecute(List<Review> movieReviewsList) {
        super.onPostExecute(movieReviewsList);
        listener.onTaskComplete(movieReviewsList, properties);
    }
}
