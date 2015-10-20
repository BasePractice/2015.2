package ru.mirea.oop.practice.coursej.impl.vk;

import retrofit.Call;
import ru.mirea.oop.practice.coursej.api.vk.AccountApi;
import ru.mirea.oop.practice.coursej.impl.vk.Account;
import ru.mirea.oop.practice.coursej.impl.vk.Result;

import java.io.IOException;

final class AccountApiImpl implements AccountApi {
    private final Account inter;

    AccountApiImpl(Account inter) {
        this.inter = inter;
    }

    @Override
    public int setOnline(Integer voip) throws IOException {
        Call<Result<Integer>> call = inter.setOnline(voip);
        return Result.call(call);
    }
}
