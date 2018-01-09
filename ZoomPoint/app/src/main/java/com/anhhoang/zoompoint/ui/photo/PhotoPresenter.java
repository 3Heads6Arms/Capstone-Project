package com.anhhoang.zoompoint.ui.photo;

import android.app.DownloadManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

import com.anhhoang.unsplashapi.UnsplashApi;
import com.anhhoang.unsplashmodel.Exif;
import com.anhhoang.unsplashmodel.Photo;
import com.anhhoang.unsplashmodel.PhotoCollection;
import com.anhhoang.unsplashmodel.PhotoLocation;
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
                    view.showError(R.string.unable_to_get_photo);
                    view.loadPhotoFromLocalDb();
                }
            }
            isLoading = false;
        }

        @Override
        public void onFailure(Call<Photo> call, Throwable t) {
            hasError = true;
            isLoading = false;
            if (view != null) {
                view.showError(R.string.unable_to_get_photo);
                view.loadPhotoFromLocalDb();
            }
        }
    };
    private Callback<Photo> likeCallback = new Callback<Photo>() {
        @Override
        public void onResponse(Call<Photo> call, Response<Photo> response) {
            if (view != null) {
                if (response.code() == HttpURLConnection.HTTP_OK || response.code() == HttpURLConnection.HTTP_CREATED) {
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
                if (response.code() == HttpURLConnection.HTTP_OK || response.code() == HttpURLConnection.HTTP_CREATED) {
                    hasError = false;
                    view.showError(R.string.success);
                } else {
                    hasError = true;
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
    public void load(String photoId, String photoType) {
        if (view != null && !isLoading) {
            isLoading = true;
            this.photoType = photoType;
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
    public void onDownloadSelected(Context context) {
        if (photo == null) {
            if (view != null) {
                view.showError(R.string.unable_to_get_photo);
            }
        } else {
            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            DownloadManager.Request request = new DownloadManager.Request(
                    Uri.parse(getPhotoDownloadUrl())
            );
            request.setTitle(context.getString(R.string.download_title));
            request.setDescription(context.getString(R.string.download_description));
            request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, photo.getId() + ".jpg");
            downloadManager.enqueue(request);
        }
    }

    @Override
    public void onSetWallpaperSelected() {
        if (photo != null && view != null) {
            view.setWallpaper(getPhotoDownloadUrl());
        }
    }

    @Override
    public void onAddToCollectionSelected() {
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

    @Override
    public void onLocationSelected() {
        if (view != null) {
            if (photo != null &&
                    photo.getLocation() != null &&
                    photo.getLocation().getPosition() != null &&
                    !TextUtils.isEmpty(getValidLocationString(photo.getLocation())) &&
                    (photo.getLocation().getPosition().getLatitude() != 0 ||
                            photo.getLocation().getPosition().getLongitude() != 0)) {
                view.openLocation(
                        photo.getLocation().getPosition().getLatitude(),
                        photo.getLocation().getPosition().getLongitude());
            } else {
                view.showError(R.string.invalid_position);
            }
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


            String location = getValidLocationString(photo.getLocation());
            if (!TextUtils.isEmpty(location)) {
                view.displayLocation(location);
            }

            view.displayLikes(photo.isLikedByUser(), photo.getLikes());

            if (!TextUtils.isEmpty(photo.getDescription())) {
                view.displayDescription(photo.getDescription());
            }

            Exif exif = photo.getExif();
            processExif(exif, photo.getUpdatedAt());
            loadMyCollections();
        }
    }

    private String getValidLocationString(PhotoLocation photoLocation) {
        String location = "";
        if (photoLocation != null) {
            if (!TextUtils.isEmpty(photoLocation.getCity())) {
                location += photoLocation.getCity();
            }
            if (!TextUtils.isEmpty(photoLocation.getCountry())) {
                if (TextUtils.isEmpty(location)) {
                    location += photoLocation.getCountry();
                } else {
                    location += ", " + photoLocation.getCountry();
                }
            }
        }

        return location;
    }

    private void processExif(Exif exif, Date updatedAt) {
        if (exif != null) {
            List<Pair> pairs = new ArrayList<>();
            if (exif.getMake() != null) {
                if (exif.getModel() != null) {
                    if (exif.getModel().toLowerCase().contains(exif.getMake().toLowerCase())) {
                        pairs.add(new Pair(
                                R.drawable.ic_camera,
                                exif.getModel(),
                                R.string.camera_icon
                        ));
                    } else {
                        pairs.add(new Pair(
                                R.drawable.ic_camera,
                                exif.getMake() + " " + exif.getModel(),
                                R.string.camera_icon
                        ));
                    }
                } else {
                    pairs.add(new Pair(
                            R.drawable.ic_camera,
                            exif.getMake(),
                            R.string.camera_icon
                    ));
                }
            } else {
                pairs.add(new Pair(
                        R.drawable.ic_camera,
                        "N/A",
                        R.string.camera_icon
                ));
            }
            if (!TextUtils.isEmpty(exif.getFocalLength())) {
                pairs.add(new Pair(
                        R.drawable.ic_length,
                        String.format("%smm", exif.getFocalLength()),
                        R.string.focal_length_icon
                ));
            }
            if (!TextUtils.isEmpty(exif.getAperture())) {
                pairs.add(new Pair(
                        R.drawable.ic_aperture,
                        String.format("f/%s", exif.getAperture()),
                        R.string.aperture_icon
                ));
            }
            if (exif.getIso() > 0) {
                pairs.add(new Pair(
                        R.drawable.ic_iso,
                        String.valueOf(exif.getIso()),
                        R.string.iso_icon
                ));
            }

            if (updatedAt != null) {
                DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
                pairs.add(new Pair(
                        R.drawable.ic_calendar,
                        dateFormat.format(updatedAt),
                        R.string.calendar_icon
                ));
            }


            if (pairs.size() == 0) {
                view.showEmptyExif();
            } else {
                view.displayExif(pairs);
            }
        }
    }

    private String getPhotoDownloadUrl() {
        return "https://unsplash.com/photos/" + photo.getId() + "/download";
    }
}
