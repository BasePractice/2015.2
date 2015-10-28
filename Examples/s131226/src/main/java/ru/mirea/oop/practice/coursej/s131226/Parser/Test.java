package ru.mirea.oop.practice.coursej.s131226.parser;

import ru.mirea.oop.practice.coursej.s131226.parser.data.DbHelper;
import ru.mirea.oop.practice.coursej.s131226.parser.data.ExcelHelper;
import ru.mirea.oop.practice.coursej.s131226.parser.parsers.*;
import ru.mirea.oop.practice.coursej.s131226.parser.parsers.Parser;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException {
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
        List<String> sites = new ArrayList<>();
        for (Parser parser : parsers) {
            sites.add(parser.getName());
        }
        File file = new ExcelHelper(myDbHelper).exportReport();
        System.out.println(file.getAbsolutePath());
        System.out.println(file.getUsableSpace());
        System.out.println(file.isFile());
        System.out.println(file.exists());


    }
}
