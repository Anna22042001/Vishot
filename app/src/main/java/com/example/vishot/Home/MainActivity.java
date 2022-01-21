package com.example.vishot.Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.FragmentManager;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.vishot.BuildConfig;
import com.example.vishot.Gallery.Gallery;
import com.example.vishot.R;
import com.example.vishot.SelectVideo.SelectvideoActivity;
import com.example.vishot.Settings.SettingActivity;
import com.example.vishot.Slideshow.SlideshowActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    RecyclerView rV_main;
    BottomNavigationView bottomNavigationView;
    String[] PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        rV_main.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this,LinearLayoutManager.VERTICAL,false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(MainActivity.this,layoutManager.getOrientation());
        MainAdapter adapter = new MainAdapter(MainActivity.this);
        rV_main.addItemDecoration(dividerItemDecoration);
        rV_main.setLayoutManager(layoutManager);
        rV_main.setAdapter(adapter);
        adapter.MyBtnClick(new MainAdapter.OnBtnClickListener() {
            @Override
            public void onClickListener(Main_button main_button) {
                switch(main_button.getImage_resource()){
                    case R.drawable.select_video:
                        Intent intent1 = new Intent(MainActivity.this, SelectvideoActivity.class);
                        startActivity(intent1);
                        break;
                    case R.drawable.settings:
                        Intent intent2 = new Intent(MainActivity.this, SettingActivity.class);
                        startActivity(intent2);
                        break;
                    case R.drawable.gallery:
                        Intent intent3 = new Intent(MainActivity.this, Gallery.class);
                        startActivity(intent3);
                        break;
                    case R.drawable.slideshow_maker:
                        Intent intent4 = new Intent(MainActivity.this, SlideshowActivity.class);
                        startActivity(intent4);
                        break;
                }
            }
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        checkPermission();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.share:
                    share();
                    break;
                case R.id.rate_us:
                    rate_us();
                    break;
                case R.id.about_us:
                    about_us();
                    break;
            }
            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);
    }

    public void showDialog(){
        FragmentManager fragmentmanager = getFragmentManager();
        Custom_dialog dialog = new Custom_dialog();
        dialog.show(fragmentmanager,"dialog");
    }
    public void showWebview(){
        Intent intent = new Intent(MainActivity.this, Policies.class);
        startActivity(intent);
    }
    public void init(){
        rV_main = findViewById(R.id.rv_main);
        bottomNavigationView = findViewById(R.id.bottomNavigationV);
    }

    public void share() {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
            String shareMessage = "\nLet me recommend you this application\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, getResources().getString(R.string.share_app)));
        } catch (Exception e) {
            //e.toString();
        }
    }
    public void rate_us(){

        Uri uri = Uri.parse("market://details?id=" +  BuildConfig.APPLICATION_ID );
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID)));
        }
    }

    public void about_us(){
        showDialog();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this,"Permission granted",Toast.LENGTH_SHORT).show();
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("To read video in your device Allow Vishot to access to read your storage.")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();

                            }
                        })
                        .setNegativeButton("Logout", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                MainActivity.this.finish();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }

    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS, 123);
        }
    }
}