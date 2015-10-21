package ru.mirea.oop.practice.coursej.api.vk;

import ru.mirea.oop.practice.coursej.api.vk.entities.UploadServer;

import java.io.IOException;

public interface PhotosApi extends ExternalCall {
    UploadServer getMessagesUploadServer() throws IOException;

    Object saveMessagesPhoto(Integer server,
                             String photoList,
                             String photo,
                             String hash) throws IOException;
}