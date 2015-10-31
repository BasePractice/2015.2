package ru.mirea.oop.practice.coursej.s131226;

import com.google.gson.JsonParser;
import com.squareup.okhttp.*;
import ru.mirea.oop.practice.coursej.api.VkontakteApi;
import ru.mirea.oop.practice.coursej.api.vk.DocumentsApi;
import ru.mirea.oop.practice.coursej.api.vk.MessagesApi;
import ru.mirea.oop.practice.coursej.api.vk.entities.Document;
import ru.mirea.oop.practice.coursej.api.vk.entities.UploadServer;

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

    public void setAttachment(File file)  {
        DocumentsApi documentsApi = null;
        try {
            documentsApi = api.getDocuments();

        Document[] documents = documentsApi.list(10, 0, 11105141);
        for (Document existDoc : documents) {
            if (existDoc.title .equals(file.getName())) {
                if (documentsApi.delete(existDoc) == 1) System.out.println("успешно удален" + existDoc.toString());
            }
        }

        documentsApi.uploadDocument(file);
        documents=documentsApi.list(1, 0, 11105141);
        this.setAttachment("doc" + documents[0].idOwner + "_" + documents[0].id);
        } catch (Exception e) {
        e.printStackTrace();
    }

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
