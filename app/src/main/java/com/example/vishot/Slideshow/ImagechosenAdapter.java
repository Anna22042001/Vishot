package com.example.vishot.Slideshow;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vishot.Gallery.Image;
import com.example.vishot.R;

import java.util.ArrayList;

public class ImagechosenAdapter extends RecyclerView.Adapter<ImagechosenAdapter.ViewHolder> {
    Context context;
    ArrayList<Image> list_of_image;
    private OnImageDeleteListener onImageDeleteListener;

    public void DeleteImage(OnImageDeleteListener onImageDeleteListener){
        this.onImageDeleteListener = onImageDeleteListener;
    }

    public ImagechosenAdapter(Context context, ArrayList<Image> list_of_image) {
        this.context = context;
        this.list_of_image = list_of_image;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.view_of_image_chosen, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImagechosenAdapter.ViewHolder holder, final int position) {
        final Image image = list_of_image.get(position);
        BitmapFactory.Options options= new  BitmapFactory.Options();
        options.inSampleSize = 7;
        Bitmap bitmap = BitmapFactory.decodeFile(image.getPath(),options);
        holder.imageView.setImageBitmap(bitmap);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImageDeleteListener.onimagedelete(image);
            }
        });
        holder.imageView.setClickable(false);
    }

    @Override
    public int getItemCount() {
        return list_of_image.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView,delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_chosen);
            delete = itemView.findViewById(R.id.delete_from_list);
        }
    }
    public interface OnImageDeleteListener{
        void onimagedelete(Image image);
    }
}
