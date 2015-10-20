package ru.mirea.oop.practice.coursej.impl.vk;

import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import ru.mirea.oop.practice.coursej.api.vk.entities.UploadServer;

interface Photos {
    @GET("/method/photos.getMessagesUploadServer")
    Call<Result<UploadServer>> getMessagesUploadServer();

    @FormUrlEncoded
    @POST("/method/photos.saveMessagesPhoto")
    Call<Result<Object>> saveMessagesPhoto(@Field("server") Integer server,
                                           @Field("photos_list") String photoList,
                                           @Field("photo") String photo,
                                           @Field("hash") String hash);
}