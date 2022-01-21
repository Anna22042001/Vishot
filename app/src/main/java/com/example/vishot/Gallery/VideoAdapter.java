package com.example.vishot.Gallery;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.vishot.R;

import java.io.File;
import java.util.ArrayList;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {
    ArrayList<Video> video_list = new ArrayList<>();
    Context context;
    private OnVideoClickListener onVideoClickListener;

    public void MyVideoClick(OnVideoClickListener onVideoClickListener){
        this.onVideoClickListener = onVideoClickListener;
    }

    public VideoAdapter(ArrayList<Video> video_list, Context context) {
        this.video_list = video_list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.view_of_video_gallery,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Video video = video_list.get(position);
        Glide.with(context).load(Uri.fromFile(new File(video.getVideo_path()))).into(holder.preview_of_video);
        holder.capacity.setText(video_list.get(position).getCapacity());
        holder.video_name.setText(video_list.get(position).getVideo_name());
        holder.date.setText(video_list.get(position).getDate());
        holder.time_limit.setText(video.getTime_limit());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onVideoClickListener.onVideoClick(video);
            }
        });

    }

    @Override
    public int getItemCount() {
        return video_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
         ImageView preview_of_video;
         TextView video_name;
         TextView capacity;
         TextView time_limit;
         TextView date;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            preview_of_video = itemView.findViewById(R.id.videoView);
            video_name = (TextView) itemView.findViewById(R.id.video_name);
            capacity = (TextView) itemView.findViewById(R.id.capacity);
            time_limit = (TextView) itemView.findViewById(R.id.duration);
            date = (TextView) itemView.findViewById(R.id.date);
        }
    }
    public interface OnVideoClickListener{
        void onVideoClick(Video video);
    }
}
