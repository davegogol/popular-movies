package com.example.android.popularmovies.service.exception;

/**
 * This class represents an exception caused by the MovieService.
 */
public class MovieServiceException extends Exception {
    private String tag;

    /**
     * Constructor
     * @param exceptionTag exception cause.
     */
    public MovieServiceException(String exceptionTag) {
        this.tag = exceptionTag;
    }

    /**
     * Getter for tag.
     * @return tag
     */
    public String getTag() {
        return tag;
    }

    /**
     * Setter for tag.
     * @param tag Exception cause.
     */
    public void setTag(String tag) {
        this.tag = tag;
    }
}
