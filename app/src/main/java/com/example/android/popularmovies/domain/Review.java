package com.example.android.popularmovies.domain;

/**
 * Movie Review Domain object, represents metadata for the movie review entity.
 */
public class Review {
    String id;
    String author;
    String content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
