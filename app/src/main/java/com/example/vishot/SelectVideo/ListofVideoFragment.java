package com.example.vishot.SelectVideo;

import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vishot.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ListofVideoFragment extends Fragment {
    RecyclerView rv_Video;
    ArrayList<MyVideo> list_of_video;
    MediaMetadataRetriever mediaMetadataRetriever;
    GridLayoutManager layoutManager;
    DividerItemDecoration dividerItemDecoration;
    VideolistAdapter videolistAdapter;
    String folder_path;
    ProgressBar progressBar;
    int progress = 0;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_of_video,container,false);
        rv_Video = view.findViewById(R.id.rv_video);
        progressBar = view.findViewById(R.id.progress_video);
        try {
            Bundle bundle = getArguments();
            folder_path = bundle.getString("folder_path");
            if(folder_path!=null) {
                new Doing().execute();
            }
        }catch (Exception e){
            Log.i("error",e.getMessage());
        }

        return view;
    }
    public void getVideoinFolder(String path, String name){
        File folder = new File(path);
        list_of_video.clear();
        File list[] = folder.listFiles();
        File mFile = null;
        String directoryname = "";
        assert list != null;
        for(File video_file : list){
            mFile = new File(folder,video_file.getName());
            if(mFile.isDirectory()&&mFile.listFiles()!=null){
                directoryname = video_file.getName();
                getVideoinFolder(mFile.toString(),directoryname);
            }else{
                if(video_file.getName().toLowerCase(Locale.getDefault()).endsWith(".mp4")) {
                    String video_name = video_file.getName().substring(0,video_file.getName().length()-4);
                    mediaMetadataRetriever.setDataSource(video_file.getAbsolutePath());
                    String time = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                    time = format_time(time);
                    list_of_video.add(new MyVideo(video_file.getAbsolutePath(), video_name, time));
                }
            }
        }
    }

    public class Doing extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mediaMetadataRetriever = new MediaMetadataRetriever();
            list_of_video = new ArrayList<>();
            rv_Video.setHasFixedSize(true);
            layoutManager = new GridLayoutManager(getContext(),2,GridLayoutManager.VERTICAL,false);
            dividerItemDecoration = new DividerItemDecoration(getContext(),layoutManager.getOrientation());
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                getVideoinFolder(folder_path,"");
                videolistAdapter = new VideolistAdapter(getContext(),list_of_video);
                publishProgress();
            }catch (Exception e){
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {

            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.INVISIBLE);
            rv_Video.addItemDecoration(dividerItemDecoration);
            rv_Video.setLayoutManager(layoutManager);
            rv_Video.setAdapter(videolistAdapter);
            videolistAdapter.MyVideoClick(new VideolistAdapter.OnvideoClickListener() {
                    @Override
                    public void onVideoClick(MyVideo myVideo) {
                        Intent intent = new Intent(getActivity(),SnapvideoActivity.class);
                        intent.putExtra("video_path",myVideo.getVideo_path());
                        startActivity(intent);
                    }
                });

        }
    }
    public String format_time(String time_in_millisecond){
        long time = Long.parseLong(time_in_millisecond);
        long seconds = time / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        String hours_in_string = Long.toString(hours%24);
        String minute_in_string = Long.toString(minutes%60);
        String second_in_string = Long.toString(seconds%60);
        if(hours%24<10){
            hours_in_string = "0" + hours%24;
        }
        if(minutes%60<10){
            minute_in_string = "0"+ minutes%60;
        }
        if(seconds%60<10){
            second_in_string = "0"+seconds%60;
        }
        return hours_in_string + ":" + minute_in_string + ":" + second_in_string;
    }

}
