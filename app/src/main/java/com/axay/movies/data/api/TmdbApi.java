package com.axay.movies.data.api;

import com.axay.movies.BuildConfig;
import com.axay.movies.data.api.model.DiscoverMovieResponse;
import com.axay.movies.data.api.model.ReviewsResponse;
import com.axay.movies.data.api.model.VideoResponse;

import java.util.HashMap;

import retrofit2.http.GET;
import retrofit2.http.Path;
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

    @GET("movie/{id}/videos?api_key=" + BuildConfig.TMDB_API_KEY)
    Observable<VideoResponse> getMovieTrailers(
            @Path("id") String id
    );

    @GET("movie/{id}/reviews?api_key=" + BuildConfig.TMDB_API_KEY)
    Observable<ReviewsResponse> getMovieReviews(
            @Path("id") String id
    );
}
