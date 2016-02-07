package com.axay.movies.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.axay.movies.R;
import com.axay.movies.data.api.model.VideoResponse;

import java.util.List;

/**
 * @author akshay
 * @since 7/2/16
 */
public class MovieTrailersAdapter extends RecyclerView.Adapter<MovieTrailersAdapter.ViewHolder> {

    private Context context;
    private List<VideoResponse.Trailer> trailerList;

    public MovieTrailersAdapter(Context context, List<VideoResponse.Trailer> trailerList) {
        this.context = context;
        this.trailerList = trailerList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate
                (R.layout.item_trailer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        VideoResponse.Trailer trailer = trailerList.get(position);

        holder.tvTrailerName.setText(trailer.getName());
        holder.tvTrailerName.setOnClickListener(v -> {
            String url = "https://www.youtube.com/watch?v=" + trailer.getKey();
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return trailerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTrailerName;

        public ViewHolder(View itemView) {
            super(itemView);

            tvTrailerName = (TextView) itemView.findViewById(R.id.tv_trailer_name);
        }
    }
}
