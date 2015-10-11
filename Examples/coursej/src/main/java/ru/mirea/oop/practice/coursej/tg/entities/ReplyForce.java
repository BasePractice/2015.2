package ru.mirea.oop.practice.coursej.tg.entities;

import com.google.gson.annotations.SerializedName;

public  final class ReplyForce extends Reply {
    @SerializedName("force_reply")
    public final Boolean force = Boolean.TRUE;
}
