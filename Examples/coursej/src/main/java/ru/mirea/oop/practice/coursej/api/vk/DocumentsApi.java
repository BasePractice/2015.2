package ru.mirea.oop.practice.coursej.api.vk;

import com.squareup.okhttp.MediaType;
import ru.mirea.oop.practice.coursej.api.vk.entities.Document;
import ru.mirea.oop.practice.coursej.api.vk.entities.UploadServer;

import java.io.File;
import java.io.IOException;

public interface DocumentsApi extends ExternalCall {
    UploadServer getDocumentsUploadServer() throws IOException;


    Document uploadAndSaveDocument(File file, String mediaType) throws IOException;

    Integer delete(Document document) throws IOException;

    Document[] list(Integer count,
                    Integer offset,
                    Integer idOwner) throws IOException;


}
