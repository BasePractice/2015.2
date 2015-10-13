package ru.mirea.oop.practice.coursej.tg.entities;

import com.google.gson.annotations.SerializedName;

public abstract class Reply {
    @SerializedName("selective")
    public Boolean selective;
}
