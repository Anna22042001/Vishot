package com.example.vishot.Gallery;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.vishot.R;
import com.example.vishot.SelectVideo.SnapvideoActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;

import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;

public class EditImageActivity extends AppCompatActivity {
    static PhotoEditorView photoEditorView;
    static PhotoEditor photoEditor;
    BottomNavigationView bottomNavigationView;
    static String image_path;
    FragmentManager fragmentManager;
    String new_path;
    String fakepath;
    Uri fakeuri;
    FragmentTransaction fragmentTransaction;
    Boolean iscrop = false;
    static Boolean iseditor = false;
    static Boolean save = false;
    String[] PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_image);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fragmentManager = getSupportFragmentManager();
        Intent intent = getIntent();
        iscrop = false;
        iseditor = false;
        fakepath = "file:" + Environment.getExternalStorageDirectory() +"/"+ "1111"+ ".png";
        fakeuri = Uri.parse(fakepath);
        image_path = intent.getStringExtra("image_path");
        init();
        photoEditorView.getSource().setImageURI(Uri.parse(image_path));
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.text:
                        photoEditor.setBrushDrawingMode(false);
                        fragmentTransaction = fragmentManager.beginTransaction();
                        TextFragment textFragment = new TextFragment();
                        fragmentTransaction.replace(R.id.container_linear, textFragment,"1");
                        fragmentTransaction.commit();
                        iseditor = true;
                        break;
                    case R.id.emoji:
                        photoEditor.setBrushDrawingMode(false);
                        fragmentTransaction = fragmentManager.beginTransaction();
                        EmojiFragment emojiFragment = new EmojiFragment();
                        fragmentTransaction.replace(R.id.container_linear, emojiFragment,"1");
                        fragmentTransaction.commit();
                        iseditor = true;
                        break;
                    case R.id.paint:
                        photoEditor.setBrushDrawingMode(true);
                        photoEditor.setBrushSize(80);
                        fragmentTransaction = fragmentManager.beginTransaction();
                        PaintFragment paintFragment = new PaintFragment();
                        fragmentTransaction.replace(R.id.container_linear, paintFragment,"1");
                        fragmentTransaction.commit();
                        iseditor = true;
                        break;
                    case R.id.crop:
                        new_path= "file:"+image_path;
                        startcrop(Uri.parse(new_path));
                        break;
                }
                return false;
            }
        });
    }

    public void init() {
        photoEditorView = findViewById(R.id.photoEditorView);
        bottomNavigationView = findViewById(R.id.bottomNavigationV);
        Typeface mTextRobotoTf = ResourcesCompat.getFont(this, R.font.adamina);
        Typeface mEmojiTypeFace = Typeface.DEFAULT;
        photoEditor = new PhotoEditor.Builder(this, photoEditorView)
                .setPinchTextScalable(true)
                .setDefaultTextTypeface(mTextRobotoTf)
                .setDefaultEmojiTypeface(mEmojiTypeFace)
                .build();
    }

    public static void addText(String input_text, int color_code) {
        photoEditor.addText(input_text, color_code);

    }

    public static void addEmoji(String code) {
        photoEditor.addEmoji(code);
    }

    public static void paint(int brush_size, int color_code) {
        photoEditor.setBrushSize((float) brush_size);
        photoEditor.setBrushColor(color_code);
    }

    public static void eraser() {
        photoEditor.setBrushEraserSize(131);
        photoEditor.brushEraser();
    }

    public void create_image_folder(String date){
        String folder_name = Environment.getExternalStorageDirectory().toString() + "/Vishot" + "/" + date;
        File file = new File(folder_name);
        if (!file.exists()) {
            if (file.mkdirs()) {
                Toast.makeText(EditImageActivity.this, "Folder name " + date + " created successfully", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_button, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        create_image_folder(getDate());
        if (item.getItemId() == R.id.save) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(EditImageActivity.this, PERMISSIONS, 123);
                return false;
            }

            String path = Environment.getExternalStorageDirectory()+"/"+"Vishot"+"/"+getDate()+"/"+getTime()+".png";

            photoEditor.saveAsFile(path, new PhotoEditor.OnSaveListener() {
                @Override
                public void onSuccess(@NonNull String imagePath) {
                    File file = new File(image_path);
                    file.delete();
                    save = true;
                    finish();
                }

                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(EditImageActivity.this,exception.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }else {
            if(iseditor){
                for(Fragment fragment : fragmentManager.getFragments()){
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.remove(fragment);
                    fragmentTransaction.commit();
                }
                if (iscrop){
                    photoEditorView.getSource().setImageURI(Uri.parse(image_path));
                    photoEditor.clearAllViews();
                    iscrop = false;
                    iseditor = false;

                }else {
                    photoEditor.clearAllViews();
                    iscrop = false;
                    iseditor = false;
                }
            }else {
                if (iscrop){
                    photoEditorView.getSource().setImageURI(Uri.parse(image_path));
                    iscrop = false;
                    iseditor = false;
                }else {
                    finish();
                }
            }        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(EditImageActivity.this,"Permission granted",Toast.LENGTH_SHORT).show();
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
                                EditImageActivity.this.finish();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }
    private void startcrop(@NonNull Uri uri){
        UCrop uCrop = UCrop.of(uri,fakeuri);
        uCrop.withMaxResultSize(2000,2000);
        uCrop.withOptions(getCropOption());
        uCrop.start(EditImageActivity.this);
        iscrop = true;


    }
    private UCrop.Options getCropOption() {
        UCrop.Options options = new UCrop.Options();

        options.setCompressionQuality(100);
        options.setCompressionFormat(Bitmap.CompressFormat.PNG);
        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(false);
        options.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        options.setToolbarColor(getResources().getColor(R.color.colorPrimary));
        options.setToolbarTitle("idk");
        return options;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==123&&resultCode==RESULT_OK){
            Uri uri = data.getData();
            if(uri!=null){
                startcrop(uri);
            }
        }else if(requestCode==UCrop.REQUEST_CROP&&resultCode==RESULT_OK){
            photoEditorView.getSource().setImageURI(data.getData());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!iscrop) {
            photoEditorView.getSource().setImageURI(Uri.parse(image_path));
        }else {
            photoEditorView.getSource().setImageURI(Uri.parse(fakepath));

        }
    }

    @Override
    public void onBackPressed() {
        if(iseditor){
            for(Fragment fragment : fragmentManager.getFragments()){
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.remove(fragment);
                fragmentTransaction.commit();
            }
            if (iscrop){
                photoEditorView.getSource().setImageURI(Uri.parse(image_path));
                photoEditor.clearAllViews();
                iscrop = false;
                iseditor = false;

            }else {
                photoEditor.clearAllViews();
                iscrop = false;
                iseditor = false;
            }
        }else {
            if (iscrop){
                photoEditorView.getSource().setImageURI(Uri.parse(image_path));
                iscrop = false;
                iseditor = false;
            }else {
                finish();
            }
        }
    }
    public String getDate(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return simpleDateFormat.format(Calendar.getInstance().getTime());
    }
    public String getTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        return  simpleDateFormat.format(Calendar.getInstance().getTime());
    }
}