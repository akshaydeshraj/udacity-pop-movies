package com.axay.movies.ui.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.axay.movies.R;
import com.axay.movies.data.api.model.Movie;
import com.axay.movies.data.api.model.ReviewsResponse;
import com.axay.movies.data.api.model.VideoResponse;
import com.axay.movies.data.provider.MovieContract;
import com.axay.movies.ui.adapter.viewholders.MovieViewHolder;
import com.axay.movies.ui.adapter.viewholders.ReviewViewHolder;
import com.axay.movies.ui.adapter.viewholders.TrailerViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * @author akshay
 * @since 7/2/16
 */
public class MovieDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> items;
    private Context context;
    private Picasso picasso;

    private final int MOVIE = 0, TRAILER = 1, REVIEW = 2;

    final String BASE_BACKDROP_URL = "http://image.tmdb.org/t/p/w500";
    final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w185";

    public MovieDetailsAdapter(List<Object> items, Context context, Picasso picasso) {
        this.items = items;
        this.context = context;
        this.picasso = picasso;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case MOVIE:
                View v1 = inflater.inflate(R.layout.item_movie_detail, parent, false);
                viewHolder = new MovieViewHolder(v1);
                break;
            case TRAILER:
                View v2 = inflater.inflate(R.layout.item_trailer, parent, false);
                viewHolder = new TrailerViewHolder(v2);
                break;
            case REVIEW:
                View v3 = inflater.inflate(R.layout.item_review, parent, false);
                viewHolder = new ReviewViewHolder(v3);
                break;
            default:
                viewHolder = null;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()) {

            case MOVIE:
                Movie movie = (Movie) items.get(position);
                MovieViewHolder movieViewHolder = (MovieViewHolder) holder;
                picasso.load(BASE_BACKDROP_URL +
                        movie.getBackdropPath()).fit().centerCrop().into(movieViewHolder.ivBackdrop);
                picasso.load(BASE_IMAGE_URL +
                        movie.getPosterPath()).fit().centerCrop().into(movieViewHolder.ivPoster);
                movieViewHolder.tvOverView.setText(movie.getOverview());
                movieViewHolder.tvReleaseDate.setText(movie.getReleaseDate());
                movieViewHolder.tvUserRating.setText(new
                        StringBuilder().append("Rating : ").append(movie.getVoteAverage()));
                movieViewHolder.btnFavourite.setOnClickListener(v -> insertData(movie));
                break;

            case TRAILER:
                VideoResponse.Trailer trailer = (VideoResponse.Trailer) items.get(position);
                TrailerViewHolder trailerViewHolder = (TrailerViewHolder) holder;
                trailerViewHolder.tvTrailerName.setText(trailer.getName());
                trailerViewHolder.tvTrailerName.setOnClickListener(v -> {
                    String url = "https://www.youtube.com/watch?v=" + trailer.getKey();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    context.startActivity(i);
                });
                break;

            case REVIEW:
                ReviewsResponse.Review review = (ReviewsResponse.Review) items.get(position);
                ReviewViewHolder reviewViewHolder = (ReviewViewHolder) holder;
                reviewViewHolder.tvReviewContent.setText(review.getContent());
                reviewViewHolder.tvReviewAuthor.setText(review.getAuthor());

                reviewViewHolder.llReview.setOnClickListener(v -> {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(review.getUrl()));
                    context.startActivity(i);
                });
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (items.get(position) instanceof Movie) {
            return MOVIE;
        }
        if (items.get(position) instanceof VideoResponse.Trailer) {
            return TRAILER;
        }
        if (items.get(position) instanceof ReviewsResponse.Review) {
            return REVIEW;
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private void insertData(Movie movie) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movie.getId());
        contentValues.put(MovieContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
        contentValues.put(MovieContract.MovieEntry.COLUMN_POSTER, movie.getPosterPath());
        contentValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
        contentValues.put(MovieContract.MovieEntry.COLUMN_BACKDROP, movie.getBackdropPath());
        contentValues.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
        contentValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        contentValues.put(MovieContract.MovieEntry.COLUMN_POPULARITY, movie.getPopularity());
        contentValues.put(MovieContract.MovieEntry.COLUMN_VOTE_COUNT, movie.getVoteCount());
        contentValues.put(MovieContract.MovieEntry.COLUMN_VIDEO, String.valueOf(movie.isVideo()));
        contentValues.put(MovieContract.MovieEntry.COLUMN_ADULT, String.valueOf(movie.isAdult()));

        context.getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);
    }
}
