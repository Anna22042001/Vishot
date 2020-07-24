package com.example.vishot.SelectVideo;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.vishot.R;

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
        MyVideo myVideo = list_of_video.get(position);
        holder.duration.setText(myVideo.getVideo_duration());
        holder.name.setText(myVideo.getVideo_name());
        Glide.with(context)
                .load(Uri.parse(myVideo.getVideo_path()))
                .into(holder.preview);
    }

    @Override
    public int getItemCount() {
        return list_of_video.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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
