package com.example.vishot.SelectVideo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.vishot.R;

public class SelectvideoActivity extends AppCompatActivity {
    static FragmentManager fragmentManager;
    static FragmentTransaction fragmentTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectvideo2);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        ListFolderFragment selectvideoFragment = new ListFolderFragment();
        fragmentTransaction.replace(R.id.fragment_container,selectvideoFragment);
        fragmentTransaction.commit();
    }
    public static void replace_fragment(String folder_path){
        fragmentTransaction = fragmentManager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("folder_path",folder_path);
        ListofVideoFragment videoFragment = new ListofVideoFragment();
        videoFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.fragment_container,videoFragment);
        fragmentTransaction.commit();
    }
}