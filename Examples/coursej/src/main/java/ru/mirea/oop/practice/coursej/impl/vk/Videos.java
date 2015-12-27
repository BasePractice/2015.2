package ru.mirea.oop.practice.coursej.impl.vk;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;
import ru.mirea.oop.practice.coursej.api.vk.entities.Video;


interface Videos {
    @GET("/method/video.get")
    Call<Result<Video[]>> list(@Query("owner_id") Long idOwner,
                               @Query("album_id") Integer idAlbum,
                               @Query("videos") String videos,
                               @Query("count") Integer count,
                               @Query("offset") Integer offset,
                               @Query("extended") Integer extended);

}
