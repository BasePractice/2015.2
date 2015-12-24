package ru.mirea.oop.practice.coursej.s131226;

import ru.mirea.oop.practice.coursej.s131226.entities.Report;
import ru.mirea.oop.practice.coursej.s131226.entities.Snapshot;

import java.util.List;

public interface ReportsRepository {


    void addSnapshot(Snapshot snapshot);



    List<Report> getReports();

}
