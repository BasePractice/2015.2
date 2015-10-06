package ru.mirea.oop.practice.coursej.vk.entities;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public final class Military {
    public String unit;
    @SerializedName("unit_id")
    public long idUnit;
    @SerializedName("country_id")
    public long idCountry;
    public int from;
    public int until;
}
