package com.axay.movies.ui;

import com.axay.movies.AppModule;
import com.axay.movies.data.DataModule;
import com.axay.movies.data.api.ApiModule;
import com.axay.movies.ui.activity.MovieDetailsActivity;
import com.axay.movies.ui.fragment.MoviesFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author akshay
 * @since 7/2/16
 */

@Singleton
@Component(
        modules = {UiModule.class,
                DataModule.class,
                ApiModule.class},
        dependencies = AppModule.class
)
public interface UiComponent {

    void inject(MovieDetailsActivity activity);

    void inject(MoviesFragment moviesFragment);

}
