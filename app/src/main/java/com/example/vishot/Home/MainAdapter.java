package com.example.vishot.Home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vishot.R;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    Context context;
    ArrayList<Main_button> list_in_main1;
    private OnBtnClickListener onBtnClickListener;

    public void MyBtnClick(OnBtnClickListener onBtnClickListener){
        this.onBtnClickListener = onBtnClickListener;
    }

    public MainAdapter(Context context, ArrayList<Main_button> list_in_main) {
        this.context = context;
        list_in_main1 = list_in_main;
    }

    public MainAdapter(Context context) {
        this(context, add_list(context));
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.view_of_main_recycler,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Main_button btn = list_in_main1.get(position);
        holder.text.setText(btn.getText_resource());
        holder.image.setImageResource(btn.getImage_resource());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBtnClickListener.onClickListener(btn);
            }
        });
    }


    @Override
    public int getItemCount() {
        return list_in_main1.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView text;
        public ViewHolder(View itemview) {
            super(itemview);
            image = itemview.findViewById(R.id.main_image);
            text = itemview.findViewById(R.id.text_info);
        }
    }
    public static ArrayList<Main_button> add_list(Context context){
        ArrayList<Main_button> list = new ArrayList<>();
        list.add(new Main_button(R.drawable.select_video,R.string.select_video));
        list.add(new Main_button(R.drawable.gallery,R.string.gallery));
        list.add(new Main_button(R.drawable.slideshow_maker,R.string.slideshow_maker));
        list.add(new Main_button(R.drawable.settings,R.string.settings));
        return list;
    }
    public interface OnBtnClickListener{
        void onClickListener(Main_button main_button);
    }
}
