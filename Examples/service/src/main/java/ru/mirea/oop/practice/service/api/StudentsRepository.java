package ru.mirea.oop.practice.service.api;

import ru.mirea.oop.practice.service.api.entities.Student;

import java.util.List;

public interface StudentsRepository extends Iterable<Student> {
    List<Student> list();
}
