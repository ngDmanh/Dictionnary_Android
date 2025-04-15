package com.example.dictionary_ui.api;

import com.example.dictionary_ui.models.Word;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface EnglishService {
    @GET("words/{word}")
    Call<Word> searchByKeyword(@Path("word") String keyword);
}
