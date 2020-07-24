package com.example.vishot.Gallery;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vishot.R;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<Image> list = new ArrayList<>();
    private int n = list.size();

    public ImageAdapter(Context context, int layout, ArrayList<Image> list) {
        this.context = context;
        this.layout = layout;
        this.list = list;
    }

    @Override
    public int getCount() {
        return n ;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView date = (TextView) convertView.findViewById(R.id.date);
        ImageView image = (ImageView) convertView.findViewById(R.id.imagecaptured);
//        if(position==0){
//            date.setText(list.get(position).getDate());
//            image.setImageBitmap(list.get(position).getImage());
//        }
//        while(position>0){
//            if(list.get(position).getDate()==list.get(position-1).getDate()){
//                date.setText(list.get(position).getDate());
//                image.setImageBitmap(list.get(position).getImage());
//                date.setVisibility(View.INVISIBLE);
//            }
//            else{
//                if(position%3==1){
//                    n=n+2;
//                    date.setText("");
//                    image.setImageResource(R.drawable.gallery);
//                    image.setVisibility(View.INVISIBLE);
//                }
//                if(position%3==2){
//                    n=n+1;
//                    date.setText("");
//                    image.setImageResource(R.drawable.gallery);
//                    image.setVisibility(View.INVISIBLE);
//                }
//                else{
//                    date.setText(list.get(position).getDate());
//                    image.setImageBitmap(list.get(position).getImage());
//                }
//            }
//    }
        date.setText(list.get(position).getDate());
            image.setImageBitmap(list.get(position).getImage());
        return convertView;
    }
}
