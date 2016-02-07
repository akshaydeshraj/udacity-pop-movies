package com.axay.movies.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.axay.movies.R;
import com.axay.movies.data.api.model.Movie;
import com.axay.movies.ui.fragment.MovieDetailsFragment;
import com.axay.movies.ui.fragment.MoviesFragment;

public class MainActivity extends AppCompatActivity implements MoviesFragment.OnMovieSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.fragment_container) != null) {

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            if (savedInstanceState != null) {
                return;
            }

            MoviesFragment moviesFragment = new MoviesFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, moviesFragment).commit();
        }
    }

    @Override
    public void onItemSelected(Movie movie) {

        MovieDetailsFragment movieDetailsFragment = (MovieDetailsFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_details);

        if (movieDetailsFragment != null) {
            movieDetailsFragment.updateMovie(movie);
        } else {
            MovieDetailsFragment newFragment = new MovieDetailsFragment();
            Bundle args = new Bundle();
            args.putParcelable(MovieDetailsFragment.MOVIE, movie);
            newFragment.setArguments(args);

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            fragmentTransaction.replace(R.id.fragment_container, newFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }
}