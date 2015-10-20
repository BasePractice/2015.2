package ru.mirea.oop.practice.coursej.api;

import com.squareup.okhttp.OkHttpClient;

public interface CommonApi {

    long idOwner();

    void start() throws Exception;

    OkHttpClient getClient();
}
