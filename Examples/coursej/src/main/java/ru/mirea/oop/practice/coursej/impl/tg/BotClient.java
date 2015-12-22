package ru.mirea.oop.practice.coursej.impl.tg;

import com.google.common.io.ByteStreams;
import com.google.gson.annotations.SerializedName;
import com.squareup.okhttp.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit.Call;
import ru.mirea.oop.practice.coursej.Configuration;
import ru.mirea.oop.practice.coursej.api.tg.entities.*;
import ru.mirea.oop.practice.coursej.impl.ClientFactory;
import ru.mirea.oop.practice.coursej.impl.ServiceCreator;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public final class BotClient {
    private static final Logger logger = LoggerFactory.getLogger(BotClient.class);
    public final String url;
    private final TgApi clientApi;
    private final OkHttpClient client;

    public BotClient() {
        this(Configuration.loadKeyFrom(".telegram.txt"), ClientFactory.createOkClient());
    }

    public BotClient(String token, OkHttpClient client) {
        this(token, client, "https://api.telegram.org/bot");
    }

    public BotClient(String token, OkHttpClient client, String serverUrl) {
        this.url = serverUrl + token + "/";
        this.client = client;
        this.clientApi = ServiceCreator.createService(client, TgApi.class, serverUrl + token + "/");
    }

    public User getMe() throws IOException {
        return get(clientApi.getMe());
    }

    public Update[] getUpdates() throws IOException {
        return getUpdates(null, null, null);
    }

    public Update[] getUpdates(Integer offset, Integer limit, Integer timeout) throws IOException {
        return get(clientApi.getUpdates(offset, limit, timeout));
    }

    public Message sendMessage(Integer id, String text, Reply reply) throws IOException {
        return get(clientApi.sendMessage(id, text, "Markdown", null, null, reply));
    }

    public Boolean sendChatAction(Integer id, ChatAction action) throws IOException {
        return get(clientApi.sendChatAction(id, action));
    }

    public Message sendSticker(Integer id, String name, InputStream stream) throws IOException {
        return send(client, id, url, "sendSticker", "sticker", name, stream, null);
    }

    public Message sendPhoto(Integer id, String caption, String name, InputStream stream) throws IOException {
        Map<String, String> fields = new HashMap<>();
        fields.put("caption", caption);
        return send(client, id, url, "sendPhoto", "photo", name, stream, fields);
    }

    public Message sendDocument(Integer id, java.io.File file) throws IOException {
        try (InputStream stream = new FileInputStream(file)) {
            return send(client, id, url, "sendDocument", "document", file.getName(), stream, null);
        }
    }

    private static Message send(OkHttpClient client,
                                Integer id,
                                String url,
                                String method,
                                String field,
                                String fieldName,
                                InputStream content,
                                Map<String, String> fields) throws IOException {
        MultipartBuilder builder = new MultipartBuilder();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ByteStreams.copy(content, output);
        builder.addFormDataPart(
                field,
                fieldName,
                RequestBody.create(MediaType.parse("application/octet-stream"), output.toByteArray()));
        builder.addFormDataPart("chat_id", String.valueOf(id));
        if (fields != null) {
            for (Map.Entry<String, String> f : fields.entrySet()) {
                builder.addFormDataPart(f.getKey(), f.getValue());
            }
        }
        builder.type(MultipartBuilder.FORM);
        RequestBody build = builder.build();
        Request request = new Request.Builder()
                .url(HttpUrl.parse(url).newBuilder().addPathSegment(method).build())
                .post(build)
                .build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            ResultMessage rm = ServiceCreator.gson.fromJson(response.body().charStream(), ResultMessage.class);
            if (rm != null)
                return rm.result;
        } else {
            String string = response.body().string();
            System.out.println(string);
        }
        return null;
    }

    private final class ResultMessage {
        @SerializedName("result")
        public Message result;
    }

    private static <E> E get(Call<Result<E>> result) throws IOException {
        return Result.call(result);
    }
}
