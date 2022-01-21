package com.example.vishot.Gallery;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.vishot.Home.MainActivity;
import com.example.vishot.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;

import ja.burhanrashid52.photoeditor.PhotoEditorView;

public class ImageDisplayFragment extends Fragment {
    static PhotoEditorView displayimage;
    BottomNavigationView bottomNavigationView;
    String image_path = "";
    int index = 0;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_display,container,false);
        setHasOptionsMenu(true);
        displayimage = view.findViewById(R.id.displayimage);
        Bundle bundle = getArguments();
        index=bundle.getInt("image_index");
        image_path =  bundle.getString("image_path");
        bottomNavigationView = view.findViewById(R.id.bottomNavigationV);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.delete:
                    delete_image(image_path,index);
                    break;
                    case R.id.share:
                    share_image(image_path);
                    break;
                    case R.id.edit:
                    edit_image(image_path);
                    break;
                }
                return false;
            }
        });
        if(image_path!=null){
        displayimage.getSource().setImageURI(Uri.parse(image_path));}
        return view;
    }

    public void share_image(String path){
        Uri uri = FileProvider.getUriForFile(getContext(),getContext().getPackageName()+".fileprovider",new File(path));
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM,uri);
        startActivity(Intent.createChooser(intent,"Share image"));
    }
    public void delete_image(String path, final int index_of_image){
        File file = new File(path);
        boolean delete = file.delete();
        if(delete){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage(getResources().getString(R.string.areyousure))
                    .setCancelable(false)
                    .setPositiveButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();

                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                            Image_fragment.imageAdapter.remove_item(index_of_image);
                            FragmentManager fragmentManager = getParentFragmentManager();
                            fragmentManager.popBackStack();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

        }
    }

    public void edit_image(String path){
        Intent intent = new Intent(getContext(),EditImageActivity.class);
        intent.putExtra("image_path",path);
        startActivity(intent);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.info_image,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.info){
            File file = new File(image_path);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(image_path,options);
            int width = options.outWidth;
            int height = options.outHeight;
            String resolution = Integer.toString(width)+"*"+Integer.toString(height);
            String size = Long.toString(file.length()/1000);
            String message = getString(R.string.location)+" " + image_path + "\n\n" + getString(R.string.resolution) +" "+ resolution +"\n\n" + getString(R.string.size)+" " + size + "KB";
            Log.i("message",message);
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage(message)
                    .setCancelable(true)
                    .setTitle(getString(R.string.imageinfo));

            AlertDialog alert = builder.create();
            alert.show();
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        displayimage.getSource().setImageURI(Uri.parse(image_path));
    }
}
