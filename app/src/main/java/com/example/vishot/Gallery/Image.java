package com.example.vishot.Gallery;

import android.graphics.Bitmap;

public class Image {
    private String date;
    private Bitmap image;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
