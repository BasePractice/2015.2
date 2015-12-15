package ru.mirea.oop.practice.coursej.s131241.impl;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import ru.mirea.oop.practice.coursej.s131241.OpenWeatherMapApi;
import ru.mirea.oop.practice.coursej.s131241.impl.Forecast.Forecast;

import java.io.IOException;

public class OpenWeatherMapApiImpl implements OpenWeatherMapApi {

    @Override
    public Forecast getForecast(String cityName) {
        String url = "http://api.openweathermap.org/data/2.5/forecast/daily?q=" + cityName + "&mode=json&units=metric&cnt=7&lang=ru&appid=2de143494c0b295cca9337e1e96b00e0";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Forecast forecast = null;
        try {
            Response response = client.newCall(request).execute();
            forecast = new Gson().fromJson(response.body().string(), Forecast.class);
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return forecast;
    }
}
