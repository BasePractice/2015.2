package ru.mirea.oop.practice.coursej.vk;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;
import ru.mirea.oop.practice.coursej.vk.entities.Contact;

public interface Users {
    @GET("/method/users.get")
    Call<Contact[]> list();

    @GET("/method/users.get")
    Call<Contact[]> list(@Query("fields") String fields);
}
