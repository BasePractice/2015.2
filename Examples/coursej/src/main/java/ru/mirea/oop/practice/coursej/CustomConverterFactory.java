package ru.mirea.oop.practice.coursej;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.ResponseBody;
import okio.Buffer;
import retrofit.Converter;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

final class CustomConverterFactory implements Converter.Factory {
    private final Gson gson;

    private CustomConverterFactory(Gson gson) {
        this.gson = gson;
    }

    public static CustomConverterFactory create(Gson gson) {
        return new CustomConverterFactory(gson);
    }

    @Override
    public Converter<?> get(Type type) {
        final TypeAdapter<?> adapter;
        if (type instanceof ParameterizedType) {
            adapter = gson.getAdapter(TypeToken.get(((ParameterizedType) type).getActualTypeArguments()[0]));
        } else {
            adapter = gson.getAdapter(TypeToken.get(type));
        }
        return new CustomConverter<>(adapter);
    }

    private static final class CustomConverter<E> implements Converter<E> {
        private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
        private static final Charset UTF_8 = Charset.forName("UTF-8");

        private final TypeAdapter<E> typeAdapter;

        private CustomConverter(TypeAdapter<E> typeAdapter) {
            this.typeAdapter = typeAdapter;
        }

        private static void to(Reader reader, int ch) throws IOException {
            int readed;
            while ((readed = reader.read()) > 0) {
                if (readed == ch)
                    break;
            }
        }

        @Override
        public E fromBody(ResponseBody body) throws IOException {
            Reader in = body.charStream();
            try {
                //FIXME: Исправить и облагородить
                to(in, ':');
                return typeAdapter.fromJson(in);
            } finally {
                try {
                    in.close();
                } catch (IOException ignored) {
                }
            }
        }

        @Override
        public RequestBody toBody(E value) {
            Buffer buffer = new Buffer();
            Writer writer = new OutputStreamWriter(buffer.outputStream(), UTF_8);
            try {
                typeAdapter.toJson(writer, value);
                writer.flush();
            } catch (IOException e) {
                throw new AssertionError(e); // Writing to Buffer does no I/O.
            }
            return RequestBody.create(MEDIA_TYPE, buffer.readByteString());
        }
    }
}
