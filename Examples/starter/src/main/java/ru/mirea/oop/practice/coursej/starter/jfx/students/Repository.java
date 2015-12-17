package ru.mirea.oop.practice.coursej.starter.jfx.students;

import javafx.collections.ObservableList;

public interface Repository<E> {
    ObservableList<E> list();
}
