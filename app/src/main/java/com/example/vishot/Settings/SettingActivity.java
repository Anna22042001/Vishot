package com.example.vishot.Settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.widget.ListView;

import com.example.vishot.R;
import com.example.vishot.SelectVideo.SnapvideoActivity;

public class SettingActivity extends PreferenceActivity {
    SharedPreferences sharedPreferences;
    String saved_size;
    String saved_quality;
    String saved_format;
    static float size;
    static int quality;
    static String format;

    public static float getSize() {
        return size;
    }

    public static int getQuality() {
        return quality;
    }

    public static String getFormat() {
        return format;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.listpreference);
        sharedPreferences = getSharedPreferences("settings",MODE_PRIVATE);
        saved_format = sharedPreferences.getString("saved_format","PNG");
        saved_quality = sharedPreferences.getString("saved_quality","High");
        saved_size = sharedPreferences.getString("saved_size","1x");
        final ListPreference format_list = (ListPreference) findPreference("File Format");
        format_list.setSummary(saved_format);
        final ListPreference quality_list = (ListPreference) findPreference("Quality");
        quality_list.setSummary(saved_quality);
        final ListPreference size_list = (ListPreference) findPreference("Size");
        size_list.setSummary(saved_size);
        format_list.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String item = (String) newValue;
                preference.setSummary(item);
                format_list.setValue(item);
                sharedPreferences.edit().putString("saved_format",item).apply();
                SnapvideoActivity.setFormat_changed(true);
                format = check_format(item);
                return false;
            }
        });
        quality_list.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String items = (String) newValue;
                preference.setSummary(items);
                quality_list.setValue(items);
                sharedPreferences.edit().putString("saved_quality",items).apply();
                SnapvideoActivity.setQuality_changed(true);
                quality = check_quality(items);
                return false;
            }
        });
        size_list.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String items = (String) newValue;
                preference.setSummary(items);
                size_list.setValue(items);
                sharedPreferences.edit().putString("saved_size",items).apply();
                SnapvideoActivity.setSize_changed(true);
                size = (float) check_size(items);
                return false;
            }
        });
    }
    public static String check_format(String input_format){
        if(input_format.equals("PNG")){
            return ".png";
        }else{
            return ".jpg";
        }
    }
    public static int check_quality(String input_quality){
        int result = 60;
        switch (input_quality){
            case "Best":
              result = 100;
              break;
            case "Very high":
              result = 80;
              break;
            case "Medium":
              result = 40;
              break;
            case "Low":
              result = 20;
              break;
        }
        return result;
    }
    public static double check_size(String input_size){
        double result =1;
        switch (input_size){
            case "0.5x":
                result = 0.5;
                break;
            case "1.5x":
                result = 1.5;
                break;
            case "2x":
                result = 2;
                break;
            case "3x":
                result = 3;
                break;
        }
        return result;
    }
}