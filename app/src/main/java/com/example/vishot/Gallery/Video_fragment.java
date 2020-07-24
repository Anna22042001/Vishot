package com.example.vishot.Gallery;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vishot.R;

import java.util.ArrayList;

public class Video_fragment extends Fragment  {
    RecyclerView recyclerView;

    public Video_fragment(Context contex) {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.video_fragment,container,false);
        initView(view);
        return view;

    }
    public void initView(View view){
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutmanager = new LinearLayoutManager(view.getContext(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutmanager);
        DividerItemDecoration divider = new DividerItemDecoration(view.getContext(),layoutmanager.getOrientation());
        recyclerView.addItemDecoration(divider);
        ArrayList<Video> video_list = new ArrayList<>();
        VideoAdapter adapter =  new VideoAdapter(video_list,view.getContext());
        recyclerView.setAdapter(adapter);




    }
}
