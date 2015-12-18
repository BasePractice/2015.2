package ru.mirea.oop.practice.coursej.impl.vk;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class AccessTokenAuthenticator {
    private static final Logger logger = LoggerFactory.getLogger(AccessTokenAuthenticator.class);

    private AccessTokenAuthenticator() {
    }

    public static void setAccessToken(final OkHttpClient ok,
                                      final String accessToken) {
        ok.networkInterceptors().add(chain -> {
            Request request = chain.request();
            HttpUrl httpUrl = request.httpUrl().newBuilder()
                    .addQueryParameter("access_token", accessToken).build();
            //logger.debug("Url: " + httpUrl.toString());
            return chain.proceed(request.newBuilder().url(httpUrl).build());
        });
    }

}
