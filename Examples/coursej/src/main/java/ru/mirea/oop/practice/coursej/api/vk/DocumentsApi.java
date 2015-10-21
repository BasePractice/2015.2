package ru.mirea.oop.practice.coursej.api.vk;

import ru.mirea.oop.practice.coursej.api.vk.entities.Document;

import java.io.IOException;

public interface DocumentsApi extends ExternalCall {
    Document[] list(Integer count,
                    Integer offset,
                    Integer idOwner) throws IOException;
}
