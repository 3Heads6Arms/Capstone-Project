package com.anhhoang.zoompoint.ui.photocollection;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.anhhoang.unsplashmodel.Photo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anh.hoang on 18.12.17.
 */

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.ViewHolder> {
    private final List<Photo> photos;

    public PhotosAdapter() {
        photos = new ArrayList<Photo>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // TODO:
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // TODO:
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public void addPhotos(List<Photo> newPhotos) {
        photos.addAll(newPhotos);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
