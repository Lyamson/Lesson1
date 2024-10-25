package ru.mirea.alyamovskiyvy.mireaproject.ui.httpinfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface API {
    @GET("coffee/hot")
    Call<List<Coffee>> getCoffee();
}
