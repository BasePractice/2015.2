package ru.mirea.oop.practice.coursej.s131241.Forecast;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Date;

public final class DayForecast {
    private long dt;
    private Temp temp;
    private double pressure;
    private double humidity;
    private Weather[] weather;
    private double speed;
    private int deg;
    private int clouds;
    private double rain;
    private double snow;

}
