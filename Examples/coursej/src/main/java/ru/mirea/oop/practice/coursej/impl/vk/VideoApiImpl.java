package ru.mirea.oop.practice.coursej.impl.vk;

import retrofit.Call;
import ru.mirea.oop.practice.coursej.api.vk.VideoApi;
import ru.mirea.oop.practice.coursej.api.vk.entities.Video;

final class VideoApiImpl implements VideoApi {
    private final Videos iter;

    VideoApiImpl(Videos iter) {
        this.iter = iter;
    }

    @Override
    public Video[] list(Long idOwner,
                        Integer idAlbum,
                        String videos,
                        Integer count,
                        Integer offset,
                        Integer extended) throws Exception {
        Call<Result<Video[]>> call = iter.list(idOwner, idAlbum, videos, count, offset, extended);
        return Result.call(call);
    }
}
