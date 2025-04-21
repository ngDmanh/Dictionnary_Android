package com.example.dictionary_ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dictionary_ui.adapter.WordAdapter;
import com.example.dictionary_ui.api.EnglishAPIClient;
import com.example.dictionary_ui.api.EnglishService;
import com.example.dictionary_ui.models.Word;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 100;
    private EnglishService englishService;
    private ImageButton ibFind, ibPickImage, ibClose;
    private EditText etWrite;
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
        ibPickImage = findViewById(R.id.ibPickImage);

        ibFind.setOnClickListener(v -> {
            String keyword = etWrite.getText().toString();
            searchByKeyword(keyword);
        });

        ibClose.setOnClickListener(v -> etWrite.setText(null));

        ibPickImage.setOnClickListener(v -> {
            if (checkPermission()) {
                openGallery();
            } else {
                requestPermission();
            }
        });

        wordAdapter = new WordAdapter();
        rcvWords.setHasFixedSize(true);
        rcvWords.setAdapter(wordAdapter);
    }

    void initData() {
        englishService = EnglishAPIClient.getClient().create(EnglishService.class);
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

    private boolean checkPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    // Request storage permission
    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            processImage(imageUri);
        }
    }

    private void processImage(Uri imageUri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            InputImage image = InputImage.fromBitmap(bitmap, 0);
            TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

            recognizer.process(image)
                    .addOnSuccessListener(visionText -> {
                        String extractedText = visionText.getText();
                        etWrite.setText(extractedText);
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(MainActivity.this, "Text extraction failed", Toast.LENGTH_SHORT).show();
                    });
        } catch (IOException e) {
            Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show();
        }
    }
}