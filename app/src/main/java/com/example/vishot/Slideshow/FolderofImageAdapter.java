package com.example.vishot.Slideshow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vishot.R;

import java.util.ArrayList;

public class FolderofImageAdapter extends RecyclerView.Adapter<FolderofImageAdapter.ViewHolder> {
    ArrayList<FolderofImage> list_of_folder;
    Context context;
    private OnFolderClickListener onFolderClickListener;

    public void MyFolderClick(OnFolderClickListener onFolderClickListener){
        this.onFolderClickListener = onFolderClickListener;
    }

    public FolderofImageAdapter(ArrayList<FolderofImage> list_of_folder, Context context) {
        this.list_of_folder = list_of_folder;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_of_image_folder,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FolderofImageAdapter.ViewHolder holder, int position) {
        final FolderofImage folder = list_of_folder.get(position);
        String text = folder.getName() ;
        String num = "(" + folder.getImage_num() + ")";
        holder.textView.setText(text);
        holder.num_of_image.setText(num);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFolderClickListener.onClickListener(folder);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list_of_folder.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView, num_of_image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.name_of_folder);
            num_of_image = itemView.findViewById(R.id.num_of_image);
        }
    }
    public interface OnFolderClickListener{
        void onClickListener(FolderofImage folderofImage);
    }
}
