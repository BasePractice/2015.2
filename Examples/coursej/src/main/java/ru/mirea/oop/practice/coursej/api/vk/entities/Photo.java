package ru.mirea.oop.practice.coursej.api.vk.entities;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public final class Photo {
    @SerializedName("pid")
//    @SerializedName("id")
    public long id;
    @SerializedName("aid")
//    @SerializedName("album_id")
    public long idAlbum;
    @SerializedName("owner_id")
    public long idOwner;
    @SerializedName("user_id")
    public long idUser;
    public String text;
    /**
     * NOTICE: unixtime
     */
    @SerializedName("created")
    public long date;
    public long width;
    public long height;
    @SerializedName("src_small")
//    @SerializedName("photo_75")
    public String urlPhoto0075;
    @SerializedName("photo_130")
    public String urlPhoto0130;
    @SerializedName("photo_604")
    public String urlPhoto0604;
    @SerializedName("src_xbig")
//    @SerializedName("photo_807")
    public String urlPhoto0807;
    @SerializedName("src_xxbig")
//    @SerializedName("photo_1280")
    public String urlPhoto1280;
    @SerializedName("src_xxxbig")
//    @SerializedName("photo_2560")
    public String urlPhoto2560;
}
