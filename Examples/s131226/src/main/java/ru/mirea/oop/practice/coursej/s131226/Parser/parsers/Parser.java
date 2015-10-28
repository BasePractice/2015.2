package ru.mirea.oop.practice.coursej.s131226.parser.parsers;


import java.util.ArrayList;


public interface Parser {


    ArrayList parseLinks();

    Prices parsePrices();

    String getName();

}
