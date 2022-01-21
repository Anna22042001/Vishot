package com.example.vishot.Slideshow;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vishot.Gallery.Image;
import com.example.vishot.R;

import java.util.ArrayList;

public class ImagetochooseAdapter extends RecyclerView.Adapter<ImagetochooseAdapter. ViewHolder> {
    Context context;
    ArrayList<Image> list_of_image;
    private OnImageChooseListener onImageChooseListener;

    public void MyImageChoose(OnImageChooseListener onImageChooseListener){
        this.onImageChooseListener = onImageChooseListener;
    }

    public ImagetochooseAdapter(Context context, ArrayList<Image> list_of_image) {
        this.context = context;
        this.list_of_image = list_of_image;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.view_of_image_to_choose,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImagetochooseAdapter.ViewHolder holder, int position) {
        final Image image = list_of_image.get(position);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        Bitmap mbitmap = BitmapFactory.decodeFile(image.getPath(),options);
        holder.imageView.setImageBitmap(mbitmap);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImageChooseListener.onchooselistener(image);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list_of_image.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_to_choose);
        }
    }
    public interface OnImageChooseListener{
        void onchooselistener(Image image);
    }
}
