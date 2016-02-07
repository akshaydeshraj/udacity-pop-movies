package com.axay.movies.ui.adapter.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.axay.movies.R;

/**
 * @author akshay
 * @since 7/2/16
 */
public class MovieViewHolder extends RecyclerView.ViewHolder {

    public ImageView ivBackdrop, ivPoster;
    public TextView tvOverView, tvReleaseDate, tvUserRating;
    public Button btnFavourite;
    
    public MovieViewHolder(View itemView) {
        super(itemView);

        ivBackdrop = (ImageView) itemView.findViewById(R.id.iv_backdrop);
        ivPoster = (ImageView) itemView.findViewById(R.id.iv_poster);
        tvOverView = (TextView) itemView.findViewById(R.id.tv_overview);
        tvReleaseDate = (TextView) itemView.findViewById(R.id.tv_release_date);
        tvUserRating = (TextView) itemView.findViewById(R.id.tv_user_rating);
        btnFavourite = (Button) itemView.findViewById(R.id.btn_favourite);
    }
}
