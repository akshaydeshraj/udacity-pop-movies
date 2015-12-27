package com.axay.movies.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.axay.movies.R;
import com.axay.movies.data.Movie;
import com.axay.movies.ui.fragment.MoviesFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * @author akshay
 * @since 26/12/15
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

    private ArrayList<Movie> moviesArrayList;
    private Context mContext;
    private MoviesFragment moviesFragment;

    public MoviesAdapter(Context context, ArrayList<Movie> movieArrayList, MoviesFragment moviesFragment) {
        this.moviesArrayList = movieArrayList;
        this.mContext = context;
        this.moviesFragment = moviesFragment;
    }

    final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w185";

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        String posterPath = moviesArrayList.get(position).getPosterPath();
        Picasso.with(mContext).load(BASE_IMAGE_URL + posterPath)
                .into(holder.ivMoviePoster);

        holder.ivMoviePoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moviesFragment.itemClicked(position);
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate
                (R.layout.item_movie, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return moviesArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivMoviePoster;

        public ViewHolder(View itemView) {
            super(itemView);

            ivMoviePoster = (ImageView) itemView.findViewById(R.id.iv_movie_pic);
        }
    }
}