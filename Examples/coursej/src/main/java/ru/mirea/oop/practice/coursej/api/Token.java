package ru.mirea.oop.practice.coursej.api;

import com.squareup.okhttp.HttpUrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mirea.oop.practice.coursej.Configuration;

import java.io.*;
import java.util.Properties;

public final class Token {
    private static final Logger logger = LoggerFactory.getLogger(Token.class.getName());
    private static final String OS = System.getProperty("os.name").toLowerCase();
    public static final String ACCESS_TOKEN = "access_key";
    public static final String EXPIRES_IN = "expires_in";
    public static final String USER_ID = "user_id";
    public final String accessToken;
    public final long expireTime;
    public final long idUser;
    public final String prefix;

    private Token(String prefix) {
        Properties prop = new Properties();
        try {
            prop.load(Configuration.loadFrom(".access_tokens"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        this.prefix = prefix;
        this.accessToken = prop.getProperty(prefix + "." + ACCESS_TOKEN);
        this.expireTime = Long.parseLong(prop.getProperty(prefix + "." + EXPIRES_IN, "-1"));
        this.idUser = Long.parseLong(prop.getProperty(prefix + "." + USER_ID, "-1"));
    }

    private Token(String prefix, String accessToken, long expireTime, long idUser) {
        this.prefix = prefix;
        this.accessToken = accessToken;
        this.expireTime = expireTime;
        this.idUser = idUser;
    }

    @SuppressWarnings("unused")
    public static Token parse(String prefix, HttpUrl url) {
        String accessToken = url.queryParameter("access_token");
        String value = url.queryParameter(EXPIRES_IN);
        if (value == null)
            value = "0";
        long time = Long.parseLong(value);
        if (time == 0)
            time = 100000;
        long expireTime = time + System.currentTimeMillis();
        value = url.queryParameter(USER_ID);
        long idUser = value == null ? -1 : Long.parseLong(value);
        return new Token(prefix, accessToken, expireTime, idUser);
    }

    public static Token parse(String prefix, String url) {
        return parse(prefix, HttpUrl.parse(url.replace('#', '?')));
    }

    @SuppressWarnings("unused")
    public static void save(String prefix, Token token, String fileName) {
        Properties prop = new Properties();
        try (Reader reader = new FileReader(fileName)) {
            prop.load(reader);
        } catch (IOException ex) {
            logger.error("", ex);
        }
        prop.setProperty(prefix + "." + ACCESS_TOKEN, token.accessToken);
        prop.setProperty(prefix + "." + EXPIRES_IN, Long.toString(token.expireTime));
        prop.setProperty(prefix + "." + USER_ID, Long.toString(token.idUser));
        try (Writer writer = new FileWriter(fileName)) {
            prop.store(writer, "");
        } catch (IOException ex) {
            logger.error("", ex);
        }
    }

    public static void save(String prefix, Token token) {
        save(prefix, token, Configuration.getFileName(".access_tokens"));
    }
}
