package ru.mirea.oop.practice.coursej.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface RowConverter<E> {
    E conver(ResultSet rs) throws SQLException;
}
