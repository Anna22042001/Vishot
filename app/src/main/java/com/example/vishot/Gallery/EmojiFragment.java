package com.example.vishot.Gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vishot.R;

public class EmojiFragment extends Fragment {
    RecyclerView rv_Emoji;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_emoji,container,false);
        rv_Emoji = view.findViewById(R.id.rv_Emoji);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),5);
        EmojiAdapter emojiAdapter = new EmojiAdapter(getContext());
        rv_Emoji.setLayoutManager(layoutManager);
        rv_Emoji.setAdapter(emojiAdapter);
        emojiAdapter.MyEmojiClick(new EmojiAdapter.OnEmojiClickListener() {
            @Override
            public void onEmojiListener(String emoji_code) {
                EditImageActivity.addEmoji(emoji_code);
            }
        });
        return view;
    }
}
