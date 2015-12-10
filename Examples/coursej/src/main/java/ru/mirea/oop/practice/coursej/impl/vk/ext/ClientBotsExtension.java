package ru.mirea.oop.practice.coursej.impl.vk.ext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mirea.oop.practice.coursej.api.vk.FriendsApi;
import ru.mirea.oop.practice.coursej.api.vk.MessagesApi;
import ru.mirea.oop.practice.coursej.api.vk.entities.Contact;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public abstract class ClientBotsExtension extends AbstractBotsExtension implements Runnable {
    protected static final Logger logger = LoggerFactory.getLogger(ServiceBotsExtension.class);
    protected static final String FRIENDS_FIELDS = "nickname, " +
            "domain, " +
            "sex, " +
            "bdate, " +
            "city, " +
            "country, " +
            "timezone, " +
            "photo_50, " +
            "photo_100, " +
            "photo_200_orig, " +
            "has_mobile, " +
            "contacts, " +
            "education, " +
            "online, " +
            "relation, " +
            "last_seen, " +
            "status, " +
            "can_write_private_message, " +
            "can_see_all_posts, " +
            "can_post, " +
            "universities";
    private static final int DEFAULT_TIMEOUT = 1000;
    protected static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    protected final Map<Long, Contact> friends = new HashMap<>();
    protected final OkHttpClient ok;
    protected final MessagesApi messages;

    protected ClientBotsExtension(String name) throws Exception {
        super(name);
        this.ok = api.createClient();
        this.messages = api.getMessages();
        this.ok.setConnectTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    @Override
    public final boolean isService() {
        return false;
    }

    @Override
    protected final Future<?> doStart() throws Exception {
        return executor.submit(this);
    }

    @Override
    protected void doStop() throws Exception {

    }

    @Override
    protected boolean init() throws Exception {
        api.start();
        return true;
    }

    @Override
    public final void run() {
        logger.info("Запущен клиент");
        try {
            friends.clear();
            FriendsApi friendsApi = api.getFriends();
            Contact[] contacts = friendsApi.list(null, null, null, null, FRIENDS_FIELDS);
            for (Contact contact : contacts) {
                friends.put(contact.id, contact);
            }
        } catch (Exception ex) {
            logger.error("Ошибка получения списка друзей", ex);
        }
        doClient();
        logger.info("Клиент остановлен");
    }

    protected abstract void doClient();
}
