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

    public static final class ArrayDeserializer implements JsonDeserializer<Document[]> {

        @Override
        public Document[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            JsonArray array = (JsonArray) json;
            JsonElement count = array.get(0);
            Document[] documents = new Document[array.size() - 1];
            for (int i = 1; i < array.size(); i++) {
                JsonObject object = (JsonObject) array.get(i);
                Document document = new Document();
                document.id = object.get("did").getAsLong();
                document.idOwner = object.get("owner_id").getAsLong();
                document.title = object.get("title").getAsString();
                document.size = object.get("size").getAsInt();
                document.ext = object.get("ext").getAsString();
                document.url = object.get("url").getAsString();
                documents[i - 1] = document;
            }
            return documents;
        }
    }
}
