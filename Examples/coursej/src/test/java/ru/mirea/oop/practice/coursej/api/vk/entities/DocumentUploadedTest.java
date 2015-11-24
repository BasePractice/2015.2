package ru.mirea.oop.practice.coursej.api.vk.entities;

import org.junit.Test;
import ru.mirea.oop.practice.coursej.impl.ServiceCreator;

import java.io.InputStreamReader;

import static org.junit.Assert.*;

public final class DocumentUploadedTest {
    @Test
    public void testParseDocumentUploaded() throws Exception {
        try (InputStreamReader reader = new InputStreamReader(DocumentUploadedTest.class.getResourceAsStream("/.document.uploaded.json"))) {
            DocumentUploaded uploaded = ServiceCreator.gson.fromJson(reader, DocumentUploaded.class);
            assertNotNull(uploaded.file);
        }
    }
}