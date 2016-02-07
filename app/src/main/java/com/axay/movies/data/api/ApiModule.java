package com.axay.movies.data.api;


import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

/**
 * @author akshay
 * @since 7/2/16
 */

@Module
public class ApiModule {

    public static final HttpUrl API_URL = HttpUrl.parse("");

    static OkHttpClient createApiClient(OkHttpClient client, HttpLoggingInterceptor
            loggingInterceptor) {
        client.interceptors().add(loggingInterceptor);
        return client;
    }

    @Provides
    @Singleton
    HttpUrl provideBaseUrl() {
        return API_URL;
    }

    @Provides
    @Singleton
    HttpLoggingInterceptor provideLoggingInterceptor() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(
                message -> Timber.tag("OkHttp").v(message));
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return loggingInterceptor;
    }

    @Provides
    @Singleton
    @Named("Api")
    OkHttpClient provideApiClient(OkHttpClient client, HttpLoggingInterceptor httpLoggingInterceptor) {
        return createApiClient(client, httpLoggingInterceptor);
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(HttpUrl baseUrl, @Named("Api") OkHttpClient client) {
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
