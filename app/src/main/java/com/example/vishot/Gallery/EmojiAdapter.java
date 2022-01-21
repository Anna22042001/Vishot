package com.example.vishot.Gallery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vishot.R;

import java.util.ArrayList;

import ja.burhanrashid52.photoeditor.PhotoEditor;

public class EmojiAdapter extends RecyclerView.Adapter<EmojiAdapter.ViewHolder> {
    Context context;
    ArrayList<String> emojiList;
    private OnEmojiClickListener onEmojiClickListener;

    public void MyEmojiClick(OnEmojiClickListener onEmojiClickListener){
        this.onEmojiClickListener = onEmojiClickListener;
    }



    public EmojiAdapter(Context context, ArrayList<String> emojiList) {
        this.context = context;
        this.emojiList = emojiList;
    }

    public EmojiAdapter(Context context) {
        this(context,PhotoEditor.getEmojis(context));
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.view_of_emoji,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmojiAdapter.ViewHolder holder, int position) {
        final String code = emojiList.get(position);
        holder.emojicode.setText(code);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEmojiClickListener.onEmojiListener(code);
            }
        });
    }

    @Override
    public int getItemCount() {
        return emojiList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView emojicode;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            emojicode = itemView.findViewById(R.id.emoji_code);
        }
    }
    public interface OnEmojiClickListener{
        void onEmojiListener(String emoji_code);
    }
}
