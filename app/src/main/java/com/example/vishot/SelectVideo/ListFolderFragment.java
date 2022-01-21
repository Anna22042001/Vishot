package com.example.vishot.SelectVideo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vishot.FragmentActionListener;
import com.example.vishot.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

public class ListFolderFragment extends Fragment {
    ArrayList<Foldermodel> list_of_model;
    RecyclerView recyclerView;
    FolderAdapter videoAdapter;
    LinearLayoutManager layoutManager;
    DividerItemDecoration dividerItemDecoration;
    FragmentActionListener fragmentActionListener;
    ProgressBar progressBar;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_selectvideo,container,false);
        list_of_model = new ArrayList<>();
        recyclerView = view.findViewById(R.id.rv_Folder);
        progressBar = view.findViewById(R.id.progress_folder);
        new Doing().execute();
        return view;
    }

    public void setFragmentActionListener(FragmentActionListener fragmentActionListener){
        this.fragmentActionListener = fragmentActionListener;
    }

    public void getAllStuff(File file, String name) {
        File[] list = file.listFiles();
        File mFile = null;
        String directoryName = "";
        for (int i = 0; i < list.length; i++) {
            mFile = new File(file, list[i].getName());
            if (mFile.isDirectory()&&mFile.listFiles()!=null) {
                directoryName = list[i].getName();
                getAllStuff(mFile, directoryName);
            } else {
                if(list[i].getName().toLowerCase(Locale.getDefault()).endsWith(".mp4")&&list[i].length()>0) {
                    list_of_model.add(new Foldermodel(name, file.toString()));
                }
            }
        }
    }

    public void getAllFolder(){
        String root_sd = Environment.getExternalStorageDirectory().toString();
        Log.i("directory",root_sd);
        File file = new File(root_sd);
        list_of_model.clear();
        getAllStuff(file,"");
    }

    public ArrayList<Foldermodel> filterList(ArrayList<Foldermodel> list) {
        Set set = new TreeSet(new Comparator() {
            @Override
            public int compare(Object o, Object t1) {
                if (((Foldermodel) o).getName().equals(((Foldermodel) t1).getName())) {
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
    public class Doing extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
            dividerItemDecoration = new DividerItemDecoration(getActivity(),layoutManager.getOrientation());
        }



        @Override
        protected Void doInBackground(Void... voids) {
            getAllFolder();
            list_of_model = filterList(list_of_model);
            videoAdapter = new FolderAdapter(getActivity(),list_of_model);

            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.INVISIBLE);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.addItemDecoration(dividerItemDecoration);
            recyclerView.setAdapter(videoAdapter);
            videoAdapter.MyFolderclick(new FolderAdapter.OnFolderClickListener() {
                @Override
                public void onClickListener(Foldermodel foldermodel) {
                    if(fragmentActionListener!=null){
                    try {
                        Bundle bundle = new Bundle();
                        bundle.putString("folder_path",foldermodel.getPath());
                        fragmentActionListener.onListener(bundle);
                    }catch (Exception e){
                        Log.i("error",e.getMessage());
                    }
                    }
                }
            });
        }
    }
}
