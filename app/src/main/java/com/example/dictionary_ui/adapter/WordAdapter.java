package com.example.dictionary_ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dictionary_ui.R;
import com.example.dictionary_ui.models.Phonetics;
import com.example.dictionary_ui.models.Word;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.Player;

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
        private final RecyclerView rcvMeaning;
        private final LinearLayout layoutPronounceUSTitle,layoutPronounceUKTitle;

        public WordViewHolder(@NonNull View itemView) {
            super(itemView);
            tvWord = itemView.findViewById(R.id.tvWord);
            tvPronounceUS = itemView.findViewById(R.id.tvPronounceUS);
            tvPronounceUK = itemView.findViewById(R.id.tvPronounceUK);
            rcvMeaning = itemView.findViewById(R.id.rcvMeaning);
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
                    playAudioWithExoPlayer(phonetics.getAudio(), v.getContext());
                } else {
                    Toast.makeText(v.getContext(), "No audio available", Toast.LENGTH_SHORT).show();
                }
            });
            layoutPronounceUKTitle.setOnClickListener(v -> {
                if (phonetics != null && phonetics.getAudio() != null && !phonetics.getAudio().isEmpty()) {
                    playAudioWithExoPlayer(phonetics.getAudio(), v.getContext());
                } else {
                    Toast.makeText(v.getContext(), "No audio available", Toast.LENGTH_SHORT).show();
                }
            });
            MeaningAdapter meaningAdapter = new MeaningAdapter(word.getMeanings());
            rcvMeaning.setHasFixedSize(true);
            rcvMeaning.setAdapter(meaningAdapter);
            meaningAdapter.notifyDataSetChanged();
        }
        private void playAudioWithExoPlayer(String audioUrl, Context context) {
            try {
                ExoPlayer player = new ExoPlayer.Builder(context).build();
                MediaItem mediaItem = MediaItem.fromUri(audioUrl);
                player.setMediaItem(mediaItem);
                player.prepare();
                player.play();
                player.addListener(new Player.Listener() {
                    @Override
                    public void onPlaybackStateChanged(int playbackState) {
                        if (playbackState == Player.STATE_ENDED ||
                                playbackState == Player.STATE_IDLE) {
                            player.release();
                        }
                    }

                    @Override
                    public void onPlayerError(PlaybackException error) {
                        Log.e("WordAdapter", "ExoPlayer error: " + error.getMessage());
                        Toast.makeText(context, "Lỗi phát âm thanh", Toast.LENGTH_SHORT).show();
                        player.release();
                    }
                });
            } catch (Exception e) {
                Log.e("WordAdapter", "ExoPlayer exception", e);
                Toast.makeText(context, "Lỗi khởi tạo player", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
