/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
            parsedMovieData.add(movie);
        }
        return parsedMovieData;
    }
}