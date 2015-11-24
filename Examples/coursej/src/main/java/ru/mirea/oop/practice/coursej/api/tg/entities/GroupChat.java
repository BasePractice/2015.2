package ru.mirea.oop.practice.coursej.api.tg.entities;

import com.google.gson.annotations.SerializedName;
import lombok.ToString;

@ToString
public final class GroupChat extends User {
    @SerializedName("title")
    public String title;
}
