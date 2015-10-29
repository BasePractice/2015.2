package ru.mirea.oop.practice.coursej.api.vk.entities;

import com.google.gson.*;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Type;

public final class Document {
    @SerializedName("id")
    public long id;
    @SerializedName("owner_id")
    public long idOwner;
    public String title;
    public int size;
    public String ext;
    public String url;

    public static final class DocumentDeserializer implements JsonDeserializer<Document> {

        @Override
        public Document deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            JsonObject object;
            if (json.getClass() == JsonArray.class) {
                object = json.getAsJsonArray().get(0).getAsJsonObject();
            } else {
                object = (JsonObject) json;
            }
            Document document = new Document();
            document.id = object.get("did").getAsLong();
            document.idOwner = object.get("owner_id").getAsLong();
            document.title = object.get("title").getAsString();
            document.size = object.get("size").getAsInt();
            document.ext = object.get("ext").getAsString();
            document.url = object.get("url").getAsString();
            return document;
        }
    }

    public static final class ArrayDeserializer implements JsonDeserializer<Document[]> {

        @Override
        public Document[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            JsonArray array = (JsonArray) json;
            //int count = array.remove(0).getAsInt();  сюда нам прилетает общее кол-во документов пользователя, что не имеет практической пользы
            // т. к. в методе list мы указываем параметр count который может отлечатся от этого значения
            Document[] documents = new Document[array.size() - 1];
            for (int i = 1; i < array.size(); i++) {// соответственно начинаем разбирать json с 1, а не 0 элемента
                JsonObject object = (JsonObject) array.get(i);
                Document document = context.deserialize(object, Document.class);
                documents[i - 1] = document;
            }
            return documents;
        }

    }
}
