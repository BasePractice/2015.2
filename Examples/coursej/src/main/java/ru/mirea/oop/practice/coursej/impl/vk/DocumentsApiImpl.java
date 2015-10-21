package ru.mirea.oop.practice.coursej.impl.vk;

import retrofit.Call;
import ru.mirea.oop.practice.coursej.api.vk.DocumentsApi;
import ru.mirea.oop.practice.coursej.api.vk.entities.Document;

import java.io.IOException;

final class DocumentsApiImpl implements DocumentsApi {
    private final Documents iter;

    DocumentsApiImpl(Documents iter) {
        this.iter = iter;
    }

    @Override
    public Document[] list(Integer count, Integer offset, Integer idOwner) throws IOException {
        Call<Result<Document[]>> call = iter.list(count, offset, idOwner);
        return Result.call(call);
    }
}
