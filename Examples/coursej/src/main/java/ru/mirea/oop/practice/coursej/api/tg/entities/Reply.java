package ru.mirea.oop.practice.coursej.api.tg.entities;

import com.google.gson.annotations.SerializedName;

public abstract class Reply {
    @SerializedName("selective")
    public Boolean selective;
}
