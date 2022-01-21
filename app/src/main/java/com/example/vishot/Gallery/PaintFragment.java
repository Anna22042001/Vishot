package com.example.vishot.Gallery;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vishot.R;

public class PaintFragment extends Fragment {
    SeekBar seekBar;
    Button color_paint, eraser_paint;
    int stroke_size = 80;
    int color_code;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_paint,container,false);
        seekBar = view.findViewById(R.id.stroke_size);
        color_paint = view.findViewById(R.id.color_paint);
        eraser_paint = view.findViewById(R.id.eraser_paint);
        seekBar.setMax(500);
        seekBar.setProgress(80);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                stroke_size= progress;
                EditImageActivity.paint(stroke_size,color_code);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        color_paint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_Color_dialog();
            }
        });
        eraser_paint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditImageActivity.eraser();
            }
        });
        return view;
    }
    public void add_Color_dialog(){
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.color_dialog);
        RecyclerView rv_Color = dialog.findViewById(R.id.rv_Color);
        rv_Color.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),3);
        ColorPickerAdapter colorPickerAdapter = new ColorPickerAdapter(getContext());
        rv_Color.setLayoutManager(layoutManager);
        rv_Color.setAdapter(colorPickerAdapter);
        colorPickerAdapter.setOnColorPickerClickListener(new ColorPickerAdapter.OnColorPickerClickListener() {
            @Override
            public void onColorPickerClickListener(int colorCode) {
                color_code = colorCode;
                EditImageActivity.paint(stroke_size,color_code);
                dialog.cancel();
            }
        });
        dialog.show();
    }

}
