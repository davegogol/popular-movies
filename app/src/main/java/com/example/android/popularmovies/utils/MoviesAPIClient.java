package com.example.android.popularmovies.utils;

import android.net.Uri;
import android.util.Log;

import com.example.android.popularmovies.config.AppConfig;

import org.json.JSONException;
import org.json.JSONObject;

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
    private final static String KEY_PARAM = "api_key";

    private static final String THE_MOVIE_API_URL = "https://api.themoviedb.org/3/movie/";
    private static final String POPULAR_SUB_PATH = "popular";
    private static final String TOP_RATED_SUB_PATH = "top_rated";
    private static final String TRAILERS_SUB_PATH = "videos";
    private static final String REVIEWS_SUB_PATH = "reviews";

    private static final String POPULAR = "POPULAR";
    private static final String TOP_RATED = "TOP_RATED";

    /**
     * Returns the most popular movies as JSON string.
     * @return JSON string
     */
    public JSONObject getPopularMovies() throws JSONException, IOException {
        String criteria = POPULAR;
        String popularMovies = getMoviesBySortingCriteria(criteria);
        return new JSONObject(popularMovies);
    }

    /**
     * Returns the top rated movies as JSON string.
     * @return JSON string
     */
    public  JSONObject getTopRatedMovies() throws JSONException, IOException {
        String criteria = TOP_RATED;
        String topRatedMovies = getMoviesBySortingCriteria(criteria);
        return new JSONObject(topRatedMovies);
    }

    /**
     * Returns the movies trailers as JSON string.
     * @return JSON string
     */
    public JSONObject getTrailersByMovieId(String movieId) throws IOException, JSONException {

        Uri builtUri = Uri.parse(THE_MOVIE_API_URL).buildUpon()
                .appendPath(movieId)
                .appendPath(TRAILERS_SUB_PATH)
                .appendQueryParameter(KEY_PARAM, AppConfig.KEY)
                .build();

        URL url = buildUrl(builtUri);

        String trailersResponseBody = getStringBodyResponseFromHttpUrl(url);

        return new JSONObject(trailersResponseBody);
    }

    /**
     * Returns the movies reviews as JSON string.
     * @return JSON string
     */
    public JSONObject getReviewsByMovieId(String movieId) throws IOException, JSONException {
        Uri builtUri = Uri.parse(THE_MOVIE_API_URL).buildUpon()
                .appendPath(movieId)
                .appendPath(REVIEWS_SUB_PATH)
                .appendQueryParameter(KEY_PARAM, AppConfig.KEY)
                .build();

        URL url = buildUrl(builtUri);

        String reviewsResponseBody = getStringBodyResponseFromHttpUrl(url);

        return new JSONObject(reviewsResponseBody);
    }

    private String getMoviesBySortingCriteria(String sortingCriteria) throws IOException {
        URL moviesUrl = buildUrlBySortingCriteria(sortingCriteria);
        return getStringBodyResponseFromHttpUrl(moviesUrl);
    }

    private URL buildUrlBySortingCriteria(String sortingCriteria) throws MalformedURLException {
        String subPath;

        switch (sortingCriteria){
            case POPULAR: subPath = POPULAR_SUB_PATH; break;
            case TOP_RATED: subPath = TOP_RATED_SUB_PATH; break;
            default: throw new IllegalArgumentException("Illegal criteria!");
        }

        Uri builtUri = Uri.parse(THE_MOVIE_API_URL).buildUpon()
                .appendPath(subPath)
                .appendQueryParameter(KEY_PARAM, AppConfig.KEY)
                .build();

        return buildUrl(builtUri);
    }

    private URL buildUrl(Uri uri) throws MalformedURLException {
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, "Built URI " + url, e);
            throw e;
        }

        Log.v(TAG, "Built URI " + url);
        return url;
    }

}
