package ru.mirea.oop.practice.coursej.impl.vk;

import retrofit.Call;
import retrofit.http.*;
import ru.mirea.oop.practice.coursej.api.vk.entities.Document;
import ru.mirea.oop.practice.coursej.api.vk.entities.UploadServer;

interface Documents {
    @GET("/method/docs.getUploadServer")
    Call<Result<UploadServer>> getDocumentsUploadServer();

    @GET("method/docs.delete")
    Call<Result<Integer>> delete(@Query("did") Long id,
                                 @Query("oid") Long idOwner);


    @FormUrlEncoded
    @POST("/method/docs.save")
    Call<Result<Document[]>> saveDocument(@Field("file") String file,
                                          @Field("title") String title,
                                          @Field("tags") String tags);

    @GET("/method/docs.get")
    Call<Result<Document[]>> list(@Query("count") Integer count,
                                  @Query("offset") Integer offset,
                                  @Query("owner_id") Integer idOwner);

    @GET("/method/docs.getById")
    Call<Result<Document[]>> id(@Query("docs") String docs);
}
