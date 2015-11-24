package ru.mirea.oop.practice.coursej.api.ext;

import java.io.Serializable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public interface BotsExtension extends Serializable {

    String description();

    String name();

    long owner() throws Exception;

    boolean isService();

    boolean isRunning();

    boolean isLoaded();

    void stop();

    Future<?> start();

    void load();

    ExecutorService executor = Executors.newCachedThreadPool();
}
