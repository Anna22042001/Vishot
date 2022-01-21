package com.example.vishot.Slideshow;

public class FolderofImage {
    String name;
    int image_num;
    String path;

    public FolderofImage(String name, int image_num, String path) {
        this.name = name;
        this.image_num = image_num;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage_num() {
        return image_num;
    }

    public void setImage_num(int image_num) {
        this.image_num = image_num;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
