package com.example.vishot.Home;

public class Main_button {
    private int image_resource;
    private int text_resource;

    public Main_button(int image_resource, int text_resource) {
        this.image_resource = image_resource;
        this.text_resource = text_resource;
    }

    public int getImage_resource() {
        return image_resource;
    }

    public void setImage_resource(int image_resource) {
        this.image_resource = image_resource;
    }

    public int getText_resource() {
        return text_resource;
    }

    public void setText_resource(int text_resource) {
        this.text_resource = text_resource;
    }
}
