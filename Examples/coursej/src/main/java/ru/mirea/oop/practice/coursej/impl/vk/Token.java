package ru.mirea.oop.practice.coursej.impl.vk;

import com.squareup.okhttp.HttpUrl;
import ru.mirea.oop.practice.coursej.Configuration;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Properties;

final class Token {
    private static final String OS = System.getProperty("os.name").toLowerCase();
    public static final String ACCESS_TOKEN = "access_token";
    public static final String EXPIRES_IN = "expires_in";
    public static final String USER_ID = "user_id";
    final String accessToken;
    final long expireTime;
    final long idUser;

    private Token() {
        Properties prop = new Properties();
        try {
            prop.load(Configuration.loadFrom(".accessToken"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        this.accessToken = prop.getProperty(ACCESS_TOKEN);
        this.expireTime = Long.parseLong(prop.getProperty(EXPIRES_IN, "-1"));
        this.idUser = Long.parseLong(prop.getProperty(USER_ID, "-1"));
    }

    private Token(String accessToken, long expireTime, long idUser) {
        this.accessToken = accessToken;
        this.expireTime = expireTime;
        this.idUser = idUser;
    }

    @SuppressWarnings("unused")
    static Token parse(HttpUrl url) {
        String accessToken = url.queryParameter(ACCESS_TOKEN);
        String value = url.queryParameter(EXPIRES_IN);
        if (value == null)
            value = "0";
        long time = Long.parseLong(value);
        if (time == 0)
            time = 100000;
        long expireTime = time + System.currentTimeMillis();
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
}
