package com.example.android.popularmovies.domain;

/**
 * Movie Domain object, represents metadata for the movie entity.
 */
public class Movie {
    private String posterPath;
    private String name;

    /**
     * Returns Poster Path data.
     * @return Poster path data.
     */
    public String getPosterPath() {
        return posterPath;
    }

    /**
     * Sets Poster path info
     * @param posterPath Poster path info
     */
    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    /**
     * Returns movie title
     * @return Movie title
     */
    public String getName() {
        return name;
    }

    /**
     * Sets movie title
     * @param name Movie title
     */
    public void setName(String name) {
        this.name = name;
    }
}
