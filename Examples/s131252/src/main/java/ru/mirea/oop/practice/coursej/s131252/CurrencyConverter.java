package ru.mirea.oop.practice.coursej.s131252;

import ru.mirea.oop.practice.coursej.impl.vk.ext.ServiceBotsExtension;

public class CurrencyConverter extends ServiceBotsExtension {

    public CurrencyConverter() throws Exception {
        super("vk.services.CurrencyConverter");
    }

    @Override
    protected void doEvent(Event event) {

    }

    @Override
    public String description() {
        return null;
    }
}
