package ru.mirea.oop.practice.coursej;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

final class AccessTokenAuthenticator {

    public static void setAccessToken(OkHttpClient ok, String accessToken) {
        ok.networkInterceptors().add(chain -> {
            Request request = chain.request();
            HttpUrl httpUrl = request.httpUrl().newBuilder().addQueryParameter("access_token", accessToken).build();
            //NOTIFY: Для отладки можно включить
            //System.out.println("Request: " + httpUrl.toString());
            return chain.proceed(request.newBuilder().url(httpUrl).build());
        });
    }

}
