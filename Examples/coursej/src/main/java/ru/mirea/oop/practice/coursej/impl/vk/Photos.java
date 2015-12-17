package ru.mirea.oop.practice.coursej.impl.vk;

import retrofit.Call;
import retrofit.http.*;
import ru.mirea.oop.practice.coursej.api.vk.entities.Album;
import ru.mirea.oop.practice.coursej.api.vk.entities.Photo;
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

    @GET("/method/photos.get")
    Call<Result<Photo[]>> list(@Query("owner_id") Integer idOwner,
                               @Query("album_id") String idAlbum,
                               @Query("photo_ids") String idsPhoto,
                               @Query("rev") Integer rev,
                               @Query("extended") Integer extended,
                               @Query("feed_type") String feedType,
                               @Query("feed") Integer feed,
                               @Query("photo_sizes") Integer photoSizes,
                               @Query("offset") Integer offset,
                               @Query("count") Integer count);

    @GET("/method/photos.getAlbums")
    Call<Result<Album[]>> listAlbums(@Query("owner_id") Integer idOwner,
                                     @Query("album_ids") String idAlbums,
                                     @Query("offset") Integer offset,
                                     @Query("count") Integer count,
                                     @Query("need_system") Integer needSystem,
                                     @Query("need_cover") Integer needCover,
                                     @Query("photo_sizes") String photoSizes);
}