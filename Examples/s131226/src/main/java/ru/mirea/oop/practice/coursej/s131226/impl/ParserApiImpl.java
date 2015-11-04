package ru.mirea.oop.practice.coursej.s131226.impl;


import ru.mirea.oop.practice.coursej.s131226.DBUpdater;
import ru.mirea.oop.practice.coursej.s131226.ParserApi;
import ru.mirea.oop.practice.coursej.s131226.helper.DbHelper;
import ru.mirea.oop.practice.coursej.s131226.helper.ExcelHelper;
import ru.mirea.oop.practice.coursej.s131226.parsers.Prices;
import ru.mirea.oop.practice.coursej.s131226.updater.DBUpdaterImpl;

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
            List<String> tables = dbHelper.getDBState();
            dbHelper.closeDB();
            String dbState = "На данный момент в базе содержатся следующие таблицы: \n";
            for (String table : tables) {
                dbState += table + "\n";

            }
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
    public String getChanges() {
        try {
            List<String> tables = dbHelper.getDBState();
            List<List<Prices>> list = new ArrayList<>();
            for (String table : tables) {
                list.add(dbHelper.getDifferences(table));
            }
            String[] siteNames = new String[list.get(0).size()];
            for (int i = 0; i <list.get(0).size() ; i++) {
                siteNames[i] = list.get(0).get(i).getSitename();

            }
            String[] changes = new String[siteNames.length];
            int i = 0;
            for (String siteName : siteNames) {
                changes[i] =siteName+": ";
                for (List<Prices> list1 : list) {
                    boolean contains=false;
                    for (Prices prices : list1) {
                        if (prices.getSitename().equals(siteName)) {
                            changes[i]+="->"+prices.getPricesMap().size();
                            contains = true;
                        }
                    }
                    if (!contains) {
                        changes[i]+="->нет";
                    }
                }
                i++;
            }
            String allChanges = "Изменения \n";
            for (String tabel : tables) {
                allChanges +="->"+ tabel;

            }
            allChanges += "\n для fissmanposuda приведено общее количество товаров \n";
            for (String s : changes) {
                allChanges += s + "\n";
            }
            return allChanges;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
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
