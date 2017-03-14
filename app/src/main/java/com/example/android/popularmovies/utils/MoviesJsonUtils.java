package com.example.android.popularmovies.utils;

import com.example.android.popularmovies.domain.Movie;
import com.example.android.popularmovies.domain.Review;
import com.example.android.popularmovies.domain.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper class to extract movies information from JSON String representation.
 */
public final class MoviesJsonUtils {
    /**
     * Returns movies information from JSON String representation.
     * @param object JSON Movies information
     * @return List of movies
     * @throws JSONException
     */
    public static List<Movie> getMovies(JSONObject object)
            throws JSONException {
        List<Movie> parsedMovieData = new ArrayList<>();
        JSONArray moviesResultsArray = object.getJSONArray("results");
        for(int i = 0; i < moviesResultsArray.length(); i++){
            JSONObject jsonObject = (JSONObject) moviesResultsArray.get(i);
            Movie movie = new Movie();
            movie.setId(jsonObject.getString("id"));
            movie.setName(jsonObject.getString("title"));
            movie.setPosterPath(jsonObject.getString("poster_path"));
            movie.setReleaseDate(jsonObject.getString("release_date"));
            movie.setOverview(jsonObject.getString("overview"));
            movie.setVoteAverage(jsonObject.getDouble("vote_average"));
            parsedMovieData.add(movie);
        }
        return parsedMovieData;
    }
    /**
     * Returns movie trailer information from JSON String representation.
     * @param object JSON Movie Trailers information
     * @return List of trailers
     * @throws JSONException
     */
    public static List<Trailer> getTrailers(JSONObject object) throws JSONException {
        List<Trailer> parsedTrailers = new ArrayList<>();
        JSONArray moviesResultsArray = object.getJSONArray("results");
        for(int i = 0; i < moviesResultsArray.length(); i++){
            JSONObject jsonObject = (JSONObject) moviesResultsArray.get(i);
            Trailer trailer = new Trailer();
            trailer.setId(jsonObject.getString("id"));
            trailer.setName(jsonObject.getString("name"));
            parsedTrailers.add(trailer);
        }
        return parsedTrailers;
    }

    /**
     * Returns movie review information from JSON String representation.
     * @param object JSON Movie Reviews information
     * @return List of trailers
     * @throws JSONException
     */
    public static List<Review> getReviews(JSONObject object) throws JSONException {
        List<Review> parsedReviews = new ArrayList<>();
        JSONArray moviesResultsArray = object.getJSONArray("results");
        for(int i = 0; i < moviesResultsArray.length(); i++){
            JSONObject jsonObject = (JSONObject) moviesResultsArray.get(i);
            Review review = new Review();
            review.setId(jsonObject.getString("id"));
            review.setAuthor(jsonObject.getString("author"));
            review.setContent(jsonObject.getString("content"));
            parsedReviews.add(review);
        }
        return parsedReviews;
    }
}