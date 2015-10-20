package ru.mirea.oop.practice.coursej.api.vk.entities;

import java.io.Serializable;

public final class ResultList<T> implements Serializable {
    public int count;
    public T[] items;
}
