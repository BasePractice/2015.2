package ru.mirea.oop.practice.coursej;

import com.google.common.io.CharStreams;

import java.io.*;

public final class Configuration {
    private static final String HOME = System.getProperty("user.home");

    public static InputStream loadFrom(String resourceName) throws FileNotFoundException {
        return new FileInputStream(new File(HOME, resourceName));
    }

    public static String getFileName(String fileName) {
        return new File(HOME, fileName).getAbsolutePath();
    }

    public static String loadKeyFrom(String fileName) {
        try (InputStream stream = loadFrom(fileName)) {
            return CharStreams.toString(new InputStreamReader(stream)).trim();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
