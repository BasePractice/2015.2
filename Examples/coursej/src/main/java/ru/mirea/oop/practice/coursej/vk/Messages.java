package ru.mirea.oop.practice.coursej.vk;


import retrofit.Call;
import retrofit.http.*;
import ru.mirea.oop.practice.coursej.vk.entities.LongPollData;

public interface Messages {
    @GET("/method/messages.getLongPollServer")
    Call<Result<LongPollData>> getLongPollServer(@Query("use_ssl") Boolean useSsl,
                                                 @Query("need_pts") Boolean needPts);

    /**
     * @param idUser          идентификатор пользователя, которому отправляется сообщение.
     * @param domain          короткий адрес пользователя (например, illarionov)
     * @param idChat          идентификатор беседы, к которой будет относиться сообщение.
     * @param idUsers         идентификаторы получателей сообщения (при необходимости отправить сообщение сразу нескольким пользователям).
     *                        /список целых чисел, разделенных запятыми/
     * @param message         текст личного cообщения (является обязательным, если не задан параметр @see attachment)
     * @param guid            уникальный идентификатор, предназначенный для предотвращения повторной отправки одинакового сообщения.
     * @param latitude        latitude, широта при добавлении местоположения.
     * @param longitude       longitude, долгота при добавлении местоположения.
     * @param attachment      медиавложения к личному сообщению, перечисленные через запятую.
     * @param forwardMessages идентификаторы пересылаемых сообщений, перечисленные через запятую. Перечисленные сообщения отправителя будут отображаться в теле письма у получателя.
     * @param isSticker       идентификатор стикера.
     * @return После успешного выполнения возвращает идентификатор отправленного сообщения.
     */
    @FormUrlEncoded
    @POST("/method/messages.send")
    Call<Result<Integer>> send(@Field("user_id") Long idUser,
                               @Field("domain") String domain,
                               @Field("chat_id") Integer idChat,
                               @Field("user_ids") String idUsers,
                               @Field("message") String message,
                               @Field("guid") Integer guid,
                               @Field("lat") Float latitude,
                               @Field("long") Float longitude,
                               @Field("attachment") String attachment,
                               @Field("forward_messages") String forwardMessages,
                               @Field("sticker_id") Integer isSticker);

    @GET("/method/messages.setActivity")
    Call<Result<Integer>> setActivity(@Query("user_id") Long user_id,
                                                 @Query("type") String type);
}
