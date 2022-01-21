package com.example.vishot.Gallery;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vishot.R;

public class TextFragment extends Fragment {
    EditText editText;
    Button color;
    String text;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_text_editor,container,false);
        editText = view.findViewById(R.id.text_edit);
        color = view.findViewById(R.id.color);
        color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString().length()!=0){
                    add_Color_dialog();
                    Log.i("result","ok");
                }else{
                    Log.i("result","not ok");
                }
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
                Log.i("colorCode",Integer.toString(colorCode));
                text = editText.getText().toString();
                EditImageActivity.addText(text,colorCode);
                dialog.cancel();
            }
        });
        dialog.show();
    }
}
