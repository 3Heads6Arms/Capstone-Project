package com.anhhoang.zoompoint.ui.photo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anhhoang.zoompoint.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by anh.hoang on 29.12.17.
 */

public class ExifAdapter extends RecyclerView.Adapter<ExifAdapter.ViewHolder> {
    private List<Pair> items;

    public ExifAdapter() {
        items = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.exif_item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Pair pair = items.get(position);
        Context context = holder.itemView.getContext();
        holder.exifIconIv.setImageResource(pair.id);
        holder.exifIconIv.setContentDescription(context.getString(pair.description));
        holder.exifTv.setText(pair.value);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateExifs(List<Pair> exif) {
        items.clear();
        items.addAll(exif);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image_view_exif_icon)
        ImageView exifIconIv;
        @BindView(R.id.text_view_exif)
        TextView exifTv;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
