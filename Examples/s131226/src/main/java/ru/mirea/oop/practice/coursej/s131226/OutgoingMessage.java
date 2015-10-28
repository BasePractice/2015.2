package ru.mirea.oop.practice.coursej.s131226;

import com.squareup.okhttp.*;
import org.json.JSONObject;
import ru.mirea.oop.practice.coursej.api.VkontakteApi;
import ru.mirea.oop.practice.coursej.api.vk.DocumentsApi;
import ru.mirea.oop.practice.coursej.api.vk.MessagesApi;
import ru.mirea.oop.practice.coursej.api.vk.entities.UploadServer;
import ru.mirea.oop.practice.coursej.s131226.Parser.ParserApi;
import ru.mirea.oop.practice.coursej.s131226.Parser.ParserApiImpl;

import java.io.File;
import java.io.IOException;

public class OutgoingMessage {
    private long id;
    private String text;
    private String attachment;
    private final VkontakteApi api;


    public OutgoingMessage(long id, VkontakteApi api) {
        this.id = id;
        this.api = api;
    }
    public void setText(String text) {
        this.text = text;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public void setAttachment(File file) throws Exception {
        // TODO: дописать DocumentsApi
//        DocumentsApi documentsApi = api.getDocuments();
//        UploadServer uploadServer = documentsApi.getMessagesUploadServer();
//        OkHttpClient client = new OkHttpClient();
//        RequestBody requestBody = new MultipartBuilder()
//                .type(MultipartBuilder.FORM)
//                .addFormDataPart("file", file.getName(),
//                        RequestBody.create(MediaType.parse("file/xlsx"), file))
//                .build();
//        Request request = new Request.Builder()
//                .url(uploadServer.upload_url)
//                .post(requestBody)
//                .build();
//        Response response = client.newCall(request).execute();
//        String responseBody = response.body().string();
//        JSONObject jsonResponse = new JSONObject(responseBody);
//        String fileStr = jsonResponse.getString("file");
//        String document = documentsApi.saveDocument(fileStr).toString();
//        document = document.split("vk.com/")[1].split("\\?")[0];
//        this.setAttachment(document);

    }
    public String getText() {
        return text;
    }

    public String getAttachment() {
        return attachment;
    }

    public int send(MessagesApi api) {
        try {
            return api.send(id, null, null, null, text, null, null, null, attachment, null, null);
        } catch (IOException e) {
            e.printStackTrace();

        }
        return 0;
    }

}
