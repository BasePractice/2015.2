package ru.mirea.oop.practice.coursej.impl.vk;

import ru.mirea.oop.practice.coursej.Configuration;

import java.io.IOException;
import java.util.Properties;

final class Credentials {
    final int id;
    final String username;
    final String password;

    private Credentials(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    Credentials() {
        Properties prop = new Properties();
        try {
            prop.load(Configuration.loadFrom(".credentials"));
            this.id = Integer.parseInt(prop.getProperty("id"));
            this.username = prop.getProperty("username");
            this.password = prop.getProperty("password");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
