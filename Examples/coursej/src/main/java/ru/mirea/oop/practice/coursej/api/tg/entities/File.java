package ru.mirea.oop.practice.coursej.api.tg.entities;

import com.google.gson.annotations.SerializedName;
import lombok.ToString;

@ToString
public class File {
    @SerializedName("file_id")
    public String id;
    @SerializedName("file_size")
    public Integer size;
}
