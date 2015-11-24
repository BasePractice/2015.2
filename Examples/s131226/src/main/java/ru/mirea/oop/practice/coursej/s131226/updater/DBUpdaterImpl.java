package ru.mirea.oop.practice.coursej.s131226.updater;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mirea.oop.practice.coursej.s131226.DBUpdater;
import ru.mirea.oop.practice.coursej.s131226.Parser;
import ru.mirea.oop.practice.coursej.s131226.ParserThread;
import ru.mirea.oop.practice.coursej.s131226.helper.DbHelper;
import ru.mirea.oop.practice.coursej.s131226.parsers.ParserCollections;
import ru.mirea.oop.practice.coursej.s131226.parsers.Prices;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DBUpdaterImpl implements DBUpdater {
    private static final Logger logger = LoggerFactory.getLogger(DBUpdaterImpl.class);

    @Override
    public void updateDB() {
        Date date = new Date();
        DbHelper myDbHelper = new DbHelper();
        try {
            myDbHelper.conn();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        List<Parser> parsers = new ArrayList<>();
        ParserCollections.setParsers(parsers);
        List<ParserThread> threads = new ArrayList<>();
        List<Prices> pricesList = Collections.synchronizedList(new ArrayList<>());
        /**
         * FIXME: Много потоков. Лучше что-то типа ExecutorService

         ExecutorService service = Executors.newFixedThreadPool(5);
         List<CompletableFuture<Prices>> futures = new ArrayList<>();
         for (Parser parser : parsers) {
             CompletableFuture<Prices> future = CompletableFuture.supplyAsync(parser::parsePrices, service);
             futures.add(future);
         }
         service.shutdown();
         for (CompletableFuture<Prices> future: futures) {
             try {
                 pricesList.add(future.get());
             } catch (Exception e) {
                 e.printStackTrace();
             }
         }

         FIXME: Можно и при помощи Streams
         pricesList = parsers.stream().parallel().map(Parser::parsePrices).collect(Collectors.toList());

         */
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
            myDbHelper.writeSumDB(pricesList);
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
