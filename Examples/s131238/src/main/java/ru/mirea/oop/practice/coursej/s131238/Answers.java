package ru.mirea.oop.practice.coursej.s131238;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

final class Answers {

    private static final Logger logger = LoggerFactory.getLogger(Answers.class);
    private final List<String> baseOfAnswers = new ArrayList<>();
    private final static Random random = new Random();

    Answers(){
        reload();
    }

    private void reload() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Answers.class.getResourceAsStream("/answers.txt")))){
            String line;
            while ((line = reader.readLine()) != null) {
                baseOfAnswers.add(line);
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Файл не найден");
        } catch (IOException ex) {
            System.out.println("Ошибка чтения файла");
        }
    }

    public String getRandomAnswer() {
        return baseOfAnswers.get(random.nextInt(baseOfAnswers.size()));
    }

}
