package ru.mirea.oop.practice.coursej.impl.vk;

import com.google.gson.annotations.SerializedName;
import retrofit.Call;
import retrofit.Response;
import ru.mirea.oop.practice.coursej.api.vk.entities.Error;

import java.io.IOException;

final class Result<E> {
    @SerializedName("response")
    public E response;
    @SuppressWarnings("unused")
    @SerializedName("error")
    public Error error;

    static <E> E call(Call<Result<E>> callable) throws IOException {
        if (callable == null)
            return null;
        Response<Result<E>> result = callable.execute();
        if (result.isSuccess() && result.body().response != null)
            return result.body().response;
        Error error = result.body().error;
        if (error != null) {
            System.out.println("Error: " + error.errorCode + ", Message: " + error.errorMessage);
            throw new IOException(error.errorMessage);
        }
        System.out.println(result.errorBody());
        return null;
    }
}
