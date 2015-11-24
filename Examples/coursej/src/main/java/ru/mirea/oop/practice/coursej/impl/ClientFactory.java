package ru.mirea.oop.practice.coursej.impl;

import com.squareup.okhttp.OkHttpClient;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.TimeUnit;

public final class ClientFactory {
    private static final int CONNECT_TIMEOUT_MILLIS = 30 * 1000;
    private static final int READ_TIMEOUT_MILLIS = 30 * 1000;
    private static final int WRITE_TIMEOUT_MILLIS = 30 * 1000;

    public static OkHttpClient createOkClient() {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        client.setReadTimeout(READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        client.setWriteTimeout(WRITE_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);

        client.setFollowSslRedirects(true);
        client.setFollowRedirects(true);

        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        client.setCookieHandler(cookieManager);
        client.setFollowRedirects(false);
//        client.interceptors().add(chain -> {
//            Request request = chain.request();
//            String string = bodyToString(request);
//            System.out.println(string);
//            return chain.proceed(request);
//        });
        return client;
    }
//
//    private static String bodyToString(final Request request) {
//
//        try {
//            final Request copy = request.newBuilder().build();
//            final Buffer buffer = new Buffer();
//            RequestBody body = copy.body();
//            if (body != null) {
//                body.writeTo(buffer);
//                return copy.toString() + "\r\n" + buffer.readUtf8();
//            }
//            return copy.toString();
//        } catch (final Exception e) {
//            return "did not work";
//        }
//    }
}
