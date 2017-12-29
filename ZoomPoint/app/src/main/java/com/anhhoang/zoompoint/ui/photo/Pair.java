package com.anhhoang.zoompoint.ui.photo;

/**
 * Created by anh.hoang on 29.12.17.
 */

public class Pair {
    public int description;
    public int id;
    public String value;

    public Pair(int id, String value) {
        this(id, value, -1);
    }

    public Pair(int id, String value, int description) {
        this.id = id;
        this.value = value;
        this.description = description;
    }
}
