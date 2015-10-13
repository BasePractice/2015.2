package ru.mirea.oop.practice.coursej.vk;

import com.squareup.okhttp.OkHttpClient;

public interface VkApi {

    OkHttpClient getClient();

    Users getUsers();

    Messages getMessages();

    Friends getFriends();
}
