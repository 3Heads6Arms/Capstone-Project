package com.anhhoang.zoompoint.ui.photo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by anh.hoang on 29.12.17.
 */

public class Pair implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.description);
        dest.writeInt(this.id);
        dest.writeString(this.value);
    }

    protected Pair(Parcel in) {
        this.description = in.readInt();
        this.id = in.readInt();
        this.value = in.readString();
    }

    public static final Parcelable.Creator<Pair> CREATOR = new Parcelable.Creator<Pair>() {
        @Override
        public Pair createFromParcel(Parcel source) {
            return new Pair(source);
        }

        @Override
        public Pair[] newArray(int size) {
            return new Pair[size];
        }
    };
}
