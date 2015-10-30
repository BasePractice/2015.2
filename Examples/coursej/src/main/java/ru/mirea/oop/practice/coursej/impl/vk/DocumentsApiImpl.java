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
    public Document uploadAndSaveDocument(File file, String mediaType) throws IOException {

        UploadServer uploadServer = this.getDocumentsUploadServer();
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBuilder()
                .type(MultipartBuilder.FORM)
                .addFormDataPart("file", file.getName(),
                        RequestBody.create(MediaType.parse(mediaType), file))
                .build();
        Request request = new Request.Builder()
                .url(uploadServer.urlUpload)
                .post(requestBody)
                .build();
        Response response = client.newCall(request).execute();
        /**FIXME: Проверить */
        if (!response.isSuccessful()) {
            Result result = ServiceCreator.gson.fromJson(
                    response.body().charStream(),
                    Result.class);
            throw new IOException(result.error.errorMessage);
        }
        /**FIXME: Проверить на работоспособность, поправить и удалить комментарий */
        String documentUploadedString = response.body().string();
        System.out.println(documentUploadedString);
        DocumentUploaded uploaded = ServiceCreator.gson.fromJson(documentUploadedString, DocumentUploaded.class);
        Call<Result<Document>> call = iter.saveDocument(uploaded.file);
        return Result.call(call);
    }

    @Override
    public Integer delete(Document document) throws IOException {
        Call<Result<Integer>> call = iter.delete(document.id, document.idOwner);
        return Result.call(call);
    }


    @Override
    public Document[] list(Integer count, Integer offset, Integer idOwner) throws IOException {
        Call<Result<Document[]>> call = iter.list(count, offset, idOwner);
        return Result.call(call);
    }


}
