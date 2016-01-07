package ru.mirea.oop.practice.coursej.impl.vk;

import com.squareup.okhttp.*;
import ru.mirea.oop.practice.coursej.api.Provider;
import ru.mirea.oop.practice.coursej.api.Token;

final class VkAuthenticator implements Constants {
    private final Provider<ru.mirea.oop.practice.coursej.api.Credentials> credentials;
    private Provider<Token> token;

    VkAuthenticator() {
        Getter getter = new Getter.DefaultGetter(PREFIX);
        this.credentials = getter.getCredentials();
        this.token = getter.getToken();
    }

    public long idOwner() {
        return token.get().idUser;
    }

    public void authenticate(OkHttpClient ok) throws Exception {

        if (token.get() == null || token.get().idUser < 0 ||
                (token.get().expireTime < System.currentTimeMillis() && token.get().expireTime != 0)) {
            if (credentials.get() == null) {
                throw new Exception("Credentials not found");
            }
            HttpUrl url = new UrlBuilder()
                    .setClientId(credentials.get().id)
                    .addScope(UrlBuilder.Scope.AUDIO)
                    .addScope(UrlBuilder.Scope.DOCS)
                    .addScope(UrlBuilder.Scope.MESSAGES)
                    .addScope(UrlBuilder.Scope.NOTES)
                    .addScope(UrlBuilder.Scope.NOTIFICATIONS)
                    .addScope(UrlBuilder.Scope.NOTIFY)
                    .addScope(UrlBuilder.Scope.PHOTOS)
                    .addScope(UrlBuilder.Scope.VIDEO)
                    .addScope(UrlBuilder.Scope.WALL)
                    .addScope(UrlBuilder.Scope.OFFLINE)
                    .build();
            Request request = new Request.Builder().url(url).build();
            //System.out.println(url.toString());
            Response response = ok.newCall(request).execute();
            ResponseBody body = response.body();
            FormAction fa = FormAction.create(body.string());
            if (fa == null) {
                throw new Exception("Form");
            }
            Response processed = fa.process(ok, data -> {
                data.put("email", credentials.get().username);
                data.put("pass", credentials.get().password);
            });
            if (processed.code() != 302) {
                throw new Exception("Must 302 code");
            }
            String location = processed.header("Location");
            if (!location.contains("__q_hash")) {
                throw new Exception("Hash not found");
            }
            request = new Request.Builder().url(location).build();
            response = ok.newCall(request).execute();
            if (!response.isSuccessful())
                throw new Exception(processed.toString());
            fa = FormAction.create(response.body().string());
            if (fa == null)
                throw new Exception("Confirm");
            processed = fa.process(ok, null);
            if (processed.code() != 302) {
                throw new Exception("Must 302 code");
            }
            location = processed.header("Location");
            location = location.replaceAll("#", "?");
            token.put(Token.parse(PREFIX, HttpUrl.parse(location)));
            Token.save(PREFIX, token.get());
        }
        AccessTokenAuthenticator.setAccessToken(ok, token.get().accessToken);
    }
}
