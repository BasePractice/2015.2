package ru.mirea.oop.practice.coursej.s131241.impl.Forecast;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class Forecast {
    public static final String MESSAGE = "Прогноз на ближайшие 7 дней: \n";
    public static final long DATE_CORRECTOR = 1000L;
    private City city;
    private String cod;
    private double message;
    private int cnt;
    private DayForecast[] list;

    public City getCity() {
        return city;
    }

    public String getCod() {
        return cod;
    }

    public double getMessage() {
        return message;
    }

    public int getCnt() {
        return cnt;
    }

    public DayForecast[] getList() {
        return list;
    }

    public String getInlineForecast() {
        StringBuilder builder = new StringBuilder();
        builder.append("(").append(city.getName()).append(")\n");
        builder.append(MESSAGE);
        for (DayForecast dayForecast : list) {
            builder.append(new SimpleDateFormat("dd.MM.yy").format(new Date(dayForecast.getDt() * DATE_CORRECTOR)))
                    .append(": T=")
                    .append(dayForecast.getTemp().getDay())
                    .append(", ")
//                  .append("weather=")
//                  .append(dayForecast.getWeather()[0].getId())
                    .append(dayForecast.getWeather()[0].getDescription())
                    .append("\n");
        }

        return builder.toString();
    }
}
