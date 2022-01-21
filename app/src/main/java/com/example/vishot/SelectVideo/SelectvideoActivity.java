package com.example.vishot.SelectVideo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.vishot.FragmentActionListener;
import com.example.vishot.R;

public class SelectvideoActivity extends AppCompatActivity implements FragmentActionListener {
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectvideo2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        ListFolderFragment selectvideoFragment = new ListFolderFragment();
        selectvideoFragment.setFragmentActionListener(this);
        fragmentTransaction.add(R.id.fragment_container,selectvideoFragment);
        fragmentTransaction.commit();
    }
    public void add_fragment(Bundle bundle){
        fragmentTransaction = fragmentManager.beginTransaction();
        ListofVideoFragment videoFragment = new ListofVideoFragment();
        videoFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.fragment_container,videoFragment,"videoFragment");
        fragmentTransaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(fragmentManager.findFragmentByTag("videoFragment")!=null){
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(fragmentManager.findFragmentByTag("videoFragment"));
            fragmentTransaction.commit();
        }else{
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListener(Bundle bundle) {
        add_fragment(bundle);
    }
}