package ru.mirea.oop.practice.coursej.s131226.Parser.DBUpdater;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mirea.oop.practice.coursej.s131226.Parser.data.DbHelper;
import ru.mirea.oop.practice.coursej.s131226.Parser.parsers.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class DBUpdaterImpl implements DBUpdater {
    private static final Logger logger = LoggerFactory.getLogger(DBUpdaterImpl.class);

    @Override
    public void updateDB() {
        Date date = new Date();
        DbHelper myDbHelper = new DbHelper();
        try {
            myDbHelper.Conn();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        ArrayList<Parser> parsers = new ArrayList<>();
        parsers.add(new FissmanPosudaParser());
        parsers.add(new ChashkaLozhkaPraser());
        parsers.add(new Fissman4youParser());
        parsers.add(new FissmanGroupParser());
        parsers.add(new FissmanInfoParser());
        parsers.add(new FissmanNetParser());
        parsers.add(new MakedonMarketParser());
        parsers.add(new PosudaProfParser());
        parsers.add(new FismartParser());
        parsers.add(new Superpovar());
        parsers.add(new KazanchikParser());
        parsers.add(new KupitfissmanParser());
        ArrayList<ParserThread> threads = new ArrayList<>();
        List<Prices> pricesList = Collections.synchronizedList(new ArrayList<Prices>());
        for (Parser parser : parsers) {
            ParserThread thread = new ParserThread(parser, pricesList);
            thread.start();
            threads.add(thread);
        }
        for (ParserThread thread : threads) {
            logger.debug("Ожидается завершение парсинга" + thread.parser.getName());
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            logger.debug("Парсинг " + thread.parser.getName() + " завершен");
        }
        try {
            myDbHelper.WriteSumDB(pricesList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        logger.debug("запись завершена");
        Date finish = new Date();
        logger.debug("Время работы программы(сек):" + ((double) (((finish.getTime() - date.getTime())) / 100) / 10));
    }

    @Override
    public void run() {
        this.updateDB();
    }
}
