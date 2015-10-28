package ru.mirea.oop.practice.coursej.s131226.parser.parsers;


import java.util.List;


public interface Parser {


    List<String> parseLinks();

    Prices parsePrices();

    String getName();

}
