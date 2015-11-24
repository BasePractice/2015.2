package ru.mirea.oop.practice.coursej.impl.vk;

import retrofit.Call;
import ru.mirea.oop.practice.coursej.api.vk.PhotosApi;
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
}
