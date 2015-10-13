package ru.mirea.oop.practice.coursej;

import com.squareup.okhttp.HttpUrl;

import java.util.HashSet;
import java.util.Set;

final class UrlBuilder {
    private static final String BASE = "oauth.vk.com";
    private final Set<String> scopes = new HashSet<>();
    private String clientId;
    private String display = Display.MOBILE.toString().toLowerCase();
    private String responseType = ResponseType.TOKEN.toString().toLowerCase();
    private String redirectUrl = "https://oauth.vk.com/blank.html";

    enum Scope {
        PHOTOS,
        AUDIO,
        VIDEO,
        DOCS,
        WALL,
        MESSAGES,
        NOTIFICATIONS,
        NOTES,
        NOTIFY,
        OFFLINE
    }

    enum Display {
        MOBILE
    }

    enum ResponseType {
        TOKEN
    }

    UrlBuilder addScope(Scope scope) {
        scopes.add(scope.toString().toLowerCase());
        return this;
    }

    UrlBuilder setClientId(int id) {
        clientId = Integer.toString(id);
        return this;
    }

    UrlBuilder setDisplay(Display display) {
        this.display = display.toString().toLowerCase();
        return this;
    }

    UrlBuilder setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
        return this;
    }

    UrlBuilder setResponseType(ResponseType type) {
        this.responseType = type.toString().toLowerCase();
        return this;
    }

    private String scopes() {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (String scope : this.scopes) {
            if (i > 0)
                sb.append(",");
            sb.append(scope);
            ++i;
        }
        return sb.toString();
    }

    HttpUrl build() {
        return new HttpUrl.Builder()
                .scheme("https")
                .host(BASE)
                .addPathSegment("authorize")
                .addQueryParameter("client_id", clientId)
                .addQueryParameter("display", display)
                .addQueryParameter("redirect_uri", redirectUrl)
                .addQueryParameter("scope", scopes())
                .addQueryParameter("response_type", responseType)
                .addQueryParameter("v", "5.8")
                .build();
    }
}
