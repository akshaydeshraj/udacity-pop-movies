package com.axay.movies.ui.adapter.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.axay.movies.R;

/**
 * @author akshay
 * @since 7/2/16
 */
public class TrailerViewHolder extends RecyclerView.ViewHolder {

    public TextView tvTrailerName;

    public TrailerViewHolder(View itemView) {
        super(itemView);

        tvTrailerName = (TextView) itemView.findViewById(R.id.tv_trailer_name);
    }
}
