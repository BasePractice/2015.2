package ru.mirea.oop.practice.coursej.api.vk.entities;

import com.google.gson.annotations.SerializedName;

public final class UploadServer {
    @SerializedName("upload_url")
    public String urlUpload;
    public int aid;
    public int mid;
}
