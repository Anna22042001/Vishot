package com.example.vishot.Gallery;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vishot.BuildConfig;
import com.example.vishot.R;
import com.example.vishot.SelectVideo.MyVideo;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Video_fragment extends Fragment  {
    RecyclerView recyclerView;
    ArrayList<Video> list_of_video;
    ArrayList<String> list_of_tool;
    MediaMetadataRetriever mediaMetadataRetriever;
    ListView list_of_dialog;
    File video_file;
    Video mvideo;
    String video_path = "";
    String video_name = "";
    String video_size = "";
    String video_date = "";
    String editext_input = "";
    VideoAdapter videoAdapter;
    ProgressBar progressBar;
    public Video_fragment(Context context) {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_gallery,container,false);

        list_of_video = new ArrayList<>();
        list_of_tool = new ArrayList<>();
        mediaMetadataRetriever = new MediaMetadataRetriever();
        initView(view);
        new Doing().execute();

        return view;
    }
    public void initView(View view){
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        progressBar = view.findViewById(R.id.progress_idk);
        progressBar.setVisibility(View.INVISIBLE);
        LinearLayoutManager layoutmanager = new LinearLayoutManager(view.getContext(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutmanager);
        DividerItemDecoration divider = new DividerItemDecoration(view.getContext(),layoutmanager.getOrientation());
        recyclerView.addItemDecoration(divider);

    }
    public void getVideoinFolder(String path, String name) {
        File folder = new File(path);
        list_of_video.clear();
        File list[] = folder.listFiles();
        File mFile = null;
        String directoryname = "";
        assert list != null;
        for (File video_file : list) {
            mFile = new File(folder, video_file.getName());
            if (mFile.isDirectory() && mFile.listFiles() != null) {
                directoryname = video_file.getName();
                getVideoinFolder(mFile.toString(), directoryname);
            } else {
                if (video_file.getName().toLowerCase(Locale.getDefault()).endsWith(".mp4")) {
                    String video_name = video_file.getName().substring(0, video_file.getName().length() - 4);
                    mediaMetadataRetriever.setDataSource(video_file.getAbsolutePath());
                    String time = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                    String duration = format_time(time);
                    String capacity = Long.toString(video_file.length()/1000000) + "MB";
                    Date date = new Date(video_file.lastModified());
                    String in_date = new SimpleDateFormat("dd-MM-yyyy").format(date);
                    String in_time = new SimpleDateFormat("HH:mm:ss").format(date);
                    String time_and_date = in_time + ", " + in_date;
                    Video video = new Video(video_file.getAbsolutePath(),video_name,capacity,duration,time_and_date);
                    Log.i("video",video.getVideo_name() +";"+video.getCapacity()+";"+video.getDate() + ";" + video.getTime_limit() + video.getVideo_path());
                    list_of_video.add(video);
                }
            }
        }
    }
    public class Doing extends AsyncTask<Void, Void, Void>{


        @Override
        protected Void doInBackground(Void... voids) {
            getVideoinFolder(Environment.getExternalStorageDirectory().toString()+"/Vishot_vid","");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            videoAdapter = new VideoAdapter(list_of_video,getContext());
            recyclerView.setAdapter(videoAdapter);
            videoAdapter.MyVideoClick(new VideoAdapter.OnVideoClickListener() {
                @Override
                public void onVideoClick(Video video) {
                    create_dialog();
                    mvideo = video;
                    video_path = video.getVideo_path();
                    video_file = new File(video_path);
                    video_name = video_file.getName().substring(0, video_file.getName().length() - 4);
                    video_size = Long.toString(video_file.length()/1000000) + "MB";
                    Date date = new Date(video_file.lastModified());
                    String in_date = new SimpleDateFormat("dd-MM-yyyy").format(date);
                    String in_time = new SimpleDateFormat("HH:mm:ss").format(date);
                    String time_and_date = in_time + ", " + in_date;
                    video_date = time_and_date;
                }
            });

        }
    }
    public String format_time(String time_in_millisecond){
        long time = Long.parseLong(time_in_millisecond);
        long seconds = time / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        String hours_in_string = Long.toString(hours%24);
        String minute_in_string = Long.toString(minutes%60);
        String second_in_string = Long.toString(seconds%60);
        if(hours%24<10){
            hours_in_string = "0" + hours%24;
        }
        if(minutes%60<10){
            minute_in_string = "0"+ minutes%60;
        }
        if(seconds%60<10){
            second_in_string = "0"+seconds%60;
        }
        return hours_in_string + ":" + minute_in_string + ":" + second_in_string;
    }
    public void create_dialog(){
        list_of_dialog = new ListView(getContext());
        list_of_tool.clear();
        list_of_tool.add(getResources().getString(R.string.open));
        list_of_tool.add(getResources().getString(R.string.rename));
        list_of_tool.add(getResources().getString(R.string.delete));
        list_of_tool.add(getResources().getString(R.string.share));
        list_of_tool.add(getResources().getString(R.string.detail));
        list_of_tool.add("");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,list_of_tool);
        list_of_dialog.setAdapter(adapter);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(list_of_dialog);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog dialog_tool = builder.create();
        dialog_tool.show();
        list_of_dialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        FragmentManager fragmentManager = getParentFragmentManager();
                        Bundle bundle = new Bundle();
                        bundle.putString("video_path",video_path);
                        VideoDisplayFragment displayFragment = new VideoDisplayFragment();
                        displayFragment.setArguments(bundle);
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.gallery_container,displayFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        dialog_tool.cancel();
                        break;
                    case 1:
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(getContext());
                        final EditText input = new EditText(getContext());
                        builder2.setTitle(getResources().getString(R.string.rename));
                        input.setInputType(InputType.TYPE_CLASS_TEXT);
                        builder2.setView(input);
                        builder2.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try{
                                    editext_input = input.getText().toString();
                                    if(editext_input.length()>0){
                                    new Doing3().execute();
                                    dialog.cancel();
                                    dialog_tool.cancel();}
                                    else {
                                        Log.i("warnning","wwwww");
                                    }
                                }
                                catch (Exception e){
                                    Log.i("errorrrr",e.getMessage());

                                }

                            }
                        });
                        builder2.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        AlertDialog inputdialog = builder2.create();
                        inputdialog.show();

                       dialog_tool.cancel();
                        break;
                    case 2:
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                        builder1.setMessage(getResources().getString(R.string.areyousure))
                                .setCancelable(false)
                                .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        video_file.delete();
                                        list_of_video.remove(mvideo);
                                        videoAdapter.notifyDataSetChanged();

                                    }
                                })
                                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();

                                    }
                                });
                        AlertDialog alert = builder1.create();
                        alert.show();
                        dialog_tool.cancel();
                        break;
                    case 3:
                        try {
                            share();
                        }catch (Exception e){
                            Log.i("erorrrrr",e.getMessage());
                        }
                        break;
                    case 4:
                        String message = getString(R.string.filename)+ "\n" + video_name + "\n\n" + getString(R.string.size)+"\n" + video_size  +"\n\n"+ getString(R.string.date)+"\n" +video_date + "\n\n"+getString(R.string.path)+"\n"+video_path;
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage(message)
                                .setCancelable(true)
                                .setTitle(getString(R.string.imageinfo));

                        AlertDialog alert1 = builder.create();
                        alert1.show();
                        dialog_tool.cancel();
                        break;
                }
            }
        });
    }
    public void create_dialog_input(){

    }

    public void share(){
//        Uri uri = Uri.parse(video_path);
        Uri uri = FileProvider.getUriForFile(getContext(), getContext().getPackageName()+".fileprovider",new File(video_path));
        Log.i("uriiiiii", video_path);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_SUBJECT,video_name);
        intent.putExtra(Intent.EXTRA_STREAM,uri);
        intent.setType("video/*");
        startActivity(Intent.createChooser(intent,"share video"));
    }

    public void rename(){

    }
    public class Doing3 extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            File new_video_file = new File(video_file.getParentFile(),  editext_input+".mp4");
            video_file.renameTo(new_video_file);
            getVideoinFolder(Environment.getExternalStorageDirectory().toString()+"/Vishot_vid","");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            videoAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}
