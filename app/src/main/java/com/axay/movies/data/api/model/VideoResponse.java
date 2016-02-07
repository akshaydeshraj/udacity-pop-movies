package com.axay.movies.data.api.model;

import java.util.List;

/**
 * @author akshay
 * @since 7/2/16
 */
public class VideoResponse {

    private String id;

    private List<Trailer> results;

    public class Trailer{
        private String id;
        private String key;
        private String name;
        private String size;
        private String site;
        private String type;

        public String getId() {
            return id;
        }

        public String getKey() {
            return key;
        }

        public String getName() {
            return name;
        }

        public String getSize() {
            return size;
        }

        public String getSite() {
            return site;
        }

        public String getType() {
            return type;
        }
    }

    public String getId() {
        return id;
    }

    public List<Trailer> getResults() {
        return results;
    }
}
