package ru.mirea.oop.practice.coursej.vk.entities;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public final class Counters {
    public int albums;
    public int videos;
    public int audios;
    public int notes;
    public int photos;
    public int groups;
    public int gifts;
    public int friends;
    @SerializedName("online_friends")
    public int onlineFriends;
    @SerializedName("user_photos")
    public int userPhotos;
    @SerializedName("user_videos")
    public int userVideos;
    public int followers;
    public int subscriptions;
    public int pages;
}
