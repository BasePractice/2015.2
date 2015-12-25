package ru.mirea.oop.practice.coursej.s131226.entities;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Snapshot {
    private final String shopName;
    private final Date date;
    private List<item> items = new ArrayList<>();

    public Snapshot(String shopName) {
        this.shopName = shopName;
        java.util.Date utilDate = new java.util.Date();
        this.date = new Date(utilDate.getTime());

    }

    public String getShopName() {
        return shopName;
    }

    public void add(item item) {
        items.add(item);
    }

    public Date getDate() {
        return date;
    }

    public List<item> getItems() {
        return items;
    }
}
