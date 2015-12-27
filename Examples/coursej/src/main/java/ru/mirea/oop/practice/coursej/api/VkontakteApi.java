package ru.mirea.oop.practice.coursej.api;

import ru.mirea.oop.practice.coursej.api.vk.*;

public interface VkontakteApi extends CommonApi {

    UsersApi getUsers() throws Exception;

    MessagesApi getMessages() throws Exception;

    PhotosApi getPhotos() throws Exception;

    AccountApi getAccounts() throws Exception;

    FriendsApi getFriends() throws Exception;

    DocumentsApi getDocuments() throws Exception;

    AudioApi getAudios() throws Exception;

    VideoApi getVideos() throws Exception;
}
