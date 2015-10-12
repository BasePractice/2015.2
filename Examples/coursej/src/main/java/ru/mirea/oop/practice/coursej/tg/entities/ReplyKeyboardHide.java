package ru.mirea.oop.practice.coursej.tg.entities;

import com.google.gson.annotations.SerializedName;

public final class ReplyKeyboardHide extends Reply {
    @SerializedName("hide_keyboard")
    public final Boolean hide = Boolean.TRUE;
}
