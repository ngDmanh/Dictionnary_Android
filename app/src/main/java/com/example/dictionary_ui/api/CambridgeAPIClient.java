package com.example.dictionary_ui.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CambridgeAPIClient {
    private CambridgeAPIClient() {}
    private static Retrofit retrofit;
    private static final String BASE_URL = "http://192.168.31.239:8000/";
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