package ru.mirea.oop.practice.coursej.s131226.entities;

import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

public class Snapshot {
    private final String shopName;
    private final Date date= getDate();
    private List<Item> items= new ArrayList<>();

    public Snapshot(String shopName) {
        this.shopName = shopName;
    }

    public String getShopName() {
        return shopName;
    }

    public void add(Item item) {
        items.add(item);
    }

    public Date getDate() {
        return date;
    }

    public List<Item> getItems() {
        return items;
    }
}
