package ru.mirea.oop.practice.coursej.s131245;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/*
   Анализ запроса пользователя
 */
final class Parser {
    private final String msg;
    private final LocalDate date;
    private final ArrayList<String> queryOfPeople;
    private final boolean allPeople;
    private final boolean isDate;
    private static final ThreadLocal<DateTimeFormatter> threadFormat = new ThreadLocal<>();

    /*Парсер будет рабоать как для класса Attachment так и для VkStatistic, все переменные будут проиницилизораны
    с помощью полученного соообщения */
    Parser(String message) {
        //Пытаемся получить дату из сообщения
        if (message.contains("/")) {
                String date = message.split(" ")[message.split(" ").length - 1];
                this.date = LocalDate.parse(date, getFormat());
                this.isDate = true;
                message = message.substring(0, message.lastIndexOf(" "));
            } else {
                this.date = null;
                this.isDate = false;
            }

        //разбор запроса: "все пользователи" или отдельные люди
            if (message.contains(" всех")) {
                this.allPeople = true;
                this.queryOfPeople = new ArrayList<>();
            } else {
                String[] people = message.split(": ")[1].split(", ");
                this.allPeople = false;
                this.queryOfPeople = new ArrayList<>(Arrays.asList(people));
            }
        //формирование ответного сообщения с учетом возможной даты и запрошенных пользователей
            if (isDate && allPeople) {
                this.msg = "Статистика всех пользователей за " + getFormat().format(date);
            } else if (!isDate && allPeople) {
                this.msg = "Статистика всех пользователей";
            } else if (isDate && !allPeople) {
                String peopleInString = "";
                for (String currentMan : queryOfPeople) {
                    peopleInString += currentMan + ", ";
                }
                this.msg = "Статистика пользователей: " + peopleInString.substring(0, peopleInString.length() - 2) + " за " + getFormat().format(date);
            } else {
                String peopleInString = "";
                for (String currentMan : queryOfPeople) {
                    peopleInString += currentMan + ", ";
                }
                this.msg = "Статистика пользователей: " + peopleInString.substring(0, peopleInString.length() - 2);
            }


    }

    static DateTimeFormatter getFormat() {
        DateTimeFormatter format = threadFormat.get();
        if (format == null) {
            format =  DateTimeFormatter.ofPattern("d/M/yyyy");
            threadFormat.set(format);
        }
        return format;
    }

    String getMsg() {
        return msg;
    }

    LocalDate getDate() {
        return date;
    }

    List<String> getQueryOfPeople() {
        return queryOfPeople;
    }

    boolean isAllPeople() {
        return allPeople;
    }

    boolean isDate() {
        return isDate;
    }
}
