package com.example.vishot.Slideshow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.vishot.Gallery.Image;
import com.example.vishot.R;
import com.example.vishot.SelectVideo.MyVideo;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

public class SlideshowActivity extends AppCompatActivity {
    RecyclerView rv_folder_of_image, rv_image_to_choose, rv_image_chosen;
    ArrayList<FolderofImage> list_of_folder;
    ArrayList<Image> list_of_image_to_choose;
    ArrayList<Image> list_of_chosen_image;
    String folder_path;
    FolderofImageAdapter folderofImageAdapter;
    ImagetochooseAdapter adapter;
    ImagechosenAdapter imagechosenAdapter;
    Image image_to_choose;

    Toolbar clear, create_video;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slideshow);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        init();
        set_up_folder_list();
        set_up_image_chosen_list();
        set_up_image_to_choose_list();
        imagechosenAdapter = new ImagechosenAdapter(SlideshowActivity.this,list_of_chosen_image);
        rv_image_chosen.setAdapter(imagechosenAdapter);
        imagechosenAdapter.DeleteImage(new ImagechosenAdapter.OnImageDeleteListener() {
            @Override
            public void onimagedelete(Image image) {
                list_of_chosen_image.remove(image);
                imagechosenAdapter.notifyDataSetChanged();
            }
        });
        new Doing1().execute();

    }
    private void init(){
        clear = findViewById(R.id.clear_button);
        clear.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                list_of_chosen_image.clear();
                imagechosenAdapter.notifyDataSetChanged();
                return false;
            }
        });

        rv_folder_of_image = findViewById(R.id.rv_Folderimage);
        rv_image_to_choose = findViewById(R.id.rv_image_tochoose);
        rv_image_chosen = findViewById(R.id.rV_image_chosen);
        list_of_folder = new ArrayList<>();
        list_of_chosen_image = new ArrayList<>();
        list_of_image_to_choose = new ArrayList<>();
    }
    private void set_up_folder_list(){
        rv_folder_of_image.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,linearLayoutManager.getOrientation());
        rv_folder_of_image.setLayoutManager(linearLayoutManager);
        rv_folder_of_image.addItemDecoration(dividerItemDecoration);

    }

    private void set_up_image_to_choose_list(){
        rv_image_to_choose.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(SlideshowActivity.this,2,GridLayoutManager.HORIZONTAL,false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(SlideshowActivity.this,gridLayoutManager.getOrientation());
        rv_image_to_choose.setLayoutManager(gridLayoutManager);
        rv_image_to_choose.addItemDecoration(dividerItemDecoration);
    }

    private void set_up_image_chosen_list(){
        rv_image_chosen.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SlideshowActivity.this,LinearLayoutManager.HORIZONTAL,false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(SlideshowActivity.this,linearLayoutManager.getOrientation());
        rv_image_chosen.setLayoutManager(linearLayoutManager);
        rv_image_chosen.addItemDecoration(dividerItemDecoration);
    }

    public class Doing1 extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            getAllFolder();
            list_of_folder = filterList(list_of_folder);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            folderofImageAdapter = new FolderofImageAdapter(list_of_folder,SlideshowActivity.this);
            rv_folder_of_image.setAdapter(folderofImageAdapter);
            folderofImageAdapter.MyFolderClick(new FolderofImageAdapter.OnFolderClickListener() {
                @Override
                public void onClickListener(FolderofImage folderofImage) {
                    folder_path = folderofImage.getPath();
                    new Doing2().execute();
                }
            });
            super.onPostExecute(aVoid);
        }
    }

    public class Doing2 extends AsyncTask<Void, Void, Void>{


        @Override
        protected Void doInBackground(Void... voids) {
            getImageinFolder(folder_path,"");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter = new ImagetochooseAdapter(SlideshowActivity.this,list_of_image_to_choose);
            rv_image_to_choose.setAdapter(adapter);
            adapter.MyImageChoose(new ImagetochooseAdapter.OnImageChooseListener() {
                @Override
                public void onchooselistener(Image image) {
                    clear.setVisibility(View.VISIBLE);
                    list_of_chosen_image.add(new Image("",image.getPath()));
                    imagechosenAdapter.notifyDataSetChanged();
                }
            });
        }
    }
    

    public void getAllStuff(File file, String name) {
        int num = 0;
        File[] list = file.listFiles();
        File mFile = null;
        String directoryName = "";
        for (int i = 0; i < list.length; i++) {
            mFile = new File(file, list[i].getName());
            if (mFile.isDirectory()&&mFile.listFiles()!=null) {
                directoryName = list[i].getName();
                getAllStuff(mFile, directoryName);
            } else {
                if(list[i].getName().toLowerCase(Locale.getDefault()).endsWith(".png")||list[i].getName().toLowerCase(Locale.getDefault()).endsWith(".jpg")&&!file.toString().equals("/storage/emulated/0")) {
                    num = list.length;
                    list_of_folder.add(new FolderofImage(name,num,file.toString()));
                }
            }
        }
    }

    public void getAllFolder(){
        String root_sd = Environment.getExternalStorageDirectory().toString();
        Log.i("directory",root_sd);
        File file = new File(root_sd);
        list_of_folder.clear();
        getAllStuff(file,"");
    }
    public ArrayList<FolderofImage> filterList(ArrayList<FolderofImage> list) {
        Set set = new TreeSet(new Comparator() {
            @Override
            public int compare(Object o, Object t1) {
                if (((FolderofImage) o).getName().equals(((FolderofImage) t1).getName())) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });
        set.addAll(list);
        list.clear();
        list = new ArrayList<>(set);
        return list;
    }

    public void getImageinFolder(String path, String name){
        File folder = new File(path);
        list_of_image_to_choose.clear();
        File list[] = folder.listFiles();
        File mFile = null;
        String directoryname = "";
        assert list != null;
        for(File image_file : list){
            mFile = new File(folder,image_file.getName());
            if(mFile.isDirectory()&&mFile.listFiles()!=null){
                directoryname = image_file.getName();
                getImageinFolder(mFile.toString(),directoryname);
            }else{
                if(image_file.getName().toLowerCase(Locale.getDefault()).endsWith(".png")||image_file.getName().toLowerCase(Locale.getDefault()).endsWith(".jpg")) {
                    list_of_image_to_choose.add(new Image("",image_file.getAbsolutePath()));
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_video,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()!=R.id.create&&item.getItemId()!=R.id.save){
            finish();
        }else{


        }
        return super.onOptionsItemSelected(item);
    }
    public static void remove_Fragment(){


    }
}