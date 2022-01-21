package com.example.vishot.Gallery;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.vishot.R;
import com.yalantis.ucrop.UCrop;

public class CropFragment extends Fragment {
    ImageView imageView;
    String image_path ;

    Uri image_uri;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_ucrop,container,false);
        imageView = view.findViewById(R.id.image_to_cut);
        Bundle bundle = getArguments();
        image_path = "file:" + bundle.getString("image_path");
        image_uri = Uri.parse(image_path);
        startcrop(image_uri);
        return view;
    }
    private void startcrop(@NonNull Uri uri){
        UCrop uCrop = UCrop.of(uri,uri);
        uCrop.withMaxResultSize(2000,2000);
        uCrop.withOptions(getCropOption());
        uCrop.start(getActivity());
    }
    private UCrop.Options getCropOption() {
        UCrop.Options options = new UCrop.Options();

        options.setCompressionQuality(100);
        options.setCompressionFormat(Bitmap.CompressFormat.PNG);
        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(true);
        options.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        options.setToolbarColor(getResources().getColor(R.color.colorPrimary));
        options.setToolbarTitle("idk");
        return options;
    }
}
