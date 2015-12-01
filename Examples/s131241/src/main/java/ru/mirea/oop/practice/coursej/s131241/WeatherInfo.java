package ru.mirea.oop.practice.coursej.s131241;

import ru.mirea.oop.practice.coursej.impl.vk.ext.ServiceBotsExtension;

public class WeatherInfo extends ServiceBotsExtension {
    public WeatherInfo() throws Exception {
        super("vk.services.WeatherInfo");
    }

    @Override
    protected void doEvent(Event event) {

    }

    @Override
    public String description() {
        return null;
    }
}
