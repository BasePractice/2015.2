package ru.mirea.oop.practice.coursej.s131235;

/**
 * Created by TopKek on 12.12.2015.
 */
public class Parser {
    private String msg;
    private String languageFirst;
    private String languageSecond;
    private String text;


    public Parser(String message) {

        this.msg = message;
        this.languageFirst = findLanguageFirst(message);
        this.languageSecond = findLanguageSecond(message);
        this.text = findText(message);

    }

    private String findLanguageFirst(String message) {
        String[] texx = message.split(":", 2);
        String[] lang = texx[0].split(" ", (texx[0].length()) - 1);
        return lang[0];

    }
    private String findLanguageSecond(String message) {
        String[] texx = message.split(":", 2);
        String[] lang = texx[0].split(" ", (texx[0].length()) - 1);
        return lang[1];

    }

    private String findText(String message) {
        String[] texx = message.split(":", 2);
        return texx[1];

    }

    public String getLanguageFirst() {
        return languageFirst;
    }

    public String getLanguageSecond() {
        return languageSecond;
    }

    public String getText() {
        return text;
    }
}
