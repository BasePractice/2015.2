package ru.mirea.oop.practice.coursej.api.vk.entities;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public final class AudioLyrics {
    @SerializedName("lyrics_id")
    public long idLyrics;
    public String text;
}
