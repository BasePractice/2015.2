package ru.mirea.oop.practice.coursej.tg;

import com.squareup.okhttp.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit.Call;
import retrofit.Retrofit;
import ru.mirea.oop.practice.coursej.tg.entities.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public final class BotClient {
    private static final Logger logger = LoggerFactory.getLogger(BotClient.class);

    private final TgApi clientApi;

    public BotClient(String token, OkHttpClient client) {
        this(token, client, "https://api.telegram.org/bot");
    }

    public BotClient(String token, OkHttpClient client, String serverUrl) {
        try {
            serverUrl = new URL(serverUrl).toExternalForm();
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
        Retrofit.Builder builder = new Retrofit.Builder()
                .client(client)
                .baseUrl(serverUrl + token);
        this.clientApi = builder.build()
                .create(TgApi.class);
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
        return get(clientApi.sendMessage(id, text, null, null, reply));
    }

    public Boolean sendChatAction(Integer id, ChatAction action) throws IOException {
        return get(clientApi.sendChatAction(id, action));
    }

    public Message sendDocument(Integer id, String fileName, InputStream stream) throws IOException {
        return get(clientApi.sendDocument(id, null, null, stream));
    }

    private static <E> E get(Call<Result<E>> result) throws IOException {
        return Result.call(result);
    }
}
