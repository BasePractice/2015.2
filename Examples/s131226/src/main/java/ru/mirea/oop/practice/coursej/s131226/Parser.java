package ru.mirea.oop.practice.coursej.s131226;


import ru.mirea.oop.practice.coursej.s131226.parsers.Prices;

import java.util.List;


public interface Parser {


    List<String> parseLinks();

    Prices parsePrices();

    String getName();

}
