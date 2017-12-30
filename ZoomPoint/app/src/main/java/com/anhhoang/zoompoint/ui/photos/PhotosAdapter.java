package com.anhhoang.zoompoint.ui.photos;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anhhoang.unsplashmodel.Photo;
import com.anhhoang.unsplashmodel.UserProfile;
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
 * Created by anh.hoang on 18.12.17.
 */

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.ViewHolder> {
    interface OnUserClickListener {
        void onUserClicked(UserProfile userProfile);
    }

    interface OnPhotoClickListener {
        void onPhotoClicked(String photoId);
    }

    private List<Photo> photos;
    private final OnUserClickListener userClickListener;
    private final OnPhotoClickListener photoClickListener;

    public PhotosAdapter(OnUserClickListener userClickListener, OnPhotoClickListener photoClickListener) {
        photos = new ArrayList<>();
        this.userClickListener = userClickListener;
        this.photoClickListener = photoClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.photo_item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        Photo photo = photos.get(position);
        holder.itemView.setTag(photo.getId());
        holder.userNameTv.setTag(photo.getUser());

        holder.userNameTv.setText(photo.getUser().getName());
        holder.photoIv.setContentDescription(context.getString(R.string.photo_content_description, photo.getUser().getName()));
        holder.photoIv.setAspectRatio((float) photo.getWidth() / photo.getHeight());
        Glide.with(holder.photoIv)
                .asDrawable()
                .load(photo.getUrls().getRegular())
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .skipMemoryCache(true))
                .transition(new DrawableTransitionOptions().crossFade())
                .into(holder.photoIv);
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public void addPhotos(List<Photo> newPhotos) {
        photos.clear();
        photos.addAll(newPhotos);
        notifyDataSetChanged();
    }


    public List<Photo> getPhotos() {
        return photos;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image_view_photo)
        DynamicSizeImageView photoIv;
        @BindView(R.id.text_view_user_name)
        TextView userNameTv;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String photoId = (String) v.getTag();
                    photoClickListener.onPhotoClicked(photoId);
                }
            });
            userNameTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserProfile userProfile = (UserProfile) v.getTag();
                    userClickListener.onUserClicked(userProfile);
                }
            });
        }
    }
}
