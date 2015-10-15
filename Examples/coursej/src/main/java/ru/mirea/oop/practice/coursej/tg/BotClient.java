package ru.mirea.oop.practice.coursej.tg;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit.Call;
import ru.mirea.oop.practice.coursej.ServiceCreator;
import ru.mirea.oop.practice.coursej.tg.entities.*;

import java.io.IOException;

public final class BotClient {
    private static final Logger logger = LoggerFactory.getLogger(BotClient.class);

    private final TgApi clientApi;

    public BotClient(String token, OkHttpClient client) {
        this(token, client, "https://api.telegram.org/bot");
    }

    public BotClient(String token, OkHttpClient client, String serverUrl) {
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

    public Message sendDocument(Integer id, String fileName, MediaType mediaType, java.io.File file) throws IOException {
        RequestBody requestBody = RequestBody.create(mediaType, file);
        return get(clientApi.sendDocument(id, null, null, requestBody));
    }

    private static <E> E get(Call<Result<E>> result) throws IOException {
        return Result.call(result);
    }
}
