package ru.mirea.oop.practice.coursej.s131235;

final class Parser {
    private final String languageFirst;
    private final String languageSecond;
    private final String text;


    Parser(String message) {
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

    String getLanguageFirst() {
        return languageFirst;
    }

    String getLanguageSecond() {
        return languageSecond;
    }

    String getText() {
        return text;
    }
}
