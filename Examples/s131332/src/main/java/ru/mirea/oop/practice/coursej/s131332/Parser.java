package ru.mirea.oop.practice.coursej.s131332;


final class Parser {
    private static String request;
    private static String name;
    private static String getInfo;
    private static String getName;


    Parser(String message) {
        request = getRequest(message);
        getName = getNamePlusInfoName(message);
        getInfo = getNamePlusInfoInfo(message);

    }

    public static String getRequest(String message) {
        String[] info = message.split(" ", 2);
        request = info[0];
        return request;

    }

    public static String getNamePlusInfo(String message) {
        String[] request = message.split(" ", 2);
        name = request[1];
        return name;

    }

    public static String getNamePlusInfoName(String message) {
        String[] request = message.split("=", 2);
        getName = request[0];
        return getName;

    }

    public static String getNamePlusInfoInfo(String message) {
        String[] request = message.split("=", 2);
        getInfo = request[1];
        return getInfo;

    }
}

