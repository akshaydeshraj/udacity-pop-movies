package com.axay.movies;

import android.app.Application;

import com.axay.movies.ui.DaggerUiComponent;
import com.axay.movies.ui.UiComponent;
import com.axay.movies.ui.UiModule;
import com.squareup.leakcanary.LeakCanary;

import timber.log.Timber;

/**
 * @author akshay
 * @since 7/2/16
 */
public class App extends Application {

    UiComponent mComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        LeakCanary.install(this);

        AppModule appModule = new AppModule(this);
        UiModule uiModule = new UiModule();

        DaggerAppComponent.builder()
                .appModule(appModule)
                .build().inject(this);

        mComponent = DaggerUiComponent.builder()
                .appModule(appModule)
                .uiModule(uiModule)
                .build();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    public UiComponent getComponent() {
        return mComponent;
    }
}
