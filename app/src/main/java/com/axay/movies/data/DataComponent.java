package com.axay.movies.data;

import com.axay.movies.AppModule;
import com.axay.movies.data.api.ApiModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author akshay
 * @since 7/2/16
 */

@Singleton
@Component(
        modules = {DataModule.class,
                ApiModule.class},
        dependencies = AppModule.class
)
public interface DataComponent {
}
