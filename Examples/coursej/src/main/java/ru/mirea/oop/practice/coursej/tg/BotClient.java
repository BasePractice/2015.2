package ru.mirea.oop.practice.coursej.tg;

import com.google.gson.annotations.SerializedName;
import com.squareup.okhttp.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit.Call;
import ru.mirea.oop.practice.coursej.ServiceCreator;
import ru.mirea.oop.practice.coursej.tg.entities.*;

import java.io.IOException;

public final class BotClient {
    private static final Logger logger = LoggerFactory.getLogger(BotClient.class);
    public final String url;
    private final TgApi clientApi;
    private final OkHttpClient client;

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

    public Message sendDocument(Integer id, java.io.File file) throws IOException {
        MultipartBuilder builder = new MultipartBuilder();
        builder.addFormDataPart(
                "document",
                file.getName(),
                RequestBody.create(MediaType.parse("application/octet-stream"), file));
        builder.addFormDataPart("chat_id", String.valueOf(id));
        builder.type(MultipartBuilder.FORM);
        RequestBody build = builder.build();
        Request request = new Request.Builder()
                .url(HttpUrl.parse(url).newBuilder().addPathSegment("sendDocument").build())
                .post(build)
                .build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            ResultMessage rm = ServiceCreator.gson.fromJson(response.body().charStream(), ResultMessage.class);
            if (rm != null)
                return rm.result;
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
