package ru.mirea.oop.practice.coursej.s131226.entities;

import java.sql.Date;
import java.util.List;

public class Report {
    private String shopName;
    private Date date;
    private List<ReportItem> items;

    public Report(String shopName, Date date, List<ReportItem> items) {
        this.shopName = shopName;
        this.date = date;
        this.items = items;
    }

    public void add(ReportItem reportItem) {
        items.add(reportItem);
    }

    public String getShopName() {
        return shopName;
    }

    public Date getDate() {
        return date;
    }

    public List<ReportItem> getItems() {
        return items;
    }
}
