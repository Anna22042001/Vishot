package com.example.vishot.SelectVideo;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.vishot.R;

public class ListofVideoFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_of_video,container,false);
        Bundle bundle = getArguments();
        String folder_path = bundle.getString("folder_path");
        Log.i("folder_path",folder_path);
        return view;
    }
}
