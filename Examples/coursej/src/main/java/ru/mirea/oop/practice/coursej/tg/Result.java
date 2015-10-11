package ru.mirea.oop.practice.coursej.tg;

import com.google.gson.annotations.SerializedName;

public final class Result<E> {
    @SerializedName("ok")
    public Boolean ok;
    @SerializedName("result")
    public E result;
}
