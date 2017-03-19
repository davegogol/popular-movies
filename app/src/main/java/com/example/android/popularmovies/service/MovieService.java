package com.example.android.popularmovies.service;

import com.example.android.popularmovies.domain.Movie;
import com.example.android.popularmovies.domain.Review;
import com.example.android.popularmovies.domain.Trailer;
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

    /**
     * Retrieves the related trailers for a specific movie, by its ID.
     * @return Trailers list
     * @throws MovieServiceException
     */
    public List<Trailer> getTrailersByMovieId(String movieId) throws MovieServiceException;

    /**
     * Retrieves the related trailers for a specific movie, by its ID.
     * @return Trailers list
     * @throws MovieServiceException
     */
    public List<Review> getReviewsByMovieId(String movieId) throws MovieServiceException;

    /**
     * Retrieves the movie details by its ID.
     * @return movie
     * @throws MovieServiceException
     */
    public Movie getMovieById(String movieId) throws MovieServiceException;
}
