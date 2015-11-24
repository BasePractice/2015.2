package ru.mirea.oop.practice.coursej.s131226.parsers;

import java.util.Map;

public class Prices {
    private String sitename;
    private Map<Integer, Integer> pricesMap;

    public Prices(String sitename, Map<Integer, Integer> pricesMap) {
        this.sitename = sitename;
        this.pricesMap = pricesMap;
    }

    public String getSitename() {
        return sitename;
    }

    public Map<Integer, Integer> getPricesMap() {
        return pricesMap;
    }

    public void addValue(Integer key, Integer value) {
        pricesMap.put(key, value);
    }

    public void remove(Integer key) {
        pricesMap.remove(key);
    }
}
