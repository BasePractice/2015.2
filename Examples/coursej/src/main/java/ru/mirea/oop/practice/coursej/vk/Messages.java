package ru.mirea.oop.practice.coursej.vk;


import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;
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
    @GET("/method/messages.send")
    Call<Result<Integer>> send(@Query("user_id") Long idUser,
                               @Query("domain") String domain,
                               @Query("chat_id") Integer idChat,
                               @Query("user_ids") String idUsers,
                               @Query("message") String message,
                               @Query("guid") Integer guid,
                               @Query("lat") Float latitude,
                               @Query("long") Float longitude,
                               @Query("attachment") String attachment,
                               @Query("forward_messages") String forwardMessages,
                               @Query("sticker_id") Integer isSticker);
}
