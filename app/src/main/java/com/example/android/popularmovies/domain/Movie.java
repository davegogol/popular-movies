package com.example.android.popularmovies.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Movie Domain object, represents metadata for the movie entity.
 */
public class Movie implements Parcelable {
    private String id;
    private String posterPath;
    private String name;
    private String overview;
    private double voteAverage;
    private String releaseDate;

    /**
     * Constructor.
     */
    public Movie() {}

    /**
     * Constructor from parcel.
     * @param in parcel.
     */
    private Movie(Parcel in){
        posterPath = in.readString();
        name = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        voteAverage = in.readDouble();
    }

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
     * Getter for id
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * Setter for id
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Sets movie title
     * @param name Movie title
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(posterPath);
        parcel.writeString(name);
        parcel.writeString(overview);
        parcel.writeString(releaseDate);
        parcel.writeDouble(voteAverage);
    }

    /**
     * Builder class.
     */
    public final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }
        @Override
        public Movie[] newArray(int i) {
            return new Movie[i];
        }
    };

}
