package ru.mirea.oop.practice.coursej.api.tg.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public final class UserProfilePhotos {
    @SerializedName("total_count")
    public Integer count;
    @SerializedName("photos")
    public List<List<PhotoSize>> photos;
}
