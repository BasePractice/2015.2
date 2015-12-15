package ru.mirea.oop.practice.coursej.db.entities;

public final class Property {
    public final int id;
    public final String key;
    public final String value;

    public Property(int id, String key, String value) {
        this.id = id;
        this.key = key;
        this.value = value;
    }
}
