package ru.mirea.oop.practice.coursej.api.vk.entities;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public final class Video {
    @SerializedName("vid")
    public long id;
    @SerializedName("owner_id")
    public long idOwner;
    public String title;
    public int duration;
    public String link;
    public String image;
    @SerializedName("image_medium")
    public String imageMedium;
    public long date;
    public String player;
    public Files files;
    public int comments;

    public class Files {
        public String mp4_240;
        public String mp4_360;
        public String mp4_480;
        public String mp4_720;
        public String mp4_1080;
        public String external;
    }

}
