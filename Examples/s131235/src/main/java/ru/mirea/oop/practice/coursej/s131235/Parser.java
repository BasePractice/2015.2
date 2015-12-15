package ru.mirea.oop.practice.coursej.s131235;

/**
 * Created by TopKek on 12.12.2015.
 */
public class Parser {
    private String msg;
    private String language;
    private String text;


    public Parser(String message) {

        this.msg = message;
        this.language = findLanguage(message);
        this.text = findText(message);

    }

    private String findLanguage(String message) {
        String[] texx = message.split(":", 2);
        String[] lang = texx[0].split(" ", (texx[0].length()) - 1);
        return lang[3];

    }

    private String findText(String message) {
        String[] texx = message.split(":", 2);
        return texx[1];

    }

    public String getLanguage() {
        return language;
    }

    public String getText() {
        return text;
    }
}
