package ru.mirea.oop.practice.coursej.api.vk.entities;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public final class Audio {
    @SerializedName("id")
    public long id;
    @SerializedName("owner_id")
    public long idOwner;
    public String artist;
    public String title;
    public int duration;
    public String url;
    @SerializedName("lyrics_id")
    public long idLyrics;
    @SerializedName("genre")
    public Genre genre;

    public enum Genre {
        @SerializedName("1")
        ROCK(1),
        @SerializedName("2")
        POP(2),
        @SerializedName("3")
        RAP_AND_HIP_HOP(3),
        @SerializedName("5")
        EASY_LISTENING(4),
        @SerializedName("6")
        DANCE_AND_HOUSE(5),
        @SerializedName("7")
        INSTRUMENTAL(6),
        @SerializedName("8")
        METAL(7),
        @SerializedName("21")
        ALTERNATIVE(21),
        @SerializedName("8")
        DUBSTEP(8),
        @SerializedName("1001")
        JAZZ_AND_BLUES(1001),
        @SerializedName("10")
        DRUM_AND_BASS(10),
        @SerializedName("11")
        TRANCE(11),
        @SerializedName("12")
        CHANSON(12),
        @SerializedName("13")
        ETHNIC(13),
        @SerializedName("14")
        ACOUSTIC_AND_VOCAL(14),
        @SerializedName("15")
        REGGAE(15),
        @SerializedName("16")
        CLASSICAL(16),
        @SerializedName("17")
        INDIE_POP(17),
        @SerializedName("19")
        SPEACH(19),
        @SerializedName("22")
        ELECTROPOP_AND_DISCO(22),
        @SerializedName("18")
        OTHER(18);

        Genre(int id) {
            this.id = id;
        }

        public static Genre valueOf(int id) {
            for (Genre genre : values()) {
                if (genre.id == id)
                    return genre;
            }
            throw new IllegalArgumentException();
        }

        private final int id;
    }
}
