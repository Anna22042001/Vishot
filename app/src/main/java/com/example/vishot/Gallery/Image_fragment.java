package com.example.vishot.Gallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.vishot.R;

import java.util.ArrayList;

public class Image_fragment extends Fragment {
    GridView grid;
    ArrayList<Image> list = new ArrayList<>();
    public Image_fragment(Context context) {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.image_fragment,container,false);
        grid = (GridView) view.findViewById(R.id.grid);
        ImageAdapter adapter = new ImageAdapter(view.getContext(),R.layout.item_grid,list);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bitmap image_bitmap = list.get(position).getImage();
                ((Gallery) getActivity()).showdialog(image_bitmap);
            }
        });
        return view;

    }
}
