package com.example.android.popularmovies.domain;

/**
 * Movie Domain object, represents metadata for the movie entity.
 */
public class Movie {
    private String posterPath;
    private String name;
    private String overview;
    private double voteAverage;
    private String releaseDate;

    /**
     * Returns movie overview.
     * @return Movie overview
     */
    public String getOverview() {
        return overview;
    }

    /**
     * Setter for movie overview
     * @param overview Movie overview
     */
    public void setOverview(String overview) {
        this.overview = overview;
    }

    /**
     * Returns movie vote average rate
     * @return Movie average rate
     */
    public double getVoteAverage() {
        return voteAverage;
    }

    /**
     * Setter for movie vote average rate
     * @param voteAverage Movie average rate
     */
    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    /**
     * Returns movie release date.
     * @return Movie release date.
     */
    public String getReleaseDate() {
        return releaseDate;
    }

    /**
     * Setter for movie release date.
     * @param releaseDate Movie release date.
     */
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

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
