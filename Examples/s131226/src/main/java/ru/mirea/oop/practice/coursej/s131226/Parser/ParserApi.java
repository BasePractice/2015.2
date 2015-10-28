package ru.mirea.oop.practice.coursej.s131226.parser;

public interface ParserApi {
    String getDBState();

    java.io.File getReport();

    java.io.File getChanges();

    String getShortReport();

    String getShortChanges();

    void updateDB() throws InterruptedException;

}
