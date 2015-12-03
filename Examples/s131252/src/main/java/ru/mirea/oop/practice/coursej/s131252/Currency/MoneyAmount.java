package ru.mirea.oop.practice.coursej.s131252.Currency;

public class MoneyAmount {
    Currency currency;
    double amount;

    public MoneyAmount(Currency currency, double amount) {
        this.currency = currency;
        this.amount = amount;
    }

    public MoneyAmount convertTo(Currency newCurrency) {
        return new MoneyAmount(newCurrency, amount * this.currency.getValue() / newCurrency.getValue());
    }

    @Override
    public String toString() {
        return String.format("%.2f", amount)+ " " + currency.getCharCode();
    }

}
