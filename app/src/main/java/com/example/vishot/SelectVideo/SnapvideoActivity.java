package com.example.vishot.SelectVideo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.vishot.Gallery.Gallery;
import com.example.vishot.Gallery.Image_fragment;
import com.example.vishot.R;
import com.example.vishot.Settings.SettingActivity;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import me.bendik.simplerangeview.SimpleRangeView;
import wseemann.media.FFmpegMediaMetadataRetriever;

public class SnapvideoActivity extends AppCompatActivity {
    CheckedTextView quicksnap, timesnap;
    VideoView videoView;
    SimpleRangeView simpleRangeView;
    RecyclerView rv_frame_cut;
    Button ok;
    TextView time_to_snap, period_to_cut;
    EditText second_in_dialog;
    DividerItemDecoration dividerItemDecoration;
    LinearLayoutManager layoutManager;
    FrameCutAdapter frameCutAdapter;
    ArrayList<Bitmap> list_of_cutimage;
    double second_of_timesnap = 2;
    String format = ".png";
    int quality = 100;
    long video_length;
    long start_to_cut;
    long end_to_cut;
    long real_end_to_cut;
    long i = start_to_cut*1000;
    String end = "";
    String start = "";
    String range = "";
    boolean quick =true;
    float size;
    static boolean format_changed = false;
    static boolean quality_changed = false;
    static boolean size_changed = false;
    SharedPreferences sharedPreferences;
    int endfix;
    FFmpegMediaMetadataRetriever metadataRetriever;
    boolean Doing = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snapvideo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);
        Intent intent = getIntent();
        String video_path = intent.getStringExtra("video_path");
        Uri uri = Uri.parse(video_path);
        list_of_cutimage = new ArrayList<>();
        createVishotfolder();
        create_image_folder(getDate());
        init();
        check_setting();
        videoView.setVideoPath(video_path);
        metadataRetriever.setDataSource(video_path);
        MediaController mediaController = new MediaController(this);
        video_length = Integer.parseInt(metadataRetriever.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_DURATION));
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);
        videoView.start();
        set_up_rV();

        quicksnap.setBackgroundColor(getResources().getColor(R.color.blue_color_picker));
        quicksnap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quick = true;
