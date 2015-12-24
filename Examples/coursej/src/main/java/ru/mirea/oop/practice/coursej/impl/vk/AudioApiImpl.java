package ru.mirea.oop.practice.coursej.impl.vk;

import retrofit.Call;
import ru.mirea.oop.practice.coursej.api.vk.AudioApi;
import ru.mirea.oop.practice.coursej.api.vk.entities.Audio;
import ru.mirea.oop.practice.coursej.api.vk.entities.AudioLyrics;

final class AudioApiImpl implements AudioApi {
    private final Audios iter;

    AudioApiImpl(Audios iter) {
        this.iter = iter;
    }

    @Override
    public Audio[] list(Long idOwner,
                        Integer idAlbum,
                        String idsAudio,
                        Integer needUser,
                        Integer offset,
                        Integer count) throws Exception {
        Call<Result<Audio[]>> call = iter.list(idOwner, idAlbum, idsAudio, needUser, offset, count);
        return Result.call(call);
    }

    @Override
    public String getLyrics(long idLyrics) throws Exception {
        if (idLyrics <= 0)
            return null;
        final Call<Result<AudioLyrics>> call = iter.getLyrics(idLyrics);
        final AudioLyrics lyrics = Result.call(call);
        return lyrics.text;
    }
}
