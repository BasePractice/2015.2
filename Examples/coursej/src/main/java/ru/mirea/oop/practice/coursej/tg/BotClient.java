package ru.mirea.oop.practice.coursej.tg;

import com.google.gson.annotations.SerializedName;
import com.squareup.okhttp.OkHttpClient;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit.Call;
import ru.mirea.oop.practice.coursej.ServiceCreator;
import ru.mirea.oop.practice.coursej.tg.entities.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public final class BotClient {
    private static final Logger logger = LoggerFactory.getLogger(BotClient.class);
    public final String url;
    private final TgApi clientApi;

    public BotClient(String token, OkHttpClient client) {
        this(token, client, "https://api.telegram.org/bot");
    }

    public BotClient(String token, OkHttpClient client, String serverUrl) {
        this.url = serverUrl + token + "/";
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
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("chat_id", String.valueOf(id));
        builder.addBinaryBody("document", file, ContentType.APPLICATION_OCTET_STREAM, file.getName());
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(url + "sendDocument");
        HttpEntity multipart = builder.build();
        httppost.setEntity(multipart);
        CloseableHttpResponse response = httpClient.execute(httppost);
        if (response.getStatusLine().getStatusCode() == 200) {
            HttpEntity entity = response.getEntity();
            try {

                InputStream content = entity.getContent();
                ResultMessage resultMessage = ServiceCreator.gson.fromJson(new InputStreamReader(content), ResultMessage.class);
                if (resultMessage == null)
                    return null;
                return resultMessage.result;
            } finally {
                EntityUtils.consume(entity);
            }
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
