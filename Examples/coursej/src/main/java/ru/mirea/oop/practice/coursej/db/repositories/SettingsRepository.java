package ru.mirea.oop.practice.coursej.db.repositories;

public interface SettingsRepository {

    void add(String key, String value);

    void delete(String key);

    Integer getInteger(String key);

    String getString(String key);

    Boolean getBoolean(String key);
}
