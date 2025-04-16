package com.example.dictionary_ui.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EnglishAPIClient {
    private EnglishAPIClient() {}
    private static Retrofit retrofit;
    private static final String BASE_URL = "http://192.168.32.246:8000/";
    public static Retrofit getClient() {
        if (retrofit == null) {
                 retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}