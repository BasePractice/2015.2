package ru.mirea.oop.practice.coursej.impl.vk;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;
import ru.mirea.oop.practice.coursej.api.vk.entities.Audio;
import ru.mirea.oop.practice.coursej.api.vk.entities.AudioLyrics;

interface Audios {
    @GET("/method/audio.get")
    Call<Result<Audio[]>> list(@Query("owner_id") Long idOwner,
                               @Query("album_id") Integer idAlbum,
                               @Query("audio_ids") String idsAudio,
                               @Query("need_user") Integer needUser,
                               @Query("offset") Integer offset,
                               @Query("count") Integer count);

    @GET("/method/audio.getLyrics")
    Call<Result<AudioLyrics>> getLyrics(@Query("lyrics_id") long idLyrics);
}
