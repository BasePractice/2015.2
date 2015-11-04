package ru.mirea.oop.practice.coursej.impl.vk;

import retrofit.Call;
import ru.mirea.oop.practice.coursej.api.vk.UsersApi;
import ru.mirea.oop.practice.coursej.api.vk.entities.Contact;

import java.io.IOException;

final class UsersApiImpl implements UsersApi {
    private final Users inter;

    UsersApiImpl(Users inter) {
        this.inter = inter;
    }

    @Override
    public Contact[] list(String users, String fields, String nameCase) throws IOException {
        Call<Result<Contact[]>> list = inter.list(users, fields, nameCase);
        return Result.call(list);
    }
}
