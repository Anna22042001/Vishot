package com.example.vishot.Gallery;

import android.graphics.Bitmap;

public class Image {
    private String date;
    private String path;

    public Image(String date, String path) {
        this.date = date;
        this.path = path;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
