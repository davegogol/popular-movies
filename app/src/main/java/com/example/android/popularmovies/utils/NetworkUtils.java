package com.example.android.popularmovies.utils;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * NetworkUtils represents a helper class for forwarding HTTP requests and fetching
 * data.
 */
class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();
    /**
     * Returns String HTTP Response Body.
     * @param url URLto forward the GET HTTP Request
     * @return String HTTP Response Body
     * @throws IOException
     */
    public static String getStringBodyResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();

            if (hasInput) {
                String response = scanner.next();
                Log.v(TAG, "< HTTP Response: " + response);
                return response;
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
