package ru.mirea.oop.practice.service;

import ru.mirea.oop.practice.service.api.StudentsRepository;
import ru.mirea.oop.practice.service.api.entities.Student;
import ru.mirea.oop.practice.service.impl.RepositoryFactory;

public final class Main {
    public static void main(String[] args) {
        RepositoryFactory factory = new RepositoryFactory();
        StudentsRepository students = factory.createStudents();
        for (Student student: students) {
            System.out.println(student);
        }
    }
}
