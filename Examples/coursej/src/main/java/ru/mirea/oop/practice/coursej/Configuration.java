package ru.mirea.oop.practice.coursej;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public final class Configuration {
    private static final String HOME = System.getProperty("user.home");

    public static InputStream loadFrom(String resourceName) throws FileNotFoundException {
        return new FileInputStream(new File(HOME, resourceName));
    }

    public static String getFileName(String fileName) {
        return new File(HOME, fileName).getAbsolutePath();
    }

}
