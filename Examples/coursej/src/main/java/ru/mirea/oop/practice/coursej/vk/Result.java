package ru.mirea.oop.practice.coursej.vk;

import com.google.gson.annotations.SerializedName;
import retrofit.Call;
import retrofit.Response;

import java.io.IOException;

public final class Result<E> {
    @SerializedName("response")
    public E response;

    public static <E> E call(Call<Result<E>> callable) throws IOException {
        if (callable == null)
            return null;
        Response<Result<E>> result = callable.execute();
        if (result.isSuccess() && result.body() != null)
            return result.body().response;
        System.out.println(result.errorBody());
        return null;
    }
}
