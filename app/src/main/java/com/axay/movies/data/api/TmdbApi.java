package com.axay.movies.data.api;

import com.axay.movies.data.api.model.DiscoverMovieResponse;

import java.util.HashMap;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author akshay
 * @since 7/2/16
 */
public interface TmdbApi {

    @GET("discover/movie?")
    Observable<DiscoverMovieResponse> discoverMovies(
            @QueryMap HashMap<String, String> queryMap
    );
}
