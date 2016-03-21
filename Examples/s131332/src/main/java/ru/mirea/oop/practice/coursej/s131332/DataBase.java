package ru.mirea.oop.practice.coursej.s131332;

import com.google.common.io.CharStreams;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DataBase {
    private static HashMap<String, String> data = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(DataBase.class);


    public static void get() {
        data.clear();
        try (Reader reader = new InputStreamReader(DataBase.class.getResourceAsStream("/help.dict"))) {
            List<String> lines = CharStreams.readLines(reader);
            for (String line : lines) {
                String[] splitted = line.split(" ");
                data.put(splitted[0].trim(), splitted[1].trim());
            }
        } catch (FileNotFoundException ex) {
            logger.error("Файл не найден", ex);
        } catch (IOException ex) {
            logger.error("Ошибка чтения файла", ex);
        }
    }

    public static void write(String message) {
        Path path = Paths.get("/help.dict");
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            List<String> lines = CharStreams.readLines(reader);
            lines.add(message);
            Files.write(Paths.get("/help.dict"), lines);
            reader.close();
        } catch (FileNotFoundException ex) {
            logger.error("Файл не найден", ex);
        } catch (IOException ex) {
            logger.error("Ошибка чтения файла", ex);
        }
    }

    public static void delete(String message) {
        Path path = Paths.get("/help.dict");
        int i = 0;
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            List<String> lines = CharStreams.readLines(reader);
            String name = Parser.getNamePlusInfoName(message);
            for (String line : lines) {
                String name2 = Parser.getNamePlusInfoName(line);
                if (name.equals(name2)) {
                    lines.remove(i);
                }
                i++;
            }
            Files.write(Paths.get("/help.dict"), lines);
            reader.close();
        } catch (FileNotFoundException ex) {
            logger.error("Файл не найден", ex);
        } catch (IOException ex) {
            logger.error("Ошибка чтения файла", ex);
        }
    }

    public static void refresh(String message) {
        Path path = Paths.get("/help.dict");
        int i = 0;
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            List<String> lines = CharStreams.readLines(reader);
            String name = Parser.getNamePlusInfoName(message);
            for (String line : lines) {
                String name2 = Parser.getNamePlusInfoName(line);
                if (name.equals(name2)) {
                    lines.set(i, message);
                }
                i++;
            }
            Files.write(Paths.get("/help.dict"), lines);
            reader.close();
        } catch (FileNotFoundException ex) {
            logger.error("Файл не найден", ex);
        } catch (IOException ex) {
            logger.error("Ошибка чтения файла", ex);
        }
    }

    static String getAnswer(String key) {
        return data.get(key);
    }

}



