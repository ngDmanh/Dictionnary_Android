package com.example.dictionary_ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dictionary_ui.adapter.WordAdapter;
import com.example.dictionary_ui.api.CambridgeAPIClient;
import com.example.dictionary_ui.api.EnglishService;
import com.example.dictionary_ui.models.Word;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {
    private EnglishService englishService;
    private ImageButton ibFind;
    private EditText etWrite;
    private ImageButton ibClose;
    private RecyclerView rcvWords;
    private WordAdapter wordAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    void initView() {
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ibFind = findViewById(R.id.ibFind);
        etWrite = findViewById(R.id.etWrite);
        ibClose = findViewById(R.id.ibClose);
        rcvWords = findViewById(R.id.rcvWords);

        ibFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = etWrite.getText().toString();
                searchByKeyword(keyword);
            }
        });
        ibClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etWrite.setText(null);
            }
        });

        wordAdapter = new WordAdapter();
        rcvWords.setHasFixedSize(true);
        rcvWords.setAdapter(wordAdapter);
    }

    void initData() {
        englishService = CambridgeAPIClient.getClient().create(EnglishService.class);
    }

    void searchByKeyword(String keyword) {
        Call<Word> call = englishService.searchByKeyword(keyword);
        call.enqueue(new Callback<Word>() {

            @Override
            public void onResponse(@NonNull Call<Word> call, @NonNull retrofit2.Response<Word> response) {
                Word word = response.body();
                if (word != null) {
                    displayWords(word);
                } else {
                    Toast.makeText(MainActivity.this, "Call api error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Word> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Call api error", Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });
    }

    void displayWords(Word word) {
        List<Word> listWord = new ArrayList<>();
        listWord.add(word);
        if (wordAdapter != null) {
            wordAdapter.setListWord(listWord);
            wordAdapter.notifyDataSetChanged();
        }
    }

}