package ru.mirea.oop.practice.coursej.api;

public interface Provider<T> {
    T get();

    void put(T value);
}
