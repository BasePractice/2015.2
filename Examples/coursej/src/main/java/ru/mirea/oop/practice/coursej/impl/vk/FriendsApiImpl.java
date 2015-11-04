package ru.mirea.oop.practice.coursej.impl.vk;

import retrofit.Call;
import ru.mirea.oop.practice.coursej.api.vk.FriendsApi;
import ru.mirea.oop.practice.coursej.api.vk.entities.Contact;

import java.io.IOException;

final class FriendsApiImpl implements FriendsApi {
    private final Friends inter;

    FriendsApiImpl(Friends inter) {
        this.inter = inter;
    }

    @Override
    public Contact[] list(Integer idUser, Integer idList, Integer count, Integer offset, String fields)
            throws IOException {
        Call<Result<Contact[]>> call = inter.list(idUser, idList, count, offset, fields);
        return Result.call(call);
    }
}
