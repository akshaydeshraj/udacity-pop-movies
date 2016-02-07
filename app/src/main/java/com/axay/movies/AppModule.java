package com.axay.movies;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author akshay
 * @since 7/2/16
 */

@Module
public class AppModule {

    private App app;

    public AppModule(App app) {
        this.app = app;
    }

    @Provides
    @Singleton
    public App provideApplication() {
        return app;
    }
}
