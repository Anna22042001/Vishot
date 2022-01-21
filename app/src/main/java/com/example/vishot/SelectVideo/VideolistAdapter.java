package com.example.vishot.SelectVideo;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.vishot.R;

import java.io.File;
import java.util.ArrayList;

public class VideolistAdapter extends RecyclerView.Adapter<VideolistAdapter.ViewHolder> {
    Context context;
    ArrayList<MyVideo> list_of_video;
    private OnvideoClickListener onvideoClickListener;

    public void MyVideoClick(OnvideoClickListener onvideoClickListener){
        this.onvideoClickListener = onvideoClickListener;
    }

    public VideolistAdapter(Context context, ArrayList<MyVideo> list_of_video) {
        this.context = context;
        this.list_of_video = list_of_video;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.view_list_of_video,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideolistAdapter.ViewHolder holder, int position) {
        final MyVideo myVideo = list_of_video.get(position);
        Glide.with(context).load(Uri.fromFile(new File(myVideo.getVideo_path()))).into(holder.preview);
        holder.duration.setText(myVideo.getVideo_duration());
        holder.name.setText(myVideo.getVideo_name());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onvideoClickListener.onVideoClick(myVideo);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list_of_video.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView duration,name;
        ImageView preview;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            duration = itemView.findViewById(R.id.duration);
            name = itemView.findViewById(R.id.name);
            preview = itemView.findViewById(R.id.preview_of_video);
        }
    }
    public interface OnvideoClickListener{
        void onVideoClick(MyVideo myVideo);
    }
}
