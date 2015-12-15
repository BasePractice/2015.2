package ru.mirea.oop.practice.coursej.db;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class Context {

    private static Properties loadProperties() {
        Properties properties = new Properties();
        try (InputStream stream = Context.class.getResourceAsStream("/db.properties")) {
            properties.load(stream);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return properties;
    }

    public Connection getConnection() throws SQLException, ClassNotFoundException {
        Properties properties = loadProperties();
        Class.forName(properties.getProperty("jdbc.driver"));
        return DriverManager.getConnection(
                properties.getProperty("jdbc.url"),
                properties.getProperty("jdbc.username"),
                properties.getProperty("jdbc.password")
        );
    }

}
