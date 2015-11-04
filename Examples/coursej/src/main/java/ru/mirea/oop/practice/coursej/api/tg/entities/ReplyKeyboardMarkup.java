package ru.mirea.oop.practice.coursej.api.tg.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public final class ReplyKeyboardMarkup extends Reply {
    @SerializedName("keyboard")
    public List<List<String>> keyboard;
    @SerializedName("resize_keyboard")
    public Boolean resizeKeyboard;
    @SerializedName("one_time_keyboard")
    public Boolean oneTimeKeyboard;
}
