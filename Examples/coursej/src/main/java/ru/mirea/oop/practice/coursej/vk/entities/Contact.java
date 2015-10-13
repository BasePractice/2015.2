package ru.mirea.oop.practice.coursej.vk.entities;


import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public final class Contact {
    public long uid;
    @SerializedName("first_name")
    public String firstName;
    @SerializedName("last_name")
    public String lastName;
    public String domain;
    @SerializedName("screen_name")
    public String screenName;
    public boolean verified;
    @SerializedName("photo_max_orig")
    public String photo;
    public String deactivated;
    public boolean hidden;
    public int online;
    public int sex;
    public long[] lists;
    public int city;
    public int country;
    public Military[] military;
    public String about;
    @SerializedName("mobile_phone")
    public String mobilePhone;
    @SerializedName("home_phone")
    public String homePhone;
    public Counters counters;
    @SerializedName("common_count")
    public long commonCount;
    public String status;
    public String site;
    public int timezon;
}
