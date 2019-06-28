package com.example.dailysmarts.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface SmartService {
    @GET
    Call<SmartModel> getItem(@Url String  url) ;
}
