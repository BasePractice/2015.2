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
        // не актуально, но пусть будет
        // "Если в Вашем приложении используется прямая авторизация, возвращается дополнительное поле files,..."
        // У нас не используется
        @SerializedName("mp4_240")
        public String mp4p240;
        @SerializedName("mp4_360")
        public String mp4p360;
        @SerializedName("mp4_480")
        public String mp4p480;
        @SerializedName("mp4_720")
        public String mp4p720;
        @SerializedName("mp4_1080")
        public String mp4p1080;
        public String external;
    }

}
