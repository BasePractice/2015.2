package ru.mirea.oop.practice.coursej.tg;

import retrofit.http.*;
import ru.mirea.oop.practice.coursej.tg.entities.*;

import java.io.IOException;
import java.io.InputStream;

public interface BotApi {
    @GET("/getMe")
    Result<User> getMe() throws IOException;

    @GET("/getUpdates")
    Result<Update[]> getUpdates(@Query("offset") Integer offset,
                                @Query("limit") Integer limit,
                                @Query("timeout") Integer timeout) throws IOException;

    @Multipart
    @POST("/sendDocument")
    Result<Message> sendDocument(@Part("chat_id") Integer id,
                                 @Part("reply_to_message_id") Integer replyMessage,
                                 @Part("reply_markup") Reply reply,
                                 @Part("document") InputStream stream) throws IOException;

    @Multipart
    @POST("/sendMessage")
    Result<Message> sendMessage(@Part("chat_id") Integer id,
                                @Part("text") String text,
                                @Part("disable_web_page_preview") Boolean disable,
                                @Part("reply_to_message_id") Integer replyMessage,
                                @Part("reply_markup") Reply reply) throws IOException;

    @POST("/sendChatAction")
    Result<Boolean> sendChatAction(@Query("chat_id") Integer id,
                                   @Query("action") ChatAction action) throws IOException;


    @Multipart
    @POST("/sendLocation")
    Result<Message> sendLocation(@Part("chat_id") Integer id,
                                 @Part("latitude") Float latitude,
                                 @Part("longitude") Float longitude,
                                 @Part("reply_markup") Reply reply) throws IOException;

}
