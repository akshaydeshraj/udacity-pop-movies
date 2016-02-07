package com.axay.movies.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.axay.movies.R;
import com.axay.movies.data.api.model.ReviewsResponse;

import java.util.List;

/**
 * @author akshay
 * @since 7/2/16
 */
public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter.ViewHolder> {

    private Context context;
    private List<ReviewsResponse.Review> reviewList;

    public MovieReviewAdapter(Context context, List<ReviewsResponse.Review> reviewList) {
        this.context = context;
        this.reviewList = reviewList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate
                (R.layout.item_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ReviewsResponse.Review review = reviewList.get(position);

        holder.tvReviewContent.setText(review.getContent());
        holder.tvReviewAuthor.setText(review.getAuthor());

        holder.llReview.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(review.getUrl()));
            context.startActivity(i);
        });

    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout llReview;

        TextView tvReviewContent;
        TextView tvReviewAuthor;

        public ViewHolder(View itemView) {
            super(itemView);

            llReview = (LinearLayout) itemView.findViewById(R.id.ll_review);
            tvReviewContent = (TextView) itemView.findViewById(R.id.tv_review_content);
            tvReviewAuthor = (TextView) itemView.findViewById(R.id.tv_review_author);
        }
    }

}
