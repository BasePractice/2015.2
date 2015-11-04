package ru.mirea.oop.practice.coursej.impl.vk;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

interface Account {
    @GET("/method/account.setOnline")
    Call<Result<Integer>> setOnline(@Query("voip") Integer voip);
}
