package ru.mirea.oop.practice.coursej.impl.vk;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;
import ru.mirea.oop.practice.coursej.api.vk.entities.Document;

interface Documents {
    @GET("/method/docs.get")
    Call<Result<Document[]>> list(@Query("count") Integer count,
                                  @Query("offset") Integer offset,
                                  @Query("owner_id") Integer idOwner);
}
