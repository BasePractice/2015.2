package ru.mirea.oop.practice.coursej.impl.vk;

import retrofit.Call;
import ru.mirea.oop.practice.coursej.api.vk.PhotosApi;
import ru.mirea.oop.practice.coursej.api.vk.entities.Album;
import ru.mirea.oop.practice.coursej.api.vk.entities.Photo;
import ru.mirea.oop.practice.coursej.api.vk.entities.UploadServer;

import java.io.IOException;

final class PhotosApiImpl implements PhotosApi {
    private final Photos inter;

    PhotosApiImpl(Photos inter) {
        this.inter = inter;
    }

    @Override
    public UploadServer getMessagesUploadServer() throws IOException {
        Call<Result<UploadServer>> call = inter.getMessagesUploadServer();
        return Result.call(call);
    }

    @Override
    public Object saveMessagesPhoto(Integer server, String photoList, String photo, String hash) throws IOException {
        Call<Result<Object>> call = inter.saveMessagesPhoto(server, photoList, photo, hash);
        return Result.call(call);
    }

    @Override
    public Photo[] list(Integer idOwner,
                        String idAlbum,
                        String idsPhoto,
                        Integer rev,
                        Integer extended,
                        String feedType,
                        Integer feed,
                        Integer photoSizes,
                        Integer offset,
                        Integer count) throws IOException {
        final Call<Result<Photo[]>> call = inter.list(idOwner, idAlbum, idsPhoto, rev,
                extended, feedType, feed, photoSizes, offset, count);
        return Result.call(call);
    }

    @Override
    public Album[] listAlbums(Integer idOwner,
                              String idAlbums,
                              Integer offset,
                              Integer count,
                              Integer needSystem,
                              Integer needCover,
                              String photoSizes) throws IOException {
        final Call<Result<Album[]>> call = inter.listAlbums(idOwner, idAlbums, offset, count,
                needSystem, needCover, photoSizes);
        return Result.call(call);
    }
}
