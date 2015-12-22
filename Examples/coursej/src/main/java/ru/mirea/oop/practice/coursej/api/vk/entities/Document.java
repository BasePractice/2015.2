package ru.mirea.oop.practice.coursej.api.vk.entities;

import com.google.gson.annotations.SerializedName;

public final class Document {
    @SerializedName("did")
    public long id;
    @SerializedName("owner_id")
    public long idOwner;
    public String title;
    public int size;
    public String ext;
    public String url;
}
