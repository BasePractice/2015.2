package ru.mirea.oop.practice.coursej.api.vk.entities;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public final class LongPollData {
    public String key;
    public String server;
    @SerializedName("ts")
    public long lastEvent;
    public List<List<Object>> updates;
    public String failed;
}
