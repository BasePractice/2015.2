package ru.mirea.oop.practice.coursej.api.tg.entities;

import com.google.gson.annotations.SerializedName;
import lombok.ToString;

@ToString
public final class Contact {
    @SerializedName("user_id")
    public Integer id;
    @SerializedName("first_name")
    public String firstName;
    @SerializedName("last_name")
    public String lastName;
    @SerializedName("phone_number")
    public String number;
}
