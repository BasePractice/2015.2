package ru.mirea.oop.practice.coursej.starter.jfx.students;

import java.util.Map;

public final class Student {
    public final String firstName;
    public final String secondName;
    public final String lastName;
    public final String number;
    public final String group;
    public final String title;
    public final String description;

    public Student(String firstName,
                   String secondName,
                   String lastName,
                   String number,
                   String group,
                   String title,
                   String description) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.lastName = lastName;
        this.number = number;
        this.group = group;
        this.title = title;
        this.description = description;
    }

    public static void createHeaders(Map<String, String> headers) {
        headers.clear();
        headers.put("firstName", "Фамилия");
        headers.put("secondName", "Имя");
        headers.put("lastName", "Отчество");
        headers.put("number", "Номер");
        headers.put("group", "Группа");
        headers.put("title", "Задание");
        headers.put("description", "Примечание");
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getNumber() {
        return number;
    }

    public String getGroup() {
        return group;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
