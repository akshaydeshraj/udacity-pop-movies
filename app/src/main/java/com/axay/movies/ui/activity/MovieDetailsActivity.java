package com.axay.movies.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.axay.movies.R;
import com.axay.movies.commons.BaseActivity;
import com.axay.movies.data.api.model.Movie;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

/**
 * @author akshay
 * @since 27/12/15
 */
public class MovieDetailsActivity extends BaseActivity {

    public static final String MOVIE = "key_movie";

    @Inject
    Picasso picasso;

    ImageView ivBackdrop, ivPoster;

    final String BASE_BACKDROP_URL = "http://image.tmdb.org/t/p/w500";
    final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w185";

    TextView tvOverView, tvReleaseDate, tvUserRating;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Movie movie = getIntent().getParcelableExtra(MOVIE);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(movie.getOriginalTitle());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ivBackdrop = (ImageView) findViewById(R.id.iv_backdrop);
        ivPoster = (ImageView) findViewById(R.id.iv_poster);
        tvOverView = (TextView) findViewById(R.id.tv_overview);
        tvReleaseDate = (TextView) findViewById(R.id.tv_release_date);
        tvUserRating = (TextView) findViewById(R.id.tv_user_rating);

        picasso.load(BASE_BACKDROP_URL + movie.getBackdropPath()).fit().centerCrop().into(ivBackdrop);
        picasso.load(BASE_IMAGE_URL + movie.getPosterPath()).fit().centerCrop().into(ivPoster);

        tvOverView.setText(movie.getOverview());
        tvReleaseDate.setText(movie.getReleaseDate());
        tvUserRating.setText(new StringBuilder().append("Rating : ").append(movie.getVoteAverage()));
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
}