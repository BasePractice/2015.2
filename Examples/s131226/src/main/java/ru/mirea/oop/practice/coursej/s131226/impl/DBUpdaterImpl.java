package ru.mirea.oop.practice.coursej.s131226.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mirea.oop.practice.coursej.s131226.DBUpdater;
import ru.mirea.oop.practice.coursej.s131226.ReportsRepository;
import ru.mirea.oop.practice.coursej.s131226.entities.Snapshot;
import ru.mirea.oop.practice.coursej.s131226.impl.parsers.ParserCollections;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DBUpdaterImpl implements DBUpdater {
    private static final Logger logger = LoggerFactory.getLogger(DBUpdaterImpl.class);

    @Override
    public void updateDB() {
        ReportsRepository reportsRepository = new ReportsRepositoryImpl();
        Date date = new Date();
        List<Parser> parsers = new ArrayList<>();
        ParserCollections.setParsers(parsers);
        ExecutorService service = Executors.newFixedThreadPool(8);
        List<CompletableFuture<Snapshot>> futures = new ArrayList<>();
        for (Parser parser : parsers) {
            CompletableFuture<Snapshot> future = CompletableFuture.supplyAsync(parser::parsePrices, service);
            futures.add(future);
        }
        service.shutdown();
        for (CompletableFuture<Snapshot> future : futures) {
            try {
                Snapshot snapshot = future.get();
                reportsRepository.addSnapshot(snapshot);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
        Date finish = new Date();
        double t = (finish.getTime() - date.getTime()) / 1000;
        logger.debug("Суммарное время обновления составило {} секунд.", t);
    }

    @Override
    public void run() {
        this.updateDB();
    }
}
