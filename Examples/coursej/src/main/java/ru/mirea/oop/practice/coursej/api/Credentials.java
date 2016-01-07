package ru.mirea.oop.practice.coursej.api;

import ru.mirea.oop.practice.coursej.Configuration;

import java.io.IOException;
import java.util.Properties;

public final class Credentials {
    public final String prefix;
    public final int id;
    public final String username;
    public final String password;

    private Credentials(String prefix, int id, String username, String password) {
        this.prefix = prefix;
        this.id = id;
        this.username = username;
        this.password = password;
    }

    Credentials(String prefix) {
        this.prefix = prefix;
        Properties prop = new Properties();
        try {
            prop.load(Configuration.loadFrom(".credentials"));
            this.id = Integer.parseInt(prop.getProperty(prefix + ".id"));
            this.username = prop.getProperty(prefix + ".username");
            this.password = prop.getProperty(prefix + ".password");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
