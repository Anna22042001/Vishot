package com.example.vishot.SelectVideo;

import android.graphics.Bitmap;

public class Image_cut {
    Bitmap bitmap;

    public Image_cut(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
