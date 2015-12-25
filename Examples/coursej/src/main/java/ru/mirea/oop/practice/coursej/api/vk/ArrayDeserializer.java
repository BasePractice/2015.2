package ru.mirea.oop.practice.coursej.api.vk;

import com.google.gson.*;

import java.lang.reflect.Type;

public final class ArrayDeserializer<E> implements JsonDeserializer<E[]> {
    private final Class<E> classs;
    private final ToArray<E> toArray;

    public ArrayDeserializer(Class<E> classs, ToArray<E> toArray) {
        this.classs = classs;
        this.toArray = toArray;
    }

    @Override
    public E[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonArray array = (JsonArray) json;
        if (array.size() == 0)
            return toArray.toArray(0);
        JsonElement element = array.get(0);
        if (!element.isJsonPrimitive() && array.size() > 1) { // одиночные документы тоже приходят в массиве =(
            throw new JsonParseException("Возможно старая верси API");
        }
        E[] list = toArray.toArray(array.size() - 1);
        for (int i = 1; i < array.size(); i++) {
            JsonObject object = (JsonObject) array.get(i);
            E entity = context.deserialize(object, classs);
            list[i - 1] = entity;
        }
        return list;
    }

    public interface ToArray<E> {
        E[] toArray(int size);
    }
}
