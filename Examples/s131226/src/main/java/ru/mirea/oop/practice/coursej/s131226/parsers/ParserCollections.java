package ru.mirea.oop.practice.coursej.s131226.parsers;

import ru.mirea.oop.practice.coursej.s131226.Parser;

import java.util.List;

public final class ParserCollections {

    public static void setParsers(List<Parser> parsers) {
        parsers.clear();
        parsers.add(new FissmanPosudaParser());
        parsers.add(new ChashkaLozhkaPraser());
        parsers.add(new Fissman4youParser());
        parsers.add(new FissmanGroupParser());
        parsers.add(new FissmanInfoParser());
        parsers.add(new FissmanNetParser());
       // parsers.add(new MakedonMarketParser()); сайт не работает
        parsers.add(new PosudaProfParser());
        parsers.add(new FismartParser());
        parsers.add(new Superpovar());
        parsers.add(new KazanchikParser());
        parsers.add(new KupitfissmanParser());
    }
}
