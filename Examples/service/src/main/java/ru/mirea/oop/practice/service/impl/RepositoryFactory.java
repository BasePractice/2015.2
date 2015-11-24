package ru.mirea.oop.practice.service.impl;

import ru.mirea.oop.practice.service.api.StudentsRepository;

public final class RepositoryFactory {
    public StudentsRepository createStudents() {
        return new CsvStudentRepositoryImpl();
    }
}
