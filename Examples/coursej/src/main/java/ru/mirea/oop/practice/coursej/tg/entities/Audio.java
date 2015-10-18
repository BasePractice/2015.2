package ru.mirea.oop.practice.coursej.tg.entities;

import com.google.gson.annotations.SerializedName;
import lombok.ToString;

@ToString
public final class Audio extends File {
    @SerializedName("duration")
    public Integer duration;
    @SerializedName("mime_type")
    public String mimeType;
}
