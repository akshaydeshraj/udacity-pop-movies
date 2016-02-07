package com.axay.movies.data.api.model;

import java.util.List;

/**
 * @author akshay
 * @since 7/2/16
 */
public class ReviewsResponse {

    private String id;

    private String page;

    private List<Review> results;

    public class Review {
        String id;
        String author;
        String content;
        String url;

        public String getId() {
            return id;
        }

        public String getAuthor() {
            return author;
        }

        public String getContent() {
            return content;
        }

        public String getUrl() {
            return url;
        }
    }

    public String getId() {
        return id;
    }

    public String getPage() {
        return page;
    }

    public List<Review> getResults() {
        return results;
    }
}
