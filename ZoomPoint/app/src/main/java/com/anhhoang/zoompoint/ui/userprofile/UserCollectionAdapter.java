package com.anhhoang.zoompoint.ui.userprofile;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anhhoang.unsplashmodel.PhotoCollection;
import com.anhhoang.zoompoint.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by anh.hoang on 27.12.17.
 */

public class UserCollectionAdapter extends RecyclerView.Adapter<UserCollectionAdapter.ViewHolder> {
    interface OnCollectionClickListener {
        void onCollectionClicked(PhotoCollection collection);
    }

    private final OnCollectionClickListener clickListener;
    private List<PhotoCollection> data;

    public UserCollectionAdapter(OnCollectionClickListener clickListener) {
        this.clickListener = clickListener;
        data = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.collections_userprofile_item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PhotoCollection collection = data.get(position);
        holder.itemView.setTag(collection);

        holder.titleTv.setText(collection.getTitle());
        holder.photoIv.setContentDescription(collection.getTitle());

        if (collection.getPhoto() != null && collection.getPhoto().getUrls() != null) {
            Glide.with(holder.photoIv)
                    .asDrawable()
                    .load(collection.getPhoto().getUrls().getRegular())
                    .apply(new RequestOptions()
                            .error(R.drawable.ic_image_placeholder)
                            .diskCacheStrategy(DiskCacheStrategy.DATA)
                            .skipMemoryCache(true))
                    .transition(new DrawableTransitionOptions().crossFade())
                    .into(holder.photoIv);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void updateCollections(List<PhotoCollection> collections) {
        data.clear();
        data.addAll(collections);
        notifyDataSetChanged();
    }

    public List<PhotoCollection> getCollections() {
        return data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image_view_photo)
        ImageView photoIv;
        @BindView(R.id.text_view_title)
        TextView titleTv;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PhotoCollection photoCollection = (PhotoCollection) v.getTag();
                    clickListener.onCollectionClicked(photoCollection);
                }
            });
        }
    }
}
