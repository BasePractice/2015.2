package ru.mirea.oop.practice.coursej.s131226.entities;

public class ReportItem extends item {
    private final int referencePrice;

    public ReportItem(int article, int price, int referencePrice) {
        super(article, price);
        this.referencePrice = referencePrice;
    }

    public int getReferencePrice() {
        return referencePrice;
    }
}
