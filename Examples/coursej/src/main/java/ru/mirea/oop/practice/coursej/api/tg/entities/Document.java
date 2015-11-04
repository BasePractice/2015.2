package ru.mirea.oop.practice.coursej.api.tg.entities;

import com.google.gson.annotations.SerializedName;

public final class Document extends File {
    @SerializedName("thumb")
    public PhotoSize thumb;
    @SerializedName("file_name")
    public String fileName;
    @SerializedName("mime_type")
    public String mimeType;
}
