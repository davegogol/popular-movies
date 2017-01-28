package com.example.android.popularmovies.utils;

import android.net.Uri;
import android.util.Log;

import com.example.android.popularmovies.config.AppConfig;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static com.example.android.popularmovies.utils.NetworkUtils.getStringBodyResponseFromHttpUrl;

/**
 * MoviesAPIClient represents the "themoviedb" API client.
 * Helper class to forward the needed HTTP requests and fetch the data.
 */
public class MoviesAPIClient {
    private static final String TAG = NetworkUtils.class.getSimpleName();
    final static String KEY_PARAM = "api_key";
    private static final String THE_MOVIE_API_URL = "https://api.themoviedb.org/3/movie/";
    private static final String POPULAR_PATH = "popular";
    private static final String TOP_RATED_PATH = "top_rated";
    private static final String POPULAR = "POPULAR";
    private static final String TOP_RATED = "TOP_RATED";

    /**
     * Returns the most popular movies as JSON string.
     * @return JSON string
     */
    public String getPopularMovies(){
        String criteria = POPULAR;
        String popularMovies = getMoviesBy(criteria);
        return popularMovies;
    }

    /**
     * Returns the top rated movies as JSON string.
     * @return JSON string
     */
    public  String getTopRatedMovies(){
        String criteria = "TOP_RATED";
        String topRatedMovies = getMoviesBy(criteria);
        return topRatedMovies;
    }

    private String getMoviesBy(String popular) {
        String movies = "";
        URL moviesUrl = buildUrl(popular);
        try {
            movies = getStringBodyResponseFromHttpUrl(moviesUrl);
        } catch (IOException e) {
            Log.e(TAG, "IO Exception thrown!", e);
        }
        return movies;
    }

    private URL buildUrl(String criteria) {
        String subPath;

        switch (criteria){
            case POPULAR: subPath = POPULAR_PATH; break;
            case TOP_RATED: subPath = TOP_RATED_PATH; break;
            default: throw new IllegalArgumentException("Illegal criteria!");
        }

        Uri builtUri = Uri.parse(THE_MOVIE_API_URL).buildUpon()
                .appendPath(subPath)
                .appendQueryParameter(KEY_PARAM, AppConfig.KEY)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, "Built URI " + url, e);
        }

        Log.v(TAG, "Built URI " + url);
        return url;
    }
}
