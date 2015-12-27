package com.axay.movies.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.axay.movies.R;
import com.axay.movies.ui.data.Movie;

import java.util.ArrayList;

/**
 * @author akshay
 * @since 26/12/15
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

    private ArrayList<Movie> moviesArrayList;

    public MoviesAdapter(ArrayList<Movie> movieArrayList) {
        this.moviesArrayList = movieArrayList;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvMovieName.setText(moviesArrayList.get(position).getTitle());
        holder.tvMovieGenre.setText(String.valueOf(position));
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

        TextView tvMovieName, tvMovieGenre;

        public ViewHolder(View itemView) {
            super(itemView);

            ivMoviePoster = (ImageView) itemView.findViewById(R.id.iv_movie_pic);
            tvMovieName = (TextView) itemView.findViewById(R.id.tv_movie_name);
            tvMovieGenre = (TextView) itemView.findViewById(R.id.tv_movie_genre);
        }
    }
}