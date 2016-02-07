package com.axay.movies.commons;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.axay.movies.App;
import com.axay.movies.ui.UiComponent;

/**
 * @author akshay
 * @since 7/2/16
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupComponent();
    }

    protected abstract void setupComponent();

    protected UiComponent initialiseComponent() {
        return ((App) getApplication()).getComponent();
    }
}
