package com.example.vishot.Gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.vishot.R;

import wseemann.media.FFmpegMediaMetadataRetriever;

public class VideoDisplayFragment extends Fragment {
    VideoView videoView;
    String video_path = "";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_display_video,container,false);
        Bundle bundle = getArguments();
        video_path = bundle.getString("video_path");
        videoView = view.findViewById(R.id.display_video);
        videoView.setVideoPath(video_path);
        MediaController mediaController = new MediaController(getContext());
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);
        videoView.start();
        return view;
    }
}
