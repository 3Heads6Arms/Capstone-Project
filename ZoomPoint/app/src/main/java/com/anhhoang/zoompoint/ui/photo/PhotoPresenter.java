package com.anhhoang.zoompoint.ui.photo;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

import com.anhhoang.unsplashapi.UnsplashApi;
import com.anhhoang.unsplashmodel.Exif;
import com.anhhoang.unsplashmodel.Photo;
import com.anhhoang.unsplashmodel.PhotoCollection;
import com.anhhoang.unsplashmodel.UserProfile;
import com.anhhoang.zoompoint.R;
import com.anhhoang.zoompoint.ui.photo.PhotoContract.Presenter;
import com.anhhoang.zoompoint.utils.PhotoUtils;

import java.net.HttpURLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by anh.hoang on 29.12.17.
 */

public class PhotoPresenter implements Presenter {
    private PhotoContract.View view;
    private Photo photo;
    private List<Pair> myCollections;
    private UnsplashApi unsplashApi;
    private boolean hasError;
    private String photoType;
    private boolean isLoading;

    private Callback<Photo> photoCallback = new Callback<Photo>() {
        @Override
        public void onResponse(Call<Photo> call, Response<Photo> response) {
            if (view != null) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    hasError = false;
                    photo = response.body();
                    view.updatePhoto(PhotoUtils.parsePhoto(photo, photoType));
                    loadFinished(photo);
                } else {
                    hasError = true;
                    view.toggleProgress(false);
                    view.showError(R.string.unable_to_get_photo);
                }
            }
            isLoading = false;
        }

        @Override
        public void onFailure(Call<Photo> call, Throwable t) {
            hasError = true;
            isLoading = false;
            if (view != null) {
                view.toggleProgress(false);
                view.showError(R.string.unable_to_get_photo);
            }
        }
    };
    private Callback<Photo> likeCallback = new Callback<Photo>() {
        @Override
        public void onResponse(Call<Photo> call, Response<Photo> response) {
            if (view != null) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    hasError = false;

                    // Update likes upon success update with server
                    photo.setLikedByUser(!photo.isLikedByUser());
                    photo.setLikes(photo.isLikedByUser() ? photo.getLikes() + 1 : photo.getLikes() - 1);

                    ContentValues contentValues = PhotoUtils.parsePhoto(photo, photoType);
                    view.updatePhoto(contentValues);
                    view.displayLikes(photo.isLikedByUser(), photo.getLikes());
                } else {
                    hasError = true;
                    view.toggleProgress(false);
                    view.showError(R.string.unable_to_connect);
                }
                view.toggleProgress(false);
            }
            isLoading = false;
        }

        @Override
        public void onFailure(Call<Photo> call, Throwable t) {
            hasError = true;
            isLoading = false;
            if (view != null) {
                view.toggleProgress(false);
                view.showError(R.string.unable_to_connect);
            }
        }
    };
    private Callback<Photo> addToCollectionCallback = new Callback<Photo>() {
        @Override
        public void onResponse(Call<Photo> call, Response<Photo> response) {
            if (view != null) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    hasError = false;
                    view.showError(R.string.success);
                } else {
                    hasError = true;
                    view.toggleProgress(false);
                    view.showError(R.string.unable_to_connect);
                }
                view.toggleProgress(false);
            }
            isLoading = false;
        }

        @Override
        public void onFailure(Call<Photo> call, Throwable t) {
            hasError = true;
            isLoading = false;
            if (view != null) {
                view.toggleProgress(false);
                view.showError(R.string.unable_to_connect);
            }
        }
    };
    private Callback<List<PhotoCollection>> collectionsCallback = new Callback<List<PhotoCollection>>() {
        @Override
        public void onResponse(Call<List<PhotoCollection>> call, Response<List<PhotoCollection>> response) {
            if (response.code() == HttpURLConnection.HTTP_OK) {
                hasError = false;

                for (PhotoCollection collection : response.body()) {
                    myCollections.add(new Pair(collection.getId(), collection.getTitle()));
                }
            } else {
                hasError = true;
            }
        }

        @Override
        public void onFailure(Call<List<PhotoCollection>> call, Throwable t) {
            hasError = true;
        }
    };

    public PhotoPresenter() {
        myCollections = new ArrayList<>();
    }

    @Override
    public void attach(PhotoContract.View view) {
        this.view = view;

        if (view != null) {
            view.setPresenter(this);
            unsplashApi = UnsplashApi.getInstance(view.getToken());
        }
    }

    @Override
    public void detach() {
        view = null;
    }

    @Override
    public void load(String photoId) {
        if (view != null && !isLoading) {
            isLoading = true;
            view.toggleProgress(true);
            unsplashApi.getPhoto(photoId)
                    .enqueue(photoCallback);
        }
    }

    @Override
    public void loadFinished(Cursor data) {
        if (data.moveToFirst()) {
            photo = PhotoUtils.parsePhoto(data);
            photoType = photo.getType();
            loadFinished(photo);
        }
    }

    @Override
    public void onUserSelected() {
        if (view != null) {
            UserProfile user = photo.getUser();
            view.openUser(user.getUsername(), user.getName());
        }
    }

    @Override
    public void onLikeButtonSelected() {
        if (view != null && !isLoading) {
            if (hasError) {
                view.showError(R.string.unable_to_connect);
            } else {
                isLoading = true;
                view.toggleProgress(true);
                if (photo.isLikedByUser()) {
                    unsplashApi.unlikePhoto(photo.getId()).enqueue(likeCallback);
                } else {
                    unsplashApi.likePhoto(photo.getId()).enqueue(likeCallback);
                }
            }
        }
    }

    @Override
    public void onDownloadSelected() {
        // TODO
    }

    @Override
    public void onSetWallpaperSelected() {
        // TODO

    }

    @Override
    public void onAddToCollection() {
        if (view != null) {
            if (hasError) {
                view.showError(R.string.unable_to_connect);
            } else if (myCollections.size() == 0) {
                view.showError(R.string.no_collection_found);
            } else {
                view.displayCollections(myCollections);
            }
        }
    }

    @Override
    public void onCollectionSelected(int collectionId, String photoId) {
        if (view != null && !isLoading) {
            isLoading = true;
            view.toggleProgress(true);
            unsplashApi.addPhotoToCollection(collectionId, photoId)
                    .enqueue(addToCollectionCallback);
        }
    }

    @Override
    public void loadMyCollections() {
        if (view != null) {
            unsplashApi.getUserCollections(
                    view.getMyUsername(),
                    1,
                    50)
                    .enqueue(collectionsCallback);
        }
    }

    private void loadFinished(Photo photo) {
        if (view != null) {
            view.toggleProgress(false);
            UserProfile user = photo.getUser();

            view.displayPhotoImage(photo.getUrls().getRegular());
            view.displayUser(
                    user.getName(),
                    user.getUsername(),
                    user.getProfileImage().getLarge());
            view.displayLikes(photo.isLikedByUser(), photo.getLikes());

            if (!TextUtils.isEmpty(photo.getDescription())) {
                view.displayDescription(photo.getDescription());
            }

            Exif exif = photo.getExif();
            processExif(exif, photo.getUpdatedAt());
        }
    }

    private void processExif(Exif exif, Date updatedAt) {
        if (exif != null) {
            List<Pair> pairs = new ArrayList<>();
            if (exif.getMake() != null) {
                if (exif.getModel() != null) {
                    if (exif.getModel().toLowerCase().contains(exif.getMake().toLowerCase())) {
                        pairs.add(new Pair(
                                R.drawable.ic_camera,
                                exif.getModel()
                        ));
                    } else {
                        pairs.add(new Pair(
                                R.drawable.ic_camera,
                                exif.getMake() + " " + exif.getModel()
                        ));
                    }
                } else {
                    pairs.add(new Pair(
                            R.drawable.ic_camera,
                            exif.getMake()
                    ));
                }
            } else {
                pairs.add(new Pair(
                        R.drawable.ic_camera,
                        "N/A"
                ));
            }
            if (exif.getFocalLength() > 0) {
                pairs.add(new Pair(
                        R.drawable.ic_length,
                        String.format("%dmm", exif.getFocalLength())
                ));
            }
            if (exif.getAperture() > 0) {
                pairs.add(new Pair(
                        R.drawable.ic_aperture,
                        String.format("f/%.1f", exif.getAperture())
                ));
            }
            if (exif.getIso() > 0) {
                pairs.add(new Pair(
                        R.drawable.ic_iso,
                        String.valueOf(exif.getIso())));
            }

            if (updatedAt != null) {
                DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
                pairs.add(new Pair(
                        R.drawable.ic_calendar,
                        dateFormat.format(updatedAt)
                ));
            }


            if (pairs.size() == 0) {
                view.showEmptyExif();
            } else {
                view.displayExif(pairs);
            }
        }
    }
}
