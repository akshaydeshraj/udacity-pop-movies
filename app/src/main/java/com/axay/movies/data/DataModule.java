package com.axay.movies.data;

import android.app.Application;

import com.axay.movies.App;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.io.File;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import timber.log.Timber;

import static com.jakewharton.byteunits.DecimalByteUnit.MEGABYTES;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * @author akshay
 * @since 7/2/16
 */

@Module
public class DataModule {

    static final int DISK_CACHE_SIZE = (int) MEGABYTES.toBytes(50);

    static OkHttpClient.Builder createOkHttpClient(Application app) {
        // Install an HTTP cache in the application cache directory.
        File cacheDir = new File(app.getCacheDir(), "http");
        Cache cache = new Cache(cacheDir, DISK_CACHE_SIZE);

        return new OkHttpClient.Builder()
                .cache(cache)
                .connectTimeout(10, SECONDS)
                .readTimeout(10, SECONDS)
                .writeTimeout(10, SECONDS);
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(App app) {
        return createOkHttpClient(app).build();
    }

    @Provides
    @Singleton
    Picasso providePicasso(Application app, OkHttpClient client) {
        return new Picasso.Builder(app)
                .downloader(new OkHttp3Downloader(client))
                .listener((picasso, uri, e) -> Timber.e(e, "Failed to load image: %s", uri))
                .build();
    }
}