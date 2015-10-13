package ru.mirea.oop.practice.coursej.ext;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public interface Extension extends Serializable {

    String name();

    long owner() throws IOException;

    boolean isService();

    boolean isRunning();

    boolean isLoaded();

    void stop();

    Future<?> start();

    void load();

    ExecutorService executor = Executors.newCachedThreadPool();
}
