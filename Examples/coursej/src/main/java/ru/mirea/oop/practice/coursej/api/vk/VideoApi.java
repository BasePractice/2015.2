package ru.mirea.oop.practice.coursej.api.vk;

import ru.mirea.oop.practice.coursej.api.vk.entities.Video;

public interface VideoApi extends ExternalCall {
    Video[] list(Long idOwner,
                 Integer idAlbum,
                 String videos,
                 Integer count,
                 Integer offset,
                 Integer extended) throws Exception;
}
