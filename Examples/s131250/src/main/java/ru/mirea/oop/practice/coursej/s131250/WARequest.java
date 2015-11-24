package ru.mirea.oop.practice.coursej.s131250;

import com.squareup.okhttp.ResponseBody;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

interface WARequest {
    @GET("/v2/query")
    Call<ResponseBody> doWARequest(@Query(value = "input", encoded = true) String input,
                                   @Query("appid") String appid,
                                   @Query("podstate") String ps);
}
