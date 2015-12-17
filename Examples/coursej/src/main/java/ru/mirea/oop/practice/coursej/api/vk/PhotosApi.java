package ru.mirea.oop.practice.coursej.api.vk;

import ru.mirea.oop.practice.coursej.api.vk.entities.Album;
import ru.mirea.oop.practice.coursej.api.vk.entities.Photo;
import ru.mirea.oop.practice.coursej.api.vk.entities.UploadServer;

import java.io.IOException;

public interface PhotosApi extends ExternalCall {
    UploadServer getMessagesUploadServer() throws IOException;

    Object saveMessagesPhoto(Integer server,
                             String photoList,
                             String photo,
                             String hash) throws IOException;

    Photo[] list(Integer idOwner,
                 String idAlbum,
                 String idsPhoto,
                 Integer rev,
                 Integer extended,
                 String feedType,
                 Integer feed,
                 Integer photoSizes,
                 Integer offset,
                 Integer count) throws IOException;

    Album[] listAlbums(
            Integer idOwner,
            String idAlbums,
            Integer offset,
            Integer count,
            Integer needSystem,
            Integer needCover,
            String photoSizes) throws IOException;
}