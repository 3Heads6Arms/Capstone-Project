package com.anhhoang.zoompoint.ui.home.collections;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anhhoang.unsplashmodel.PhotoCollection;
import com.anhhoang.zoompoint.R;
import com.anhhoang.zoompoint.ui.DynamicSizeImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by anh.hoang on 22.12.17.
 */

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.ViewHolder> {
    private final OnCollectionItemClickListener itemClickListener;

    interface OnCollectionItemClickListener {
        void onCollectionItemClicked(PhotoCollection collection);
    }

    private List<PhotoCollection> collections;

    public CollectionAdapter(OnCollectionItemClickListener itemClickListener) {
        collections = new ArrayList<>();
        this.itemClickListener = itemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.collection_item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PhotoCollection collection = collections.get(position);
        holder.itemView.setTag(collection);

        holder.titleTv.setText(collection.getTitle());

        if (collection.getPhoto() != null && collection.getPhoto().getUrls() != null) {
            holder.photoIv.setAspectRatio((float) collection.getPhoto().getWidth() / collection.getPhoto().getHeight());
            Glide.with(holder.photoIv)
                    .asDrawable()
                    .load(collection.getPhoto().getUrls().getRegular())
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.DATA)
                            .skipMemoryCache(true))
                    .transition(new DrawableTransitionOptions().crossFade())
                    .into(holder.photoIv);
        }
    }

    @Override
    public int getItemCount() {
        return collections.size();
    }

    public List<PhotoCollection> getCollections() {
        return collections;
    }

    public void addCollections(List<PhotoCollection> collections) {
        this.collections.addAll(collections);
        notifyDataSetChanged();
    }

    public void clearCollections() {
        collections.clear();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image_view_photo)
        DynamicSizeImageView photoIv;
        @BindView(R.id.text_view_title)
        TextView titleTv;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        PhotoCollection collection = (PhotoCollection) v.getTag();
                        itemClickListener.onCollectionItemClicked(collection);
                    }
                }
            });
        }
    }
}
