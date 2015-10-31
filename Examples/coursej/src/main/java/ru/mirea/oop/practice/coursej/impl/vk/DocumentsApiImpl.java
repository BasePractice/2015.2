package ru.mirea.oop.practice.coursej.impl.vk;

import com.squareup.okhttp.*;
import retrofit.Call;
import ru.mirea.oop.practice.coursej.api.vk.DocumentsApi;
import ru.mirea.oop.practice.coursej.api.vk.entities.Document;
import ru.mirea.oop.practice.coursej.api.vk.entities.DocumentUploaded;
import ru.mirea.oop.practice.coursej.api.vk.entities.UploadServer;
import ru.mirea.oop.practice.coursej.impl.ServiceCreator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

final class DocumentsApiImpl implements DocumentsApi {
    private final Documents iter;

    DocumentsApiImpl(Documents iter) {
        this.iter = iter;
    }

    @Override
    public UploadServer getDocumentsUploadServer() throws IOException {
        Call<Result<UploadServer>> call = iter.getDocumentsUploadServer();
        return Result.call(call);
    }

    @Override
    public boolean uploadDocument(File file) throws IOException {
        String type = Files.probeContentType(Paths.get(file.getAbsolutePath()));
        if (type == null)
            type = "application/octet-stream";
        UploadServer uploadServer = this.getDocumentsUploadServer();
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBuilder()
                .type(MultipartBuilder.FORM)
                .addFormDataPart("file", file.getName(),
                        RequestBody.create(MediaType.parse(type), file))
                .build();
        Request request = new Request.Builder()
                .url(uploadServer.urlUpload)
                .post(requestBody)
                .build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            Result result = ServiceCreator.gson.fromJson(
                    response.body().charStream(),
                    Result.class);
            throw new IOException(result.error.errorMessage);
        }
        DocumentUploaded uploaded = ServiceCreator.gson.fromJson(response.body().string(), DocumentUploaded.class);
        if (uploaded.file == null)
            throw new IOException("Файл " + file.getAbsolutePath() + " не найден");
        Call<Result<Document[]>> call = iter.saveDocument(uploaded.file, null, null);
        /** FIXME: Не возвращает список объектов созданных. Может написать в поддержку? */
        Result.call(call);
        return true;
    }

    @Override
    public Integer delete(Document document) throws IOException {
        Call<Result<Integer>> call = iter.delete(document.id, document.idOwner);
        return Result.call(call);
    }


    @Override
    public Document[] list(Integer count, Integer offset, Long idOwner) throws IOException {
        Call<Result<Document[]>> call = iter.list(count, offset, idOwner);
        return Result.call(call);
    }

    @Override
    public Document[] id(String docs) throws IOException {
        Call<Result<Document[]>> call = iter.id(docs);
        return Result.call(call);
    }


}
