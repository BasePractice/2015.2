package ru.mirea.oop.practice.coursej.s131226.Parser;

import ru.mirea.oop.practice.coursej.api.tg.entities.File;

public interface ParserApi {
    String getDBState();

    java.io.File getReport();

    java.io.File getChanges();

    String getShortReport();

    String getShortChanges();

    void updateDB() throws InterruptedException;

}
