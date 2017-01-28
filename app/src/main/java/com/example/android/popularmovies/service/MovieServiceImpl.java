package com.example.android.popularmovies.service;

import android.util.Log;

import com.example.android.popularmovies.domain.Movie;
import com.example.android.popularmovies.service.exception.MovieServiceException;
import com.example.android.popularmovies.utils.MoviesAPIClient;
import com.example.android.popularmovies.utils.MoviesJsonUtils;
import org.json.JSONObject;

import java.util.List;

/**
 * This class represents the service to retrieve movies data.
 */
public class MovieServiceImpl implements MovieService {
    private static final String TAG = MovieServiceImpl.class.getSimpleName();
    private MoviesAPIClient moviesAPIClient = new MoviesAPIClient();
    public List<Movie> getPopularMovies() throws MovieServiceException {
        List<Movie> moviesList = null;
        try {
            JSONObject moviesJson = moviesAPIClient.getPopularMovies();
            moviesList = MoviesJsonUtils.getMoviesDataFromJson(moviesJson);
        }catch (Exception e){
            Log.e(TAG, "Exception thrown!", e);
            throw new MovieServiceException();
        }
        return moviesList;
    }
    public List<Movie> getTopRatedMovies() throws MovieServiceException  {
        List<Movie> moviesList = null;
        try {
            JSONObject moviesJson = moviesAPIClient.getTopRatedMovies();
            moviesList = MoviesJsonUtils.getMoviesDataFromJson(moviesJson);
        }catch (Exception e){
            Log.e(TAG, "Exception thrown!", e);
            throw new MovieServiceException();
        }
        return moviesList;
    }
}
