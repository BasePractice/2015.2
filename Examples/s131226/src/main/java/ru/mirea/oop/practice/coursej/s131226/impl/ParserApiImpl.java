package ru.mirea.oop.practice.coursej.s131226.impl;


import ru.mirea.oop.practice.coursej.s131226.DBUpdater;
import ru.mirea.oop.practice.coursej.s131226.ParserApi;
import ru.mirea.oop.practice.coursej.s131226.parsers.Prices;
import ru.mirea.oop.practice.coursej.s131226.updater.DBUpdaterImpl;
import ru.mirea.oop.practice.coursej.s131226.helper.DbHelper;
import ru.mirea.oop.practice.coursej.s131226.helper.ExcelHelper;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
            dbHelper.conn();
            String dbState = dbHelper.getDBState();
            dbHelper.closeDB();
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
    public String getShortReport(){
        List<Prices> differences = new ArrayList<>();
        try {
            differences = dbHelper.getDifferences();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        differences.remove(0);
        String shortReport="";
        for (Prices prices : differences) {
            shortReport += prices.getSitename() + " цен ниже нормы:" + prices.getPricesMap().size() + "\n";
        }
        return shortReport;
    }


    @Override
    public synchronized void updateDB() {
        DBUpdater updater = new DBUpdaterImpl();
        Thread update = new Thread(updater);
        update.start();
        try {
            update.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
