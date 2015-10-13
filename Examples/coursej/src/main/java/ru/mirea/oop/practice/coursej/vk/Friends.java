package ru.mirea.oop.practice.coursej.vk;


import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;
import ru.mirea.oop.practice.coursej.vk.entities.Contact;

public interface Friends {
    @GET("/method/friends.get")
    Call<Result<Contact[]>> list(@Query("user_id") Integer idUser,
                                 @Query("list_id") Integer idList,
                                 @Query("count") Integer count,
                                 @Query("offset") Integer offset,
                                 @Query("fields") String fields);
}
