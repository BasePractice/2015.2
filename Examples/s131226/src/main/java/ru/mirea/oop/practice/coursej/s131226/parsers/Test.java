package ru.mirea.oop.practice.coursej.s131226.parsers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mirea.oop.practice.coursej.s131226.Parser;
import ru.mirea.oop.practice.coursej.s131226.ParserThread;
import ru.mirea.oop.practice.coursej.s131226.helper.DbHelper;
import ru.mirea.oop.practice.coursej.s131226.updater.DBUpdaterImpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Test {

    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(DBUpdaterImpl.class);
        Date date = new Date();
        DbHelper myDbHelper = new DbHelper();
        Parser parser = new KupitfissmanParser();
        parser.parsePrices();

//        try {
//            myDbHelper.conn();
//        } catch (ClassNotFoundException | SQLException e) {
//            e.printStackTrace();
//        }
//        List<Parser> parsers = new ArrayList<>();
//        ParserCollections.setParsers(parsers);
//        List<ParserThread> threads = new ArrayList<>();
//        List<Prices> pricesList = Collections.synchronizedList(new ArrayList<>());
//        for (Parser parser : parsers) {
//            ParserThread thread = new ParserThread(parser, pricesList);
//            thread.start();
//            threads.add(thread);
//        }
//        for (ParserThread thread : threads) {
//            logger.debug("Ожидается завершение парсинга" + thread.parser.getName());
//            try {
//                thread.join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            logger.debug("Парсинг " + thread.parser.getName() + " завершен");
//        }
//        try {
//            myDbHelper.writeSumDB(pricesList);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        logger.debug("запись завершена");
//        Date finish = new Date();
//        logger.debug("Время работы программы(сек):" + ((double) (((finish.getTime() - date.getTime())) / 100) / 10));
    }
}
