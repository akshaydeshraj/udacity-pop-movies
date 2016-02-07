package com.axay.movies.ui.adapter.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.axay.movies.R;

/**
 * @author akshay
 * @since 7/2/16
 */
public class ReviewViewHolder extends RecyclerView.ViewHolder {

    public LinearLayout llReview;

    public TextView tvReviewContent;
    public TextView tvReviewAuthor;

    public ReviewViewHolder(View itemView) {
        super(itemView);

        llReview = (LinearLayout) itemView.findViewById(R.id.ll_review);
        tvReviewContent = (TextView) itemView.findViewById(R.id.tv_review_content);
        tvReviewAuthor = (TextView) itemView.findViewById(R.id.tv_review_author);
    }
}
