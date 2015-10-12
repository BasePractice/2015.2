package ru.mirea.oop.practice.coursej.tg.entities;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("id")
    public Integer id;
    @SerializedName("first_name")
    public String firstName;
    @SerializedName("last_name")
    public String lastName;
    @SerializedName("username")
    public String username;
}
