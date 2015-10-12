package ru.mirea.oop.practice.coursej.tg.entities;

import com.google.gson.annotations.SerializedName;

public final class Video extends File {
    @SerializedName("width")
    public Integer width;
    @SerializedName("height")
    public Integer hwight;
    @SerializedName("thumb")
    public PhotoSize thumb;
    @SerializedName("duration")
    public Integer duration;
    @SerializedName("mime_type")
    public String mimeType;
    @SerializedName("caption")
    public String caption;
}
