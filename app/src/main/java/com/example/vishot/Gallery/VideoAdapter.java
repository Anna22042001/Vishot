package com.example.vishot.Gallery;

import android.content.Context;
import android.media.MediaRecorder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vishot.R;

import java.util.ArrayList;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {
    ArrayList<Video> video_list = new ArrayList<>();
    Context context;

    public VideoAdapter(ArrayList<Video> video_list, Context context) {
        this.video_list = video_list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.video_recyclerview,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.video.setVideoURI(video_list.get(position).getVideo());
        holder.time_limit.setText(video_list.get(position).getTime_limit());
        holder.capacity.setText(video_list.get(position).getCapacity());
        holder.video_name.setText(video_list.get(position).getVideo_name());
        holder.date.setText(video_list.get(position).getDate());

    }

    @Override
    public int getItemCount() {
        return video_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
         VideoView video;
         TextView video_name;
         TextView capacity;
         TextView time_limit;
         TextView date;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            video = (VideoView) itemView.findViewById(R.id.videoView);
            video_name = (TextView) itemView.findViewById(R.id.video_name);
            capacity = (TextView) itemView.findViewById(R.id.capacity);
            time_limit = (TextView) itemView.findViewById(R.id.time_limit);
            date = (TextView) itemView.findViewById(R.id.date);
        }
    }


}
