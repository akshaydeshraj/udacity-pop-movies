package com.axay.movies.ui.data;

/**
 * @author akshay
 * @since 27/12/15
 */
public class Movie {

    private String title;
    private String posterPath;
    private String overview;
    private String backdropPath;
    private String voteAverage;

    public Movie(String title, String posterPath, String overview, String backdropPath, String voteAverage) {
        this.title = title;
        this.posterPath = posterPath;
        this.overview = overview;
        this.backdropPath = backdropPath;
        this.voteAverage = voteAverage;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public String getVoteAverage() {
        return voteAverage;
    }
}