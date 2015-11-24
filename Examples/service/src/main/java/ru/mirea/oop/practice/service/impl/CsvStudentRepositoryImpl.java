package ru.mirea.oop.practice.service.impl;

import ru.mirea.oop.practice.service.api.StudentsRepository;
import ru.mirea.oop.practice.service.api.entities.Student;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

final class CsvStudentRepositoryImpl implements StudentsRepository {
    @Override
    public List<Student> list() {
        return getStudents();
    }

    private static List<Student> getStudents() {
        return StudentsHolder.students;
    }

    @Override
    public Iterator<Student> iterator() {
        return list().iterator();
    }

    private static final class StudentsHolder {
        private static final List<Student> students = new ArrayList<>();

        static {
            final int[] i = {0};
            try (BufferedReader reader =
                         new BufferedReader(
                                 new InputStreamReader(
                                         CsvStudentRepositoryImpl.class.getResourceAsStream("/.students.csv")))) {
                reader.readLine();
                reader.lines().forEach((line) -> {
                    if (line.isEmpty())
                        return;
                    String[] split = line.split(",");
                    String number = split[3].trim();
                    students.add(new Student(
                            i[0]++,
                            split[0].trim(),
                            split[1].trim(),
                            split[2].trim(),
                            Integer.parseInt(number.contains("-") ? "-1" : number),
                            split[4].trim(),
                            split[5].trim(),
                            split[6].trim()

                    ));
                });
                Collections.sort(students);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
