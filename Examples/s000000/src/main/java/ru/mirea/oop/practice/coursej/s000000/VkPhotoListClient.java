package ru.mirea.oop.practice.coursej.s000000;

import ru.mirea.oop.practice.coursej.api.vk.PhotosApi;
import ru.mirea.oop.practice.coursej.api.vk.entities.Album;
import ru.mirea.oop.practice.coursej.api.vk.entities.Photo;
import ru.mirea.oop.practice.coursej.impl.vk.ext.ClientBotsExtension;

public final class VkPhotoListClient extends ClientBotsExtension {
    public VkPhotoListClient() throws Exception {
        super("vk.clients.PhotoList");
    }

    @Override
    protected void doClient() throws Exception {
        final PhotosApi photos = api.getPhotos();
        final Album[] albums = photos.listAlbums(null, null, null, null, 1, null, null);
        for (Album album : albums) {
            long size = album.size;
            final int count = 1000;
            for (int offset = 0; offset < size; ) {
                final Photo[] list = photos.list(null, album.id, null, null, null, null, null, null, offset, count);
                for (Photo photo : list) {
                    System.out.println(photo);
                }
                offset += list.length;
                /**FIXME: Чтобы ВК не ругался на слишком частое обращение */
                Thread.sleep(1000);
            }
        }
    }

    @Override
    public String description() {
        return "Список фотографий пользователя";
    }
}
