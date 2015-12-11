package ru.mirea.oop.practice.coursej.db.repositories;

import ru.mirea.oop.practice.coursej.db.Context;
import ru.mirea.oop.practice.coursej.db.RowConverter;
import ru.mirea.oop.practice.coursej.db.entities.Property;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

final class SettingsRepositoryImpl implements SettingsRepository {
    private static final RowConverter<Property> PROPERTY_CONVERTER = rs -> new Property(
            rs.getInt("ID"),
            rs.getString("KEY"),
            rs.getString("VALUE")
    );
    private final Context ctx = new Context();

    SettingsRepositoryImpl() {
        init(ctx);
    }

    private static void init(Context ctx) {
        try (Connection connection = ctx.getConnection()) {
            try (final Statement stmt = connection.createStatement()) {
                stmt.execute("CREATE TABLE IF NOT EXISTS SETTINGS(ID BIGINT AUTO_INCREMENT PRIMARY KEY, KEY VARCHAR(255) NOT NULL UNIQUE, VALUE VARCHAR(255) NOT NULL);");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void add(String key, String value) {
        try (Connection connection = ctx.getConnection()) {
            try (final PreparedStatement stmt = connection.prepareStatement("INSERT INTO SETTINGS(KEY, VALUE) VALUES(?, ?)")) {
                stmt.setString(1, key);
                stmt.setString(2, value);
                stmt.executeUpdate();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void delete(String key) {
        try (Connection connection = ctx.getConnection()) {
            try (final PreparedStatement stmt = connection.prepareStatement("DELETE FROM SETTINGS WHERE KEY = ?")) {
                stmt.setString(1, key);
                stmt.executeUpdate();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Integer getInteger(String key) {
        String value = getString(key);
        return Integer.parseInt(value == null ? "-1" : value);
    }

    @Override
    public String getString(String key) {
        Property property = getProperty(key);
        return property != null ? property.value : null;
    }

    @Override
    public Boolean getBoolean(String key) {
        return Boolean.parseBoolean(getString(key));
    }

    private Property getProperty(String key) {
        try (Connection connection = ctx.getConnection()) {
            try (final PreparedStatement stmt = connection.prepareStatement("SELECT * FROM SETTINGS WHERE KEY = ?")) {
                stmt.setString(1, key);
                try (final ResultSet rs = stmt.executeQuery()) {
                    if (rs.next())
                        return PROPERTY_CONVERTER.convert(rs);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
