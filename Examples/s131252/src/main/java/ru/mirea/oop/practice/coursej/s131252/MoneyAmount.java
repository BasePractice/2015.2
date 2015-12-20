package ru.mirea.oop.practice.coursej.s131252;

final class MoneyAmount {
    private final Currency currency;
    private final double amount;

    MoneyAmount(Currency currency, double amount) {
        this.currency = currency;
        this.amount = amount;
    }

    MoneyAmount convertTo(Currency newCurrency) {
        return new MoneyAmount(newCurrency, amount * this.currency.value / newCurrency.value);
    }

    @Override
    public String toString() {
        return String.format("%.2f", amount) + " " + currency.code;
    }

}
