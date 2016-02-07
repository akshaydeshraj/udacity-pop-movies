package com.axay.movies.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.axay.movies.R;
import com.axay.movies.commons.BaseActivity;
import com.axay.movies.data.api.TmdbApi;
import com.axay.movies.data.api.model.Movie;
import com.axay.movies.ui.adapter.MovieDetailsAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @author akshay
 * @since 27/12/15
 */
public class MovieDetailsActivity extends BaseActivity {

    public static final String MOVIE = "key_movie";

    @Inject
    Picasso picasso;

    @Inject
    TmdbApi tmdbApi;

    RecyclerView rvMovieDetails;

    Movie movie;

    List<Object> list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        movie = getIntent().getParcelableExtra(MOVIE);

        list.add(movie);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(movie.getOriginalTitle());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        rvMovieDetails = (RecyclerView) findViewById(R.id.rv_movie_details);
        rvMovieDetails.setLayoutManager(new LinearLayoutManager(this));

        loadAdditionalData();
    }

    @Override
    protected void setupComponent() {
        initialiseComponent().inject(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadAdditionalData() {

        Observable.zip(tmdbApi.getMovieReviews(movie.getId()), tmdbApi.getMovieTrailers(movie.getId()),
                (reviewsResponse, videoResponse) -> {
                    List<Object> reviewsAndTrailers = new ArrayList<Object>();
                    reviewsAndTrailers.addAll(videoResponse.getResults());
                    reviewsAndTrailers.addAll(reviewsResponse.getResults());
                    return reviewsAndTrailers;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Object>>() {
                    @Override
                    public void onCompleted() {
                        Timber.i("Requests Completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(List<Object> objects) {
                        list.addAll(objects);
                        MovieDetailsAdapter movieDetailsAdapter = new
                                MovieDetailsAdapter(list, MovieDetailsActivity.this, picasso);
                        rvMovieDetails.setAdapter(movieDetailsAdapter);
                    }
                });
    }
}