package com.example.dictionary_ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dictionary_ui.R;
import com.example.dictionary_ui.models.Meaning;

import java.util.ArrayList;
import java.util.List;

public class MeaningAdapter extends RecyclerView.Adapter<MeaningAdapter.ContentViewHolder> {
    private final List<Meaning> listMeaning;

    public MeaningAdapter(List<Meaning> listMeaning) {
        this.listMeaning = listMeaning == null ? new ArrayList<>() : listMeaning;
    }

    @NonNull
    @Override
    public ContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meaning, parent, false);
        return new ContentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContentViewHolder holder, int position) {
        if (position < listMeaning.size()) {
            Meaning meaning = listMeaning.get(position);
            if (meaning != null) holder.binData(meaning);
        }
    }

    @Override
    public int getItemCount() {
        return listMeaning.size();
    }

    public static class ContentViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvPartOfSpeech;
        private final RecyclerView rcvDefinition;

        public ContentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPartOfSpeech = itemView.findViewById(R.id.tvPartOfSpeech);
            rcvDefinition = itemView.findViewById(R.id.rcvDefinition);
        }

        public void binData(@NonNull Meaning meaning) {
            tvPartOfSpeech.setText(meaning.getPartOfSpeech());
            DefinitionAdapter definitionAdapter = new DefinitionAdapter(meaning.getDefinitions());
            rcvDefinition.setAdapter(definitionAdapter);
            definitionAdapter.notifyDataSetChanged();
        }
    }
}
