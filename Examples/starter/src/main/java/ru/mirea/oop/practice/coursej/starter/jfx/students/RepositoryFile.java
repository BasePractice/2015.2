package ru.mirea.oop.practice.coursej.starter.jfx.students;

import com.google.common.base.Charsets;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public final class RepositoryFile implements Repository<Student> {
    private final ObservableList<Student> students = FXCollections.observableArrayList();

    public RepositoryFile(InputStream stream) {
        update(stream);
    }

    public ObservableList<Student> list() {
        return students;
    }

    private void update(InputStream stream) {
        students.clear();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, Charsets.UTF_8))) {
            reader.readLine();
            reader.lines().forEach((line) -> {
                final String trimmed = line.trim();
                if (!trimmed.isEmpty()) {
                    final String[] split = line.split(",");
                    if (split.length == 7) {
                        students.add(new Student(
                                split[0].trim(),
                                split[1].trim(),
                                split[2].trim(),
                                split[3].trim(),
                                split[4].trim(),
                                split[5].trim(),
                                split[6].trim()
                        ));
                    }
                }
            });
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
