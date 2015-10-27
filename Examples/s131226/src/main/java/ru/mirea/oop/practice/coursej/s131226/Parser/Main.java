package ru.mirea.oop.practice.coursej.s131226.Parser;


import ru.mirea.oop.practice.coursej.s131226.Parser.DBUpdater.ParserThread;
import ru.mirea.oop.practice.coursej.s131226.Parser.data.DbHelper;
import ru.mirea.oop.practice.coursej.s131226.Parser.parsers.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class Main {
    public static void main(String[] args) throws Exception {
        Date date = new Date();
        DbHelper myDbHelper = new DbHelper();
        myDbHelper.Conn();
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
            System.out.println(thread.parser.getClass().getName() + " ожидается в данный момент");
            thread.join();
        }
        System.out.println("запись в таблицу и экспорт в эксель");
        myDbHelper.WriteSumDB(pricesList);
        System.out.println("запись завершена");
        Date finish = new Date();
        System.out.println("Время работы программы(сек):" + ((double) (((finish.getTime() - date.getTime())) / 100) / 10));

    }
}
