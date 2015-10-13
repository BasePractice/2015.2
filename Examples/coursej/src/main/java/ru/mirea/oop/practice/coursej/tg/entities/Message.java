package ru.mirea.oop.practice.coursej.tg.entities;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public final class Message {
    @SerializedName("message_id")
    public Integer id;
    @SerializedName("from")
    public User from;
    @SerializedName("date")
    public Integer date;
    @SerializedName("chat")
    public GroupChat chat;
    @SerializedName("forward_from")
    public User forwardFrom;
    @SerializedName("forward_date")
    public Integer forwardDate;
    @SerializedName("reply_to_message")
    public Message reply;
    @SerializedName("text")
    public String text;
    @SerializedName("audio")
    public Audio audio;
    @SerializedName("document")
    public Document document;
    @SerializedName("photo")
    public PhotoSize[] photo;
    @SerializedName("sticker")
    public Sticker sticker;
    @SerializedName("video")
    public Video video;
    @SerializedName("contact")
    public Contact contact;
    @SerializedName("location")
    public Location location;
    @SerializedName("new_chat_participant")
    public User newUserInChat;
    @SerializedName("left_chat_participant")
    public User leftUserFromChat;
    @SerializedName("new_chat_title")
    public String newChatTitle;
    @SerializedName("new_chat_photo")
    public PhotoSize[] newChatPhoto;
    @SerializedName("delete_chat_photo")
    public Boolean deleteChatPhoto;
    @SerializedName("group_chat_created")
    public Boolean groupChatPhoto;
}
