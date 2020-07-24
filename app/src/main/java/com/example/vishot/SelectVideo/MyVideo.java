package com.example.vishot.SelectVideo;

public class MyVideo {
    private String video_path;
    private String video_name;
    private String video_duration;

    public MyVideo(String video_path, String video_name, String video_duration) {
        this.video_path = video_path;
        this.video_name = video_name;
        this.video_duration = video_duration;
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

    public String getVideo_duration() {
        return video_duration;
    }

    public void setVideo_duration(String video_duration) {
        this.video_duration = video_duration;
    }
}
