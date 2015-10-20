package ru.mirea.oop.practice.coursej.impl.vk;

import com.squareup.okhttp.*;
import ru.mirea.oop.practice.coursej.Configuration;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Properties;

final class Authenticator {
    private final Credentials credentials;
    private Token token;

    Authenticator() {
        this.credentials = Credentials.createDefault();
        this.token = Token.createDefault();
    }

    public long idOwner() {
        return token.idUser;
    }

    public void authenticate(OkHttpClient ok) throws Exception {

        if (token == null || token.idUser < 0 || (token.expireTime < System.currentTimeMillis() && token.expireTime != 0)) {
            HttpUrl url = new UrlBuilder()
                    .setClientId(credentials.id)
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
                data.put("email", credentials.username);
                data.put("pass", credentials.password);
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
            token = Token.parse(HttpUrl.parse(location));
            Token.save(token);
        }
        AccessTokenAuthenticator.setAccessToken(ok, token.accessToken);
    }

    private static final class Credentials {
        private final int id;
        private final String username;
        private final String password;

        private Credentials(int id, String username, String password) {
            this.id = id;
            this.username = username;
            this.password = password;
        }

        public static Credentials createDefault() {
            Properties prop = new Properties();
            try {
                prop.load(Configuration.loadFrom(".credentials"));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                return new Credentials(
                        Integer.parseInt(prop.getProperty("id")),
                        prop.getProperty("username"),
                        prop.getProperty("password"));
            } catch (Exception ex) {
                return null;
            }
        }
    }

    static final class Token {
        private static final String OS = System.getProperty("os.name").toLowerCase();
        public static final String ACCESS_TOKEN = "access_token";
        public static final String EXPIRES_IN = "expires_in";
        public static final String USER_ID = "user_id";
        private final String accessToken;
        private final long expireTime;
        private final long idUser;

        private Token(String accessToken, long expireTime, long idUser) {
            this.accessToken = accessToken;
            this.expireTime = expireTime;
            this.idUser = idUser;
        }

        @SuppressWarnings("unused")
        static Token parse(HttpUrl url) {
            String accessToken = url.queryParameter(ACCESS_TOKEN);
            String value = url.queryParameter(EXPIRES_IN);
            long expireTime = value == null ? -1 : (Long.parseLong(value) * 1000) + System.currentTimeMillis();
            value = url.queryParameter(USER_ID);
            long idUser = value == null ? -1 : Long.parseLong(value);
            return new Token(accessToken, expireTime, idUser);
        }

        static Token parse(String url) {
            return parse(HttpUrl.parse(url.replace('#', '?')));
        }

        @SuppressWarnings("unused")
        static void save(Token token, String fileName) {
            Properties prop = new Properties();
            prop.setProperty(ACCESS_TOKEN, token.accessToken);
            prop.setProperty(EXPIRES_IN, Long.toString(token.expireTime));
            prop.setProperty(USER_ID, Long.toString(token.idUser));
            try (Writer writer = new FileWriter(fileName)) {
                prop.store(writer, "");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        static void save(Token token) {
            save(token, Configuration.getFileName(".accessToken"));
        }

        public static Token createDefault() {
            Properties prop = new Properties();
            try {
                prop.load(Configuration.loadFrom(".accessToken"));
            } catch (Exception ex) {
                return null;
            }
            return new Token(
                    prop.getProperty(ACCESS_TOKEN),
                    Long.parseLong(prop.getProperty(EXPIRES_IN, "-1")),
                    Long.parseLong(prop.getProperty(USER_ID, "-1")));
        }
    }
}
