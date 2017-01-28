package com.example.android.popularmovies.utils;

import com.example.android.popularmovies.domain.Movie;
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
     * @param jsonString JSON Movies information
     * @return List of movies
     * @throws JSONException
     */
    public static List<Movie> getMoviesDataFromJson(String jsonString)
            throws JSONException {
        List<Movie> parsedMovieData = new ArrayList<>();

        JSONObject forecastJson = new JSONObject(jsonString);
        JSONArray moviesResultsArray = forecastJson.getJSONArray("results");

        for(int i = 0; i < moviesResultsArray.length(); i++){
            JSONObject jsonObject = (JSONObject) moviesResultsArray.get(i);
            Movie movie = new Movie();
            movie.setName(jsonObject.getString("title"));
            movie.setPosterPath(jsonObject.getString("poster_path"));
            movie.setReleaseDate(jsonObject.getString("release_date"));
            movie.setOverview(jsonObject.getString("overview"));
            movie.setVoteAverage(jsonObject.getDouble("vote_average"));
            parsedMovieData.add(movie);
        }
        return parsedMovieData;
    }
}