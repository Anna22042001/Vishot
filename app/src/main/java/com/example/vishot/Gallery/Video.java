package com.example.vishot.Gallery;

import android.net.Uri;

public class Video {
    private String video_path;
    private String video_name;
    private String capacity;
    private String time_limit;
    private String date;

    public Video(String video_path, String video_name, String capacity, String time_limit, String date) {
        this.video_path = video_path;
        this.video_name = video_name;
        this.capacity = capacity;
        this.time_limit = time_limit;
        this.date = date;
    }

    public String getVideo_path() {
        return video_path;
    }

    public void setVideo_path(String video_path) {
        this.video_path = video_path;
    }

    public String getVideo_name() {
        return video_name;
    }

    public void setVideo_name(String video_name) {
        this.video_name = video_name;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getTime_limit() {
        return time_limit;
    }

    public void setTime_limit(String time_limit) {
        this.time_limit = time_limit;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
