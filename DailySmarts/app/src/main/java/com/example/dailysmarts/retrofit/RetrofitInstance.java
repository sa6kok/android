package com.example.dailysmarts.retrofit;


import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    public final static String ENGLISH = "en";
    public final static String RUSSIAN = "ru";
    private final String BASE_URL = "http://api.forismatic.com/api/1.0/?method=getQuote&format=json&lang=";

    private static RetrofitInstance retrofit;
    private SmartService smartService;


    public static RetrofitInstance getInstance(String language) {
        if(retrofit == null) {
           retrofit = new RetrofitInstance(language);
        }
        return retrofit;
    }

    public SmartService getSmartService() {
        return smartService;
    }

    private RetrofitInstance(String language) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL + language)
                .client(new OkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.smartService = retrofit.create(SmartService.class);
    }
}
