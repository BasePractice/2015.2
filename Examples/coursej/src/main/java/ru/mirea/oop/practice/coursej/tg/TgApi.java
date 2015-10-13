package ru.mirea.oop.practice.coursej.tg;

import retrofit.Call;
import retrofit.http.*;
import ru.mirea.oop.practice.coursej.tg.entities.*;

import java.io.IOException;
import java.io.InputStream;

public interface TgApi {
    @GET("getMe")
    Call<Result<User>> getMe() throws IOException;

    @GET("getUpdates")
    Call<Result<Update[]>> getUpdates(@Query("offset") Integer offset,
                                      @Query("limit") Integer limit,
                                      @Query("timeout") Integer timeout) throws IOException;

    @Multipart
    @POST("sendDocument")
    Call<Result<Message>> sendDocument(@Part("chat_id") Integer id,
                                       @Part("reply_to_message_id") Integer replyMessage,
                                       @Part("reply_markup") Reply reply,
                                       @Part("document") InputStream stream) throws IOException;

    @Multipart
    @POST("sendMessage")
    Call<Result<Message>> sendMessage(@Part("chat_id") Integer id,
                                      @Part("text") String text,
                                      @Part("disable_web_page_preview") Boolean disable,
                                      @Part("reply_to_message_id") Integer replyMessage,
                                      @Part("reply_markup") Reply reply) throws IOException;

    @POST("sendChatAction")
    Call<Result<Boolean>> sendChatAction(@Query("chat_id") Integer id,
                                         @Query("action") ChatAction action) throws IOException;


    @Multipart
    @POST("sendLocation")
    Call<Result<Message>> sendLocation(@Part("chat_id") Integer id,
                                       @Part("latitude") Float latitude,
                                       @Part("longitude") Float longitude,
                                       @Part("reply_markup") Reply reply) throws IOException;

}
