package com.example.vishot.SelectVideo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vishot.R;

import java.util.ArrayList;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder> {
    Context context;
    ArrayList<Foldermodel> list_of_folder;
    private OnFolderClickListener onFolderClickListener;

    public void MyFolderclick(OnFolderClickListener onFolderClickListener){
        this.onFolderClickListener = onFolderClickListener;
    }

    public FolderAdapter(Context context, ArrayList<Foldermodel> list_of_folder) {
        this.context = context;
        this.list_of_folder = list_of_folder;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.view_list_of_folder,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Foldermodel model = list_of_folder.get(position);
        holder.name.setText(model.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFolderClickListener.onClickListener(model);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list_of_folder.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name_of_folder);
        }
    }
    public interface  OnFolderClickListener{
        void onClickListener(Foldermodel foldermodel);
    }
}
