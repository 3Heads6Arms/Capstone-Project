package com.anhhoang.unsplashapi.RequestModel;

import com.anhhoang.unsplashmodel.Photo;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by anh.hoang on 24.12.17.
 */

public class RequestSearchPhoto {
    @SerializedName("total")
    private int total;
    @SerializedName("total_pages")
    private int totalPages;
    @SerializedName("results")
    private List<Photo> results;

    public int getTotal() {
        return total;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public List<Photo> getResults() {
        return results;
    }
}
