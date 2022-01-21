package com.example.vishot.Gallery;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vishot.FragmentActionListener;
import com.example.vishot.R;
import com.example.vishot.SelectVideo.SnapvideoActivity;
import com.example.vishot.Slideshow.ImagechosenAdapter;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

import wseemann.media.FFmpegMediaMetadataRetriever;

public class Image_fragment extends Fragment {
    static RecyclerView rv_Image;
    static ImageAdapter imageAdapter;
    DividerItemDecoration dividerItemDecoration;
    static Toolbar toolbar;
    TextView num_of_images;
    static GridLayoutManager gridLayoutManager;
    static ArrayList<Image> list_of_image;
    ArrayList<Image> list;
    FragmentActionListener fragmentActionListener;
    DeleteImageAdapter deleteImageAdapter;
    ProgressBar progressBar;
    public Image_fragment(Context context) {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.image_fragment,container,false);
        rv_Image = view.findViewById(R.id.rv_Image);
        progressBar = view.findViewById(R.id.progress_image);
        toolbar = view.findViewById(R.id.toolbar_gallery);
        num_of_images = view.findViewById(R.id.num_of_image);
        list = DeleteImageAdapter.num_of_chosen;
        new Doing1().execute();

        return view;
    }
    public void setFragmentActionListener(FragmentActionListener fragmentActionListener){
        this.fragmentActionListener = fragmentActionListener;
    }
    public static void getImageinVishot(String path, String name) {
        File folder = new File(path);
        File list[] = folder.listFiles();
        File mFile = null;
        String directoryname = "";
        assert list != null;
        for (File image_file : list) {
            mFile = new File(folder, image_file.getName());
            if (mFile.isDirectory() && mFile.listFiles() != null) {
                directoryname = image_file.getName();
                getImageinVishot(mFile.toString(), directoryname);
            } else {
                if (image_file.getName().toLowerCase(Locale.getDefault()).endsWith(".png") || image_file.getName().toLowerCase(Locale.getDefault()).endsWith(".jpg")) {
                    list_of_image.add(new Image("123", image_file.getAbsolutePath()));
                }
            }
        }
    }


    public void delete_list_of_image(){
        ArrayList<Image> list = DeleteImageAdapter.num_of_chosen;
        for(int i = list.size()-1; i>=0;i--){
            File file = new File(list.get(i).getPath());
            file.delete();
            list_of_image.remove(list.get(i));
            list.remove(list.get(i));
        }
        deleteImageAdapter.notifyDataSetChanged();
        imageAdapter.notifyDataSetChanged();
    }

    public void reload_list(){
        ArrayList<Image> list = DeleteImageAdapter.num_of_chosen;
        for(int i = list.size()-1; i>=0;i--){
            list.remove(list.get(i));
        }
        deleteImageAdapter.notifyDataSetChanged();
        imageAdapter.notifyDataSetChanged();
    }

    public void share_list(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Here are some files.");
        intent.setType("image/*");

        ArrayList<Image> list = DeleteImageAdapter.num_of_chosen;
        ArrayList<Uri> list_uri = new ArrayList<>();
        for(int i = list.size()-1; i>=0;i--){
            Uri uri = FileProvider.getUriForFile(getContext(),getContext().getPackageName()+".fileprovider",new File(list.get(i).getPath()));
            list_uri.add(uri);
        }
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM,list_uri);
        startActivity(intent);


    }
    public class Doing1 extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            list_of_image = new ArrayList<>();
            list_of_image.clear();
            rv_Image.setHasFixedSize(true);
            gridLayoutManager = new GridLayoutManager(getContext(),3);
            dividerItemDecoration = new DividerItemDecoration(getContext(),gridLayoutManager.getOrientation());
            rv_Image.setLayoutManager(gridLayoutManager);
            rv_Image.addItemDecoration(dividerItemDecoration);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            getImageinVishot(Environment.getExternalStorageDirectory()+"/Vishot","");
            filter_list();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.INVISIBLE);
            imageAdapter = new ImageAdapter(getContext(),list_of_image);
            rv_Image.setAdapter(imageAdapter);
            imageAdapter.MyImageClick(new ImageAdapter.OnImageclicklistener() {
                @Override
                public void onclicklistener(Image image) {
                    Bundle bundle = new Bundle();
                    bundle.putString("image_path",image.getPath());
                    bundle.putInt("image_index",list_of_image.indexOf(image));
                    fragmentActionListener.onListener(bundle);
                }
            });
            imageAdapter.MyImageLongClick(new ImageAdapter.OnImagelongclicklistener() {
                @Override
                public void onlongclicklistener(Image image) {
                    Gallery.longclick = true;
                    progressBar.setVisibility(View.VISIBLE);

                    new Doing3().execute();
                }
            });

        }
    }
    public static void filter_list(){
       Collections.sort(list_of_image, new Comparator<Image>() {
           @Override
           public int compare(Image o1, Image o2) {
               File file1 = new File(o1.getPath());
               File file2 = new File(o2.getPath());
               long k = file1.lastModified() - file2.lastModified();
               if(k > 0){
                   return -1;
               }else if(k == 0){
                   return 0;
               }else{
                   return 1;
               } }
       });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        if(EditImageActivity.save) {
            rv_Image.smoothScrollToPosition(0);
            rv_Image.scrollToPosition(0);
        }
    }
    public class Doing3 extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            rv_Image.setClickable(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            getImageinVishot(Environment.getExternalStorageDirectory()+"/Vishot","");
            filter_list();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.INVISIBLE);
            toolbar.setVisibility(View.VISIBLE);
            num_of_images.setVisibility(View.VISIBLE);
            num_of_images.setText("Chọn 0 ảnh");
            deleteImageAdapter = new DeleteImageAdapter(getContext(),list_of_image);
            rv_Image.setAdapter(deleteImageAdapter);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Log.i("delete list","success");
                    switch (item.getItemId()){
                        case R.id.delete_list:
                            delete_list_of_image();
                            break;
                        case R.id.reload:
                            reload_list();
                            break;
                        case R.id.share_list:
                            share_list();
                            break;

                    }
                    num_of_images.setText("Chọn "+ list.size() + " ảnh");
                    return false;
                }
            });
            deleteImageAdapter.DeleteImage(new ImagechosenAdapter.OnImageDeleteListener() {
                @Override
                public void onimagedelete(Image image) {
                    num_of_images.setText("Chọn "+ list.size() + " ảnh");
                }
            });
        }
    }

}
