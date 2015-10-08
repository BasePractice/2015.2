package ru.mirea.oop.practice.coursej.ext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.*;
import lombok.Data;
import ru.mirea.oop.practice.coursej.vk.Friends;
import ru.mirea.oop.practice.coursej.vk.Messages;
import ru.mirea.oop.practice.coursej.vk.Result;
import ru.mirea.oop.practice.coursej.vk.VkApi;
import ru.mirea.oop.practice.coursej.vk.entities.Contact;
import ru.mirea.oop.practice.coursej.vk.entities.LongPollData;

import java.io.IOException;
import java.io.Reader;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public abstract class ServiceExtension extends AbstractExtension implements Runnable {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final Map<Long, Contact> friends = new HashMap<>();
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
        //FIXME: Получаем всех друзей
        try {
            ServiceExtension.friends.clear();
            Friends friends = api.getFriends();
            retrofit.Call<Result<Contact[]>> list = friends.list(null, null, null, null, "nickname, domain, sex, bdate, city, country, timezone, photo_50, photo_100, photo_200_orig, has_mobile, contacts, education, online, relation, last_seen, status, can_write_private_message, can_see_all_posts, can_post, universities");
            Contact[] contacts = Result.call(list);
            for (Contact contact : contacts) {
                ServiceExtension.friends.put(contact.uid, contact);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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

    private static final int MODE_ATTACH = 2;
    private static final int MODE_EXTEND_EVENT = 8;
    private static final int MODE_PTS = 32;
    private static final int MODE_STATUS_CODE = 64;

    private void requestServer() {
        LongPollData data;
        try {
            data = Result.call(messages.getLongPollServer(null, null));

            if (data != null) {
                int mode = MODE_ATTACH | MODE_EXTEND_EVENT | MODE_PTS | MODE_STATUS_CODE;
                HttpUrl url = HttpUrl.parse("http://" + data.server).newBuilder()
                        .addQueryParameter("key", data.key)
                        .addQueryParameter("wait", "" + timeout)
                        .addQueryParameter("mode", Integer.toString(mode))
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

    /**
     * Первым параметром каждого события передаётся его код, поддерживаются следующие коды событий:
     * <p/>
     * 0,$message_id,0 — удаление сообщения с указанным local_id
     * 1,$message_id,$flags — замена флагов сообщения (FLAGS:=$flags)
     * 2,$message_id,$mask[,$user_id] — установка флагов сообщения (FLAGS|=$mask)
     * 3,$message_id,$mask[,$user_id] — сброс флагов сообщения (FLAGS&=~$mask)
     * 4,$message_id,$flags,$from_id,$timestamp,$subject,$text,$attachments — добавление нового сообщения
     * 6,$peer_id,$local_id — прочтение всех входящих сообщений с $peer_id вплоть до $local_id включительно
     * 7,$peer_id,$local_id — прочтение всех исходящих сообщений с $peer_id вплоть до $local_id включительно
     * 8,-$user_id,$extra — друг $user_id стал онлайн, $extra не равен 0, если в mode был передан флаг 64, в младшем байте (остаток от деления на 256) числа $extra лежит идентификатор платформы (таблица ниже)
     * 9,-$user_id,$flags — друг $user_id стал оффлайн ($flags равен 0, если пользователь покинул сайт (например, нажал выход) и 1, если оффлайн по таймауту (например, статус away))
     * 51,$chat_id,$self — один из параметров (состав, тема) беседы $chat_id были изменены. $self - были ли изменения вызваны самим пользователем
     * 61,$user_id,$flags — пользователь $user_id начал набирать текст в диалоге. событие должно приходить раз в ~5 секунд при постоянном наборе текста. $flags = 1
     * 62,$user_id,$chat_id — пользователь $user_id начал набирать текст в беседе $chat_id.
     * 70,$user_id,$call_id — пользователь $user_id совершил звонок имеющий идентификатор $call_id.
     * 80,$count,0 — новый счетчик непрочитанных в левом меню стал равен $count.
     */
    //TODO:  Написать разбор обновлений
    private void processUpdates(List<List<Object>> updates) {
        for (List<Object> update : updates) {
            int type = ((Number) update.remove(0)).intValue();
            switch (type) {
                case 4: {
                    long idMessage = ((Number) update.remove(0)).longValue();
                    int flags = ((Number) update.remove(0)).intValue();
                    long idFrom = ((Number) update.remove(0)).longValue();
                    long timestamp = ((Number) update.remove(0)).longValue();
                    String subject = (String) update.remove(0);
                    String text = (String) update.remove(0);
                    Message message = new Message(idMessage, flags, friends.get(idFrom), timestamp, subject, text);
                    System.out.println(message);
                    break;
                }
                case 8: {
                    long idUser = Math.abs(((Number) update.remove(0)).longValue());
                    int extra = ((Number) update.remove(0)).intValue();
                    int value = 0;
                    if (extra != 0) {
                        value = extra & 0xff;
                    }
                    UserOnline status = new UserOnline(friends.get(idUser), value);
                    System.out.println(status);
                    break;
                }
                case 9: {
                    long idUser = Math.abs(((Number) update.remove(0)).longValue());
                    int flags = ((Number) update.remove(0)).intValue();
                    UserOffline status = new UserOffline(friends.get(idUser), flags != 0);
                    System.out.println(status);
                    break;
                }
                default: {
                    System.out.print("" + type + ": ");
                    for (int i = 0; i < update.size(); ++i) {
                        if (i > 0)
                            System.out.print(", ");
                        System.out.print(update.get(i));
                    }
                    System.out.println();
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

    @Data
    protected static final class UserOnline {
        protected final Contact contact;
        protected final int platform;


        protected UserOnline(Contact contact, int platform) {
            this.contact = contact;
            this.platform = platform;
        }

        @Override
        public String toString() {
            return contact.firstName + " " + contact.lastName + " online";
        }
    }

    @Data
    protected static final class UserOffline {
        protected final Contact contact;
        protected final boolean isAway;


        protected UserOffline(Contact contact, boolean isAway) {
            this.contact = contact;
            this.isAway = isAway;
        }

        @Override
        public String toString() {
            return contact.firstName + " " + contact.lastName + " " + (isAway ? "away" : "offline");
        }
    }

    @Data
    protected static final class Message {
        protected final long id;
        protected final int flags;
        protected final Contact contact;
        protected final long timestamp;
        protected final String subject;
        protected final String text;

        /**
         * Attach
         */
        protected Message(long id, int flags, Contact contact, long timestamp, String subject, String text) {
            this.id = id;
            this.flags = flags;
            this.contact = contact;
            this.timestamp = timestamp;
            this.subject = subject;
            this.text = text;
        }
    }
}
