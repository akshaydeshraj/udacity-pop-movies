package com.axay.movies.data.api;


import com.axay.movies.BuildConfig;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author akshay
 * @since 7/2/16
 */

@Module
public class ApiModule {

    public static final HttpUrl API_URL = HttpUrl.parse(BuildConfig.API_ENDPOINT);


    @Provides
    @Singleton
    HttpUrl provideBaseUrl() {
        return API_URL;
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(HttpUrl baseUrl, OkHttpClient client) {
        return new Retrofit.Builder()
                .client(client)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    TmdbApi provideTmdbApi(Retrofit retrofit) {
        return retrofit.create(TmdbApi.class);
    }
}
