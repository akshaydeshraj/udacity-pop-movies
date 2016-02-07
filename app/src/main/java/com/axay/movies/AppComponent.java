package com.axay.movies;

import com.axay.movies.data.DataModule;
import com.axay.movies.ui.UiModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author akshay
 * @since 7/2/16
 */
@Singleton
@Component(
        modules = {
                AppModule.class,
                DataModule.class,
                UiModule.class
        }
)
public interface AppComponent {

    void inject(App app);
}
