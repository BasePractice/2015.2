package ru.mirea.oop.practice.coursej.vk;


import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;
import ru.mirea.oop.practice.coursej.vk.entities.LongPollData;

public interface Messages {
    @GET("/method/messages.getLongPollServer")
    Call<Result<LongPollData>> getLongPollServer(@Query("use_ssl") Boolean useSsl,
                                                 @Query("need_pts") Boolean needPts);
}
