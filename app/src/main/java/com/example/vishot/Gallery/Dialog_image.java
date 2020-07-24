package com.example.vishot.Gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.vishot.R;

public class Dialog_image extends DialogFragment {
    ImageView mainimage, share_image, edit_image, delete_image;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_image,container,false);
        mainimage = (ImageView) view.findViewById(R.id.largerimage);
        share_image = (ImageView) view.findViewById(R.id.share_image);
        edit_image = (ImageView) view.findViewById(R.id.edit_image);
        delete_image = (ImageView) view.findViewById(R.id.delete_image);
        return view;
    }
}
