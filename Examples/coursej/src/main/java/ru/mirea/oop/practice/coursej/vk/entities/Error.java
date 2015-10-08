package ru.mirea.oop.practice.coursej.vk.entities;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public final class Error {
    @SerializedName("error_code")
    public int errorCode;
    @SerializedName("error_msg")
    public String errorMessage;
    @SerializedName("request_params")
    public Map<String, String> requestParams[];
}
