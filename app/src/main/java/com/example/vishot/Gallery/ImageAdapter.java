package com.example.vishot.Gallery;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.vishot.R;

import java.io.File;
import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Image> list;
    private OnImageclicklistener onImageclicklistener;
    private OnImagelongclicklistener onImagelongclicklistener;
    public void MyImageClick(OnImageclicklistener onImageclicklistener){
        this.onImageclicklistener = onImageclicklistener;
    }

    public void MyImageLongClick(OnImagelongclicklistener onImagelongclicklistener){
        this.onImagelongclicklistener = onImagelongclicklistener;
    }

    public ImageAdapter(Context context, ArrayList<Image> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.view_of_imagelist,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ImageAdapter.ViewHolder holder, int position) {
        final Image image = list.get(position);
        Glide.with(context).load(new File(image.getPath())).into(holder.imageView);
        holder.imageView.setClickable(true);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImageclicklistener.onclicklistener(image);
            }
        });
        holder.imageView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                onImagelongclicklistener.onlongclicklistener(image);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_trim);
            imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    imageView.setClickable(false);
                    return false;
                }
            });
        }
    }
    public interface OnImageclicklistener {
        void onclicklistener(Image image);
    }
    public void remove_item(int index){
        list.remove(index);
        notifyItemRemoved(index);
    }
    public  interface OnImagelongclicklistener{
        void onlongclicklistener(Image image);
    }
}
