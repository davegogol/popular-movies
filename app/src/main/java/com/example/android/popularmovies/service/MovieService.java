package com.example.android.popularmovies.service;

import com.example.android.popularmovies.domain.Movie;
import com.example.android.popularmovies.service.exception.MovieServiceException;

import java.util.List;

/**
 * MovieService states the movies operations.
 */
public interface MovieService {
    /**
     * Retrieves the most popular movies.
     * @return Popular movies list
     * @throws MovieServiceException
     */
    public List<Movie> getPopularMovies() throws MovieServiceException;

    /**
     * Retrieves the top rated movies.
     * @return Top rated movies list
     * @throws MovieServiceException
     */
    public List<Movie> getTopRatedMovies() throws MovieServiceException;


}
