package ru.mirea.oop.practice.coursej.vk;

import com.squareup.okhttp.OkHttpClient;

public interface VkApi {

    long idOwner();

    OkHttpClient getClient();

    Users getUsers();

    Messages getMessages();

    Photos saveMessagesPhoto();

    Friends getFriends();

    Photos getMessagesUploadServer();

    Messages setActivity();

    Account setOnline();
}
