package ru.mirea.oop.practice.coursej.s131241;

import ru.mirea.oop.practice.coursej.s131241.impl.Forecast.Forecast;

public interface OpenWeatherMapApi {
    Forecast getForecast(String cityName);
}
