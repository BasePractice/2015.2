package ru.mirea.oop.practice.coursej.s131226.Parser;


import ru.mirea.oop.practice.coursej.s131226.Parser.DBUpdater.DBUpdater;
import ru.mirea.oop.practice.coursej.s131226.Parser.DBUpdater.DBUpdaterImpl;
import ru.mirea.oop.practice.coursej.s131226.Parser.data.DbHelper;
import ru.mirea.oop.practice.coursej.s131226.Parser.data.ExcelHelper;

import java.io.File;
import java.sql.SQLException;

public class ParserApiImpl implements ParserApi {
    private DbHelper dbHelper;
    private ExcelHelper excelHelper;

    public ParserApiImpl() {

        this.dbHelper = new DbHelper();
        this.excelHelper = new ExcelHelper(dbHelper);
    }

    @Override
    public String getDBState() {
        try {
            dbHelper.Conn();
            String dbState = dbHelper.getDBState();
            dbHelper.CloseDB();
            return dbState;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public File getReport() {
        return excelHelper.exportReport();
    }

    @Override
    public File getChanges() {
        return excelHelper.exportChanges();
    }

    @Override
    public String getShortReport() {
        return null;
    }

    @Override
    public String getShortChanges() {
        return null;

    }

    @Override
    public synchronized void updateDB() throws InterruptedException {
        DBUpdater updater = new DBUpdaterImpl();
        Thread update = new Thread(updater);
        update.start();
        update.join();
    }
}
