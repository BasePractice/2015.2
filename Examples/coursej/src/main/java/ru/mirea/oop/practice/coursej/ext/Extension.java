package ru.mirea.oop.practice.coursej.ext;

import java.io.Serializable;

public interface Extension extends Serializable {
    boolean isService();

    boolean isRunning();

    boolean isLoaded();

    void stop();

    void start();

    void load();
}
