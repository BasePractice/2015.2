package ru.mirea.oop.practice.coursej.impl.vk;

import com.google.gson.JsonParser;
import com.squareup.okhttp.*;
import retrofit.Call;
import ru.mirea.oop.practice.coursej.api.vk.DocumentsApi;
import ru.mirea.oop.practice.coursej.api.vk.entities.Document;
import ru.mirea.oop.practice.coursej.api.vk.entities.UploadServer;

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
        String responseBody = response.body().string();
        String fileStr = new JsonParser().parse(responseBody).getAsJsonObject().get("file").getAsString();
        Call<Result<Document>> call = iter.saveDocument(fileStr);
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
