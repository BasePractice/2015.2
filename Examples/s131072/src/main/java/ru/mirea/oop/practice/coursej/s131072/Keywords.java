package ru.mirea.oop.practice.coursej.s131072;

import com.google.common.io.CharStreams;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;

final class Keywords {
    private static HashMap<String, String> answers = new HashMap<>();

    static void loadKeywords() {
        answers.clear();
        try (Reader reader = new InputStreamReader(Keywords.class.getResourceAsStream("/dictionary.dict"))) {
            List<String> lines = CharStreams.readLines(reader);
            for (String line: lines) {
                String [] splitted = line.split("=");
                answers.put(splitted[0].trim(), splitted[1].trim());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    static boolean isKeyword (String key) {
        return answers.containsKey(key);
    }

    static String getAnswer(String key) {
        return answers.get(key);
    }
}
