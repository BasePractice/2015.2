package ru.mirea.oop.practice.coursej.api.vk;

import ru.mirea.oop.practice.coursej.api.vk.entities.Audio;

public interface AudioApi extends ExternalCall {
    Audio[] list(Long idOwner,
                 Integer idAlbum,
                 String idsAudio,
                 Integer needUser,
                 Integer offset,
                 Integer count) throws Exception;

    String getLyrics(long idLyrics) throws Exception;
}
