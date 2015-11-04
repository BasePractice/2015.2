package ru.mirea.oop.practice.coursej.s131226;


import ru.mirea.oop.practice.coursej.s131226.parsers.Prices;

import java.util.Date;
import java.util.List;

public class ParserThread extends Thread {
    public Parser parser;
    public List<Prices> pricesList;

    public ParserThread(Parser parser, List<Prices> pricesList) {
        this.parser = parser;
        this.pricesList = pricesList;
    }


    @Override
    public void run() {
        Date date = new Date();
        System.out.println("Парсер запущен" + parser.getClass().getName());
        Prices prices = parser.parsePrices();
        System.out.println("Парсер добавляется в массив" + parser.getClass().getName());
        pricesList.add(prices);
        Date date2 = new Date();
        System.out.println("Парсер отработал " + parser.getClass().toString() + "  время работы " + ((double) ((date2.getTime() - date.getTime()) / 100) / 10));
    }

}
