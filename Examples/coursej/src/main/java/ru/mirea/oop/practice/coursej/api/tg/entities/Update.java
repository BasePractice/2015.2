package ru.mirea.oop.practice.coursej.api.tg.entities;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public final class Update {
    @SerializedName("update_id")
    public Integer id;
    @SerializedName("message")
    public Message message;
}
