package com.example.dictionary_ui.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dictionary_ui.R;
import com.example.dictionary_ui.models_new.Phonetics;
import com.example.dictionary_ui.models_new.Word;

import java.util.ArrayList;
import java.util.List;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.WordViewHolder> {
    private List<Word> listWord = new ArrayList<>();

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_word, parent, false);
        return new WordViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        if (position < listWord.size()) {
            Word word = listWord.get(position);
            if (word != null) holder.bindData(word);
        }
    }

    @Override
    public int getItemCount() {
        return listWord.size();
    }

    public void setListWord(List<Word> listWord) {
        this.listWord = listWord;
    }

    public static class WordViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvWord;
        private final TextView tvPronounceUS;
        private final TextView tvPronounceUK;
        private final RecyclerView rcvContents;
        private final LinearLayout layoutPronounceUSTitle,layoutPronounceUKTitle;

        public WordViewHolder(@NonNull View itemView) {
            super(itemView);
            tvWord = itemView.findViewById(R.id.tvWord);
            tvPronounceUS = itemView.findViewById(R.id.tvPronounceUS);
            tvPronounceUK = itemView.findViewById(R.id.tvPronounceUK);
            rcvContents = itemView.findViewById(R.id.rcvMeaning);
            layoutPronounceUSTitle = itemView.findViewById(R.id.layoutPronounceUSTitle);
            layoutPronounceUKTitle = itemView.findViewById(R.id.layoutPronounceUKTitle);
        }

        public void bindData(@NonNull Word word) {
            tvWord.setText(word.getWord());
            Phonetics phonetics = word.getPronounce();
            tvPronounceUS.setText(phonetics == null ? "" : phonetics.getText());
            tvPronounceUK.setText(phonetics == null ? "" : phonetics.getText());
            layoutPronounceUSTitle.setOnClickListener(v -> {
                if (phonetics != null && phonetics.getAudio() != null && !phonetics.getAudio().isEmpty()) {
                    playAudio(phonetics.getAudio(), v.getContext());
                } else {
                    Toast.makeText(v.getContext(), "No audio available", Toast.LENGTH_SHORT).show();
                }
            });
            layoutPronounceUKTitle.setOnClickListener(v -> {
                if (phonetics != null && phonetics.getAudio() != null && !phonetics.getAudio().isEmpty()) {
                    playAudio(phonetics.getAudio(), v.getContext());
                } else {
                    Toast.makeText(v.getContext(), "No audio available", Toast.LENGTH_SHORT).show();
                }
            });
            MeaningAdapter meaningAdapter = new MeaningAdapter(word.getMeanings());
            rcvContents.setHasFixedSize(true);
            rcvContents.setAdapter(meaningAdapter);
            meaningAdapter.notifyDataSetChanged();
        }
        private void playAudio(String audioUrl, Context context) {
            MediaPlayer mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(audioUrl);
                mediaPlayer.setOnPreparedListener(MediaPlayer::start);
                mediaPlayer.setOnCompletionListener(MediaPlayer::release);
                mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                    Toast.makeText(context, "Error playing audio", Toast.LENGTH_SHORT).show();
                    mp.release();
                    return true;
                });
                mediaPlayer.prepareAsync();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context, "Failed to play audio", Toast.LENGTH_SHORT).show();
                mediaPlayer.release();
            }
        }
    }
}
