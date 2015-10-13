package ru.mirea.oop.practice.coursej.tg.entities;

import com.google.gson.annotations.SerializedName;

public final class Location {
    @SerializedName("longitude")
    public Float longitude;
    @SerializedName("latitude")
    public Float latitude;
}
