package ru.mirea.oop.practice.coursej.starter;

import ru.mirea.oop.practice.coursej.ext.Extension;

import java.io.IOException;
import java.util.ServiceLoader;

public final class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        ServiceLoader<Extension> loader = ServiceLoader.load(Extension.class);
        for (Extension object : loader) {
            object.load();
            if (!object.isLoaded()) {
                System.err.println("Сервис: \"" + object.name() + "\" не удалось загрузить");
                continue;
            }
            object.start();
            if (!object.isRunning()) {
                System.err.println("Сервис: \"" + object.name() + "\" не удалось запустить");
                continue;
            }
            System.err.println("Сервис: \"" + object.name() + "\" запущен");
        }
        Thread.sleep(1000000);
    }
}
