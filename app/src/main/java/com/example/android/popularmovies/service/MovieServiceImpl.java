package com.example.android.popularmovies.service;

import android.util.Log;

import com.example.android.popularmovies.domain.Movie;
import com.example.android.popularmovies.domain.Review;
import com.example.android.popularmovies.domain.Trailer;
import com.example.android.popularmovies.service.exception.MovieServiceException;
import com.example.android.popularmovies.utils.MoviesAPIClient;
import com.example.android.popularmovies.utils.MoviesJsonUtils;
import org.json.JSONObject;

import java.net.UnknownHostException;
import java.util.List;

/**
 * This class represents the service to retrieve movies data.
 */
public class MovieServiceImpl implements MovieService {

    private static final String TAG = MovieServiceImpl.class.getSimpleName();

    private MoviesAPIClient moviesAPIClient = new MoviesAPIClient();

    @Override
    public List<Movie> getPopularMovies() throws MovieServiceException {
        List<Movie> moviesList = null;
        try {
            JSONObject moviesJson = moviesAPIClient.getPopularMovies();
            moviesList = MoviesJsonUtils.getMovies(moviesJson);
            Log.d(TAG, "< movies json: " + moviesJson.toString());
        }catch (Exception e){
            handleException(e);
        }
        return moviesList;
    }

    @Override
    public List<Movie> getTopRatedMovies() throws MovieServiceException  {
        List<Movie> moviesList = null;
        try {
            JSONObject moviesJson = moviesAPIClient.getTopRatedMovies();
            moviesList = MoviesJsonUtils.getMovies(moviesJson);
            Log.d(TAG, "< movies json: " + moviesJson.toString());
        }catch (Exception e){
            handleException(e);
        }
        return moviesList;
    }

    @Override
    public List<Trailer> getTrailersByMovieId(String movieId) throws MovieServiceException {

        if(movieId == null || "".equals(movieId) )
            throw new IllegalArgumentException("movieId must be not null/empty!");

        List<Trailer> trailersList = null;
        try{
            JSONObject trailersListJson = moviesAPIClient.getTrailersByMovieId(movieId);
            trailersList = MoviesJsonUtils.getTrailers(trailersListJson);
        }catch (Exception e){
            handleException(e);
        }
        return trailersList;
    }

    @Override
    public List<Review> getReviewsByMovieId(String movieId) throws MovieServiceException {
        if(movieId == null || "".equals(movieId) )
            throw new IllegalArgumentException("movieId must be not null/empty!");
        List<Review> reviewsList = null;
        try{
            JSONObject reviewsListJson = moviesAPIClient.getReviewsByMovieId(movieId);
            reviewsList = MoviesJsonUtils.getReviews(reviewsListJson);
        }catch (Exception e){
            handleException(e);
        }
        return reviewsList;
    }

    @Override
    public Movie getMovieById(String movieId) throws MovieServiceException {
        if(movieId == null || "".equals(movieId) )
            throw new IllegalArgumentException("movieId must be not null/empty!");
        Movie movie = null;
        try{
            JSONObject movieJson = moviesAPIClient.getMovieById(movieId);
            movie = MoviesJsonUtils.getMovie(movieJson);
        }catch (Exception e){
            handleException(e);
        }
        return movie;
    }

    private void handleException(Exception e) throws MovieServiceException {
        Log.e(TAG, "Exception thrown!", e);
        String exceptionTag = "";
        if(e instanceof UnknownHostException){
            exceptionTag = "NO_INTERNET";
        }
        throw new MovieServiceException(exceptionTag);
    }
}
