package ru.mirea.oop.practice.coursej.ext;

import ru.mirea.oop.practice.coursej.vk.entities.Contact;

import java.io.IOException;
import java.io.Serializable;

public interface Extension extends Serializable {

    String name();

    Contact owner() throws IOException;

    boolean isService();

    boolean isRunning();

    boolean isLoaded();

    void stop();

    void start();

    void load();
}
