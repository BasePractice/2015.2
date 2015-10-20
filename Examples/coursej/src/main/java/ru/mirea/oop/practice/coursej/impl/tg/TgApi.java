package ru.mirea.oop.practice.coursej.impl.tg;

import com.squareup.okhttp.RequestBody;
import retrofit.Call;
import retrofit.http.*;
import ru.mirea.oop.practice.coursej.api.tg.entities.*;

import java.io.IOException;

interface TgApi {
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
                                       @Part("document") RequestBody document) throws IOException;

    /**
     * @param id
     * @param text
     * @param parseMode    Markdown
     * @param disable
     * @param replyMessage
     * @param reply
     * @return
     * @throws IOException
     */
    @FormUrlEncoded
    @POST("sendMessage")
    Call<Result<Message>> sendMessage(@Field("chat_id") Integer id,
                                      @Field("text") String text,
                                      @Field("parse_mode") String parseMode,
                                      @Field("disable_web_page_preview") Boolean disable,
                                      @Field("reply_to_message_id") Integer replyMessage,
                                      @Field("reply_markup") Reply reply) throws IOException;

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
