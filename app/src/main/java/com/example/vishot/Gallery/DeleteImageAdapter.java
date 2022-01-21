package com.example.vishot.Gallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.vishot.R;
import com.example.vishot.Slideshow.ImagechosenAdapter;

import java.io.File;
import java.util.ArrayList;

public class DeleteImageAdapter extends RecyclerView.Adapter<DeleteImageAdapter.ViewHolder> {
    Context context;
    ArrayList<Image> list_of_image;
    private ImagechosenAdapter.OnImageDeleteListener onImageDeleteListener;
    static ArrayList<Image> num_of_chosen = new ArrayList<>();
    int chosen = -1;

    public void DeleteImage(ImagechosenAdapter.OnImageDeleteListener onImageDeleteListener){
        this.onImageDeleteListener = onImageDeleteListener;
    }

    public DeleteImageAdapter(Context context, ArrayList<Image> list_of_image) {
        this.context = context;
        this.list_of_image = list_of_image;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.view_of_image_to_delete, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public int getItemCount() {
        return list_of_image.size();
    }


    @Override
    public void onBindViewHolder(@NonNull final DeleteImageAdapter.ViewHolder holder, final int position) {
         final Image image = list_of_image.get(position);
        Glide.with(context).load(Uri.parse(image.getPath())).into(holder.imageView);
        if(num_of_chosen.contains(image)){
            holder.delete.setImageResource(R.drawable.ic_baseline_check_circle_24);
        }else {
            holder.delete.setImageResource(R.drawable.ic_outline_check_circle_24);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!num_of_chosen.contains(image)) {
                    num_of_chosen.add(image);
                    holder.delete.setImageResource(R.drawable.ic_baseline_check_circle_24);
                }else {
                    num_of_chosen.remove(image);
                    holder.delete.setImageResource(R.drawable.ic_outline_check_circle_24);
                }
                onImageDeleteListener.onimagedelete(image);
            }
        });
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_to_delete);
            delete = itemView.findViewById(R.id.choose);


        }
    }
    public interface OnImageDeleteListener{
        void onimagedelete(Image image);
    }
}



