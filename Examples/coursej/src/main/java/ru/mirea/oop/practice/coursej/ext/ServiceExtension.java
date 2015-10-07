package ru.mirea.oop.practice.coursej.ext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.*;
import lombok.Data;
import ru.mirea.oop.practice.coursej.vk.Messages;
import ru.mirea.oop.practice.coursej.vk.Result;
import ru.mirea.oop.practice.coursej.vk.VkApi;
import ru.mirea.oop.practice.coursej.vk.entities.LongPollData;

import java.io.IOException;
import java.io.Reader;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public abstract class ServiceExtension extends AbstractExtension implements Runnable {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final OkHttpClient ok;
    private final Messages messages;

    private static final int DEFAULT_TIMEOUT = 1000;
    private static final Event timeoutEvent = new Event(EventType.TIMEOUT);
    private final int timeout;
    private volatile boolean isRunning;

    protected ServiceExtension(VkApi api, int timeout) {
        super(api);
        this.timeout = timeout;
        this.isRunning = true;
        this.ok = api.getClient().clone();
        this.messages = api.getMessages();
        this.ok.setConnectTimeout(timeout, TimeUnit.MILLISECONDS);
    }

    public ServiceExtension(VkApi api) {
        this(api, DEFAULT_TIMEOUT);
    }

    @Override
    public final boolean isService() {
        return true;
    }

    @Override
    protected final void doStart() throws Exception {
        new Thread(this).start();
    }

    @Override
    protected final void doStop() throws Exception {
        isRunning = false;
    }

    protected abstract void doEvent(Event event);

    private void raiseTimeout() {
        doEvent(timeoutEvent);
    }

    @Override
    public final void run() {
        System.out.println("Start longpll");
        while (isRunning) {
            requestServer();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Stop longpull");
    }

    private void requestServer() {
        LongPollData data;
        try {
            data = Result.call(messages.getLongPollServer(null, null));

            if (data != null) {
                HttpUrl url = HttpUrl.parse("http://" + data.server).newBuilder()
                        .addQueryParameter("key", data.key)
                        .addQueryParameter("wait", "" + timeout)
                        .addQueryParameter("mode", "2")
                        .addQueryParameter("act", "a_check")
                        .build();
                long lastEvent = data.lastEvent;
                while (isRunning) {
                    try {
                        lastEvent = requestData(url, lastEvent);
                        if (lastEvent < 0)
                            break;
                    } catch (SocketTimeoutException ex) {
                        raiseTimeout();
                    }
                }
            }
        } catch (SocketTimeoutException ex) {
            raiseTimeout();
        } catch (IOException e) {
            raiseException(e);
        }
    }

    private void raiseException(IOException e) {
        e.printStackTrace();
    }

    private long requestData(HttpUrl url, long lastEvent) throws IOException {
        HttpUrl requestUrl = url.newBuilder().addQueryParameter("ts", Long.toString(lastEvent)).build();
        Request request = new Request.Builder().url(requestUrl).get().build();
        Response response = ok.newCall(request).execute();
        LongPollData data = null;
        if (response.isSuccessful()) {
            ResponseBody body = response.body();
            try (Reader reader = body.charStream()) {
                data = gson.fromJson(reader, LongPollData.class);
                if (data.failed != null) {
                    return -1;
                }
                processUpdates(data.updates);
            }
        }
        return response.isSuccessful() ? data.lastEvent : -1;
    }

    //http://vk.com/dev/using_longpoll
    //TODO:  Написать разбор обновлений
    private void processUpdates(List<List<Object>> updates) {
        for (List<Object> update: updates) {
            Integer type = (Integer)update.remove(0);
            switch (type) {
                case 4: {
                    //TODO: Message
                    break;
                }
            }
        }
        System.out.println("Updates: " + updates.size());
    }

    protected enum EventType {
        TIMEOUT
    }

    @Data
    protected static final class Event {
        public final EventType type;

        private Event(EventType type) {
            this.type = type;
        }
    }
}
