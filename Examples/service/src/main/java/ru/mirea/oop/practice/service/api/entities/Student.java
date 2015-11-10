package ru.mirea.oop.practice.service.api.entities;

public final class Student implements Comparable<Student> {
    public final int id;
    public final String lastName;
    public final String firstName;
    public final String middleName;
    public final int number;
    public final String group;
    public final String subject;
    public final String note;

    public Student(int id, String lastName, String firstName, String middleName,
                   int number, String group, String subject, String note) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.number = number;
        this.group = group;
        this.subject = subject;
        this.note = note;
    }

    @Override
    public String toString() {
        return lastName + " " + firstName + " " + middleName + "[" + group + "]";
    }

    @Override
    public int compareTo(Student o) {
        return lastName.compareTo(o.lastName);
    }
}
