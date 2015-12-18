package ru.mirea.oop.practice.coursej.s131072;

import com.google.common.io.CharStreams;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Paul on 14.12.2015.
 */
public final class Keywords {
    private static HashMap<String, String> answers = new HashMap<>();

    public static void loadKeywords() {
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

//    public static void fillMap() {
//        answers.put("привет", "Привет!");
//        answers.put("привет!", "Привет!");
//        answers.put("пока", "До встречи!");
//        answers.put("пока!", "До встречи!");
//        answers.put("дела?", "Отлично! А у тебя?");
//        answers.put(":)",":)");
//        answers.put("вычмат?","Нет ещё(("); //фразы не воспринимает, только отдельные слова
//        answers.put("ахаха","хахахахахахаха!");
//        answers.put("ночи", "Доброй ночи");
//        answers.put("Party", "It's my party and I'll cry if I want to");
//        answers.put("Bathroom", "She came in through the bathroom window");
//        answers.put("comes","Here comes the sun");
//        answers.put("jungle","welcome to the jungle");
//    }

    public static boolean isKeyword (String key) {
        return answers.containsKey(key);
    }

    public static String getAnswer(String key)
    {
        return answers.get(key);
    }
}
