package com.example.vishot.Gallery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.vishot.FragmentActionListener;
import com.example.vishot.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class Gallery extends AppCompatActivity implements FragmentActionListener {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public static ArrayList<Image> list_of_image;
    static FragmentManager fragmentManager;
    static FragmentTransaction fragmentTransaction;
    static ImageDisplayFragment displayFragment;
    static boolean longclick = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        fragmentManager = getSupportFragmentManager();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        setupViewPager();
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(fragmentManager.getBackStackEntryCount()>0&&item.getItemId()!=R.id.info){
            Log.i("1","1");
           fragmentManager.popBackStack();
        }else if(item.getItemId()!=R.id.info&&!longclick){
            finish();
        }else if(item.getItemId()!=R.id.info&&longclick){
            Image_fragment.rv_Image.setAdapter(Image_fragment.imageAdapter);
            Image_fragment.toolbar.setVisibility(View.INVISIBLE);
            longclick = false;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    private void setupViewPager() {
        ViewPagerAdapter_gallery adapter = new ViewPagerAdapter_gallery(getSupportFragmentManager());
        Image_fragment image_fragment = new Image_fragment(this);
        Video_fragment video_fragment = new Video_fragment(this);
        image_fragment.setFragmentActionListener(this);
        adapter.addFragment(image_fragment, "Images");
        adapter.addFragment(video_fragment, "Videos");
        viewPager.setAdapter(adapter);
    }



    public static void replaceFragment(Bundle bundle){
        fragmentTransaction = fragmentManager.beginTransaction();
        displayFragment = new ImageDisplayFragment();
        displayFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.gallery_container,displayFragment,"display_fragment");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    public static void remove_fragment(){
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(fragmentManager.findFragmentByTag("display_fragment"));
        fragmentTransaction.commit();
    }

    @Override
    public void onListener(Bundle bundle) {
        replaceFragment(bundle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(EditImageActivity.save){
        getSupportFragmentManager().popBackStack();
        setupViewPager();
        tabLayout.setupWithViewPager(viewPager);}
    }

    @Override
    public void onBackPressed() {
        if(!longclick){
            if(getSupportFragmentManager().getBackStackEntryCount()>0){
                getSupportFragmentManager().popBackStack();
            }else {
                finish();
            }
        }else{
            Image_fragment.rv_Image.setAdapter(Image_fragment.imageAdapter);
            Image_fragment.toolbar.setVisibility(View.INVISIBLE);
            longclick = false;
        }
    }

}