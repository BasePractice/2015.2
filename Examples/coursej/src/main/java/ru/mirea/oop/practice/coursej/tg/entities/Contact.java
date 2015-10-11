package ru.mirea.oop.practice.coursej.tg.entities;

import com.google.gson.annotations.SerializedName;

public final class Contact {
    @SerializedName("user_id")
    public String id;
    @SerializedName("first_name")
    public String firstName;
    @SerializedName("last_name")
    public String lastName;
    @SerializedName("phone_number")
    public String number;
}
