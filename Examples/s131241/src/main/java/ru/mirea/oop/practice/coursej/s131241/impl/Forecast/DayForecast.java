package ru.mirea.oop.practice.coursej.s131241.impl.Forecast;

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

    public long getDt() {
        return dt;
    }

    public Temp getTemp() {
        return temp;
    }

    public double getPressure() {
        return pressure;
    }

    public double getHumidity() {
        return humidity;
    }

    public Weather[] getWeather() {
        return weather;
    }

    public double getSpeed() {
        return speed;
    }

    public int getDeg() {
        return deg;
    }

    public int getClouds() {
        return clouds;
    }

    public double getRain() {
        return rain;
    }

    public double getSnow() {
        return snow;
    }

}
