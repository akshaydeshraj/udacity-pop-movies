package com.axay.movies.data.api.model;

import java.util.List;

/**
 * @author akshay
 * @since 7/2/16
 */
public class DiscoverMovieResponse {

    private String page;

    private List<Movie> results;

    public String getPage() {
        return page;
    }

    public List<Movie> getResults() {
        return results;
    }
}
