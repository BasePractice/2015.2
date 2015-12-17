package ru.mirea.oop.practice.coursej.api.vk.entities;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public final class Album {
    @SerializedName("aid")
    public String id;
    @SerializedName("thumb_id")
    public long idThumb;
    @SerializedName("owner_id")
    public long idOwner;
    public String title;
    public String description;
    public long created;
    public long updated;
    public long size;
    @SerializedName("can_upload")
    public int canUpload;
    @SerializedName("privacy_view")
    public Privacy privacyView;
    @SerializedName("privacy_comment")
    public Privacy privacyComment;

    public static final class Privacy {
        public String type;
        public String[] lists;
        public String[] users;
    }
}