//                list_of_cutimage.clear();
                frameCutAdapter.notifyDataSetChanged();
                timesnap.setBackgroundColor(getResources().getColor(R.color.white));
                quicksnap.setBackgroundColor(getResources().getColor(R.color.blue_color_picker));
                simpleRangeView.setEnd(1001);
                simpleRangeView.setStart(0);
                time_to_snap.setVisibility(View.INVISIBLE);
                period_to_cut.setVisibility(View.INVISIBLE);
                simpleRangeView.setVisibility(View.INVISIBLE);
                videoView.start();
            }
        });
        timesnap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Doing = false;
                quick = false;
                list_of_cutimage.clear();
                frameCutAdapter.notifyDataSetChanged();
                quicksnap.setBackgroundColor(getResources().getColor(R.color.white));
                timesnap.setBackgroundColor(getResources().getColor(R.color.blue_color_picker));
                time_to_snap.setVisibility(View.VISIBLE);
                period_to_cut.setVisibility(View.VISIBLE);
                simpleRangeView.setVisibility(View.VISIBLE);
                start_to_cut = format_seekbar(simpleRangeView.getStart());
                end_to_cut = format_seekbar(simpleRangeView.getEnd());
                start =format_time(String.valueOf(format_seekbar(simpleRangeView.getStart())));
                end = format_time(String.valueOf(format_seekbar(simpleRangeView.getEnd())));
                endfix = simpleRangeView.getEnd();
                range = start + "-" + end;
                period_to_cut.setText(range);
                videoView.seekTo(0);
                videoView.pause();
                showsecondialog();

            }
        });

        simpleRangeView.setOnTrackRangeListener(new SimpleRangeView.OnTrackRangeListener() {
            @Override
            public void onStartRangeChanged(@NotNull SimpleRangeView simpleRangeView, int i) {
                videoView.seekTo((int) format_seekbar(i));
                start_to_cut = format_seekbar(i);
                start = format_time(String.valueOf(format_seekbar(i)));
                range = start + "-" + end;
                period_to_cut.setText(range);
            }

            @Override
            public void onEndRangeChanged(@NotNull SimpleRangeView simpleRangeView, int i) {
                endfix = i;
                end_to_cut = format_seekbar(i);
                end = format_time(String.valueOf(format_seekbar(i)));
                range = start + "-" + end;
                period_to_cut.setText(range);
            }
        });
        time_to_snap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showsecondialog();
            }
        });
    }

    public long format_seekbar(int progress){
        return progress*(video_length/999);
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
    public void init(){
        time_to_snap = findViewById(R.id.time_to_snap);
        period_to_cut = findViewById(R.id.period_to_cut);
        simpleRangeView = findViewById(R.id.seekbar_for_timesnap);
        rv_frame_cut = findViewById(R.id.rv_frame_cut);
        quicksnap = findViewById(R.id.quicksnap);
        timesnap = findViewById(R.id.timesnap);
        videoView = findViewById(R.id.video_to_cut);
        metadataRetriever = new FFmpegMediaMetadataRetriever();
    }
    public void set_up_rV(){
        layoutManager = new LinearLayoutManager(SnapvideoActivity.this, LinearLayoutManager.HORIZONTAL,false);
        dividerItemDecoration = new DividerItemDecoration(SnapvideoActivity.this,layoutManager.getOrientation());
        frameCutAdapter = new FrameCutAdapter(list_of_cutimage,SnapvideoActivity.this);
        rv_frame_cut.setLayoutManager(layoutManager);
        rv_frame_cut.addItemDecoration(dividerItemDecoration);
        rv_frame_cut.setAdapter(frameCutAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.snap_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.camera){
            if(quick){
                quicksnap();
                item.setTitle("");
                item.setIcon(R.drawable.ic_baseline_camera_alt_24);
            }else {
                time_to_snap.setClickable(false);
                simpleRangeView.setClickable(false);
                if(!Doing){
                    Doing = true;
                    item.setIcon(null);
                    item.setTitle("Stop");
                    real_end_to_cut = end_to_cut;
                    period_to_cut.setClickable(false);
                    time_to_snap.setClickable(false);
                    simpleRangeView.setClickable(false);
                    simpleRangeView.setMovable(false);
                    simpleRangeView.setEndFixed(endfix);
                    quicksnap.setClickable(false);
                    timesnap();
                    quicksnap.setClickable(false);
                }else {
                    Doing = false;
                    item.setTitle("");
                    item.setIcon(R.drawable.ic_baseline_camera_alt_24);
                    start_to_cut += second_of_timesnap*1000;
                    period_to_cut.setClickable(true);
                    time_to_snap.setClickable(true);
                    simpleRangeView.setClickable(true);
                    simpleRangeView.setMovable(true);
                    quicksnap.setClickable(true);

                }
            }
        } else if(item.getItemId()==R.id.done){
            startActivity(new Intent(SnapvideoActivity.this, Gallery.class));
            finish();
        }else {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void createVishotfolder(){
        String path = Environment.getExternalStorageDirectory().toString()+"/Vishot";
        File vishot_folder = new File(path);
        if(!vishot_folder.exists()){
            boolean folder_creation = vishot_folder.mkdirs();
            if(folder_creation){
                Log.i("Creation of Vishot","Successfully");
            }else{
                Log.i("Creation of Vishot","Unsuccessfully");
            }
        }
    }

    public void showsecondialog(){
        final Dialog dialog = new Dialog(SnapvideoActivity.this);
        dialog.setContentView(R.layout.dialog_snap_video_2nd);
        dialog.setTitle(R.string.time_title);
        second_in_dialog = dialog.findViewById(R.id.second_to_cut);
        ok = dialog.findViewById(R.id.ok);
        try {
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(second_in_dialog.getText().length()>0) {
                        second_of_timesnap = Double.parseDouble(second_in_dialog.getText().toString());
                        if (second_of_timesnap > 0 && second_of_timesnap*1000 < video_length) {
                            second_of_timesnap = Double.parseDouble(second_in_dialog.getText().toString());
                            if (second_of_timesnap == 1) {
                                time_to_snap.setText(getResources().getString(R.string.time_to_snap));
                                dialog.cancel();
                            }else{
                                time_to_snap.setText(getResources().getString(R.string.time_to_snap_1) + "" + second_of_timesnap + " " + getResources().getString(R.string.time_to_snap_3));
                                dialog.cancel();
                            }
                        }else {
                            Toast.makeText(SnapvideoActivity.this,getResources().getString(R.string.warning_when_submit),Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(SnapvideoActivity.this,getResources().getString(R.string.warning_when_submit),Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }catch (Exception e){
            Toast.makeText(SnapvideoActivity.this,getResources().getString(R.string.warning_when_submit),Toast.LENGTH_SHORT).show();
        }

        dialog.show();
    }

    public void check_setting(){
        if(format_changed){
            format = SettingActivity.getFormat();
        }else{
            format = SettingActivity.check_format(sharedPreferences.getString("saved_format","PNG"));
        }
        if(quality_changed){
            quality = SettingActivity.getQuality();
        }else {
            quality = SettingActivity.check_quality(sharedPreferences.getString("saved_quality","High"));
        }
        if(size_changed){
            size = SettingActivity.getSize();
        }else {
            size = (float) SettingActivity.check_size(sharedPreferences.getString("saved_size","1x"));
        }
    }

    public void quicksnap(){
        videoView.pause();
        long n = (long) videoView.getCurrentPosition();
        n = n*1000;
        Log.i("currentposition",Long.toString(n));
        Bitmap btmap = metadataRetriever.getFrameAtTime(n,FFmpegMediaMetadataRetriever.OPTION_CLOSEST);
        list_of_cutimage.add(0,btmap);
        save_image(btmap,getDate(),getTime());
        frameCutAdapter.notifyDataSetChanged();
        videoView.start();
    }
    public void timesnap(){
        new SaveImageintimesnap().execute();
    }
    public void create_image_folder(String date){
        String folder_name = Environment.getExternalStorageDirectory().toString() + "/Vishot" + "/" + date;
        File file = new File(folder_name);
        if (!file.exists()) {
            if (file.mkdirs()) {
                Toast.makeText(SnapvideoActivity.this, "Folder name " + date + " created successfully", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void save_image(Bitmap btmap,String date, String time) {
            File image_file = new File(Environment.getExternalStorageDirectory().toString() + "/Vishot" + "/" + date+"/"+time + format);
            try {
                image_file.createNewFile();
                FileOutputStream fos = new FileOutputStream(image_file);
                btmap = resizeBitmap(btmap,size);
                btmap.compress(Bitmap.CompressFormat.PNG, quality, fos);
                fos.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
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

    public static void setFormat_changed(boolean format_changed) {
        SnapvideoActivity.format_changed = format_changed;
    }

    public static void setQuality_changed(boolean quality_changed) {
        SnapvideoActivity.quality_changed = quality_changed;
    }

    public static void setSize_changed(boolean size_changed) {
        SnapvideoActivity.size_changed = size_changed;
    }

    public Bitmap resizeBitmap(Bitmap btmap, float scale){
        int width = btmap.getWidth();
        int height = btmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(scale,scale);
        return Bitmap.createBitmap(btmap,0,0,width,height,matrix,false);

    }
    public class SaveImageintimesnap extends AsyncTask<Void, Long, Void>{


        @Override
        protected Void doInBackground(Void... voids) {

                for (i = start_to_cut * 1000; i <= real_end_to_cut * 1000; i = i + (long) (second_of_timesnap * 1000000)) {
                    if(Doing) {
                        Bitmap mBitmap = metadataRetriever.getFrameAtTime(i,FFmpegMediaMetadataRetriever.OPTION_CLOSEST);
                        if(!Doing){break;}
                        save_image(mBitmap, getDate(), getTime());
                        list_of_cutimage.add(0, mBitmap);
                        publishProgress(i);
                    }else {
                        break;
                    }
                }
            Log.i("ooooo","done");
            return null;
        }
        @Override
        protected void onProgressUpdate(Long... values) {
            super.onProgressUpdate(values);
            start_to_cut = values[0]/1000;
            videoView.seekTo((int) start_to_cut);
            long t = values[0]/1000;
            int k_start = (int) (1001*t/video_length);
            int k_end = (int) (1001*real_end_to_cut/video_length);
            start = format_time(String.valueOf(t));
            end = format_time(String.valueOf(real_end_to_cut));
            range = start + "-" + end;
            period_to_cut.setText(range);
            simpleRangeView.setStart(k_start);
            simpleRangeView.setEnd(k_end);
            Log.i("timeeeeeeee",String.valueOf(values[0]));
            frameCutAdapter.notifyDataSetChanged();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
        }
    }
    @Override
    public void onBackPressed() {
        finish();
    }


}