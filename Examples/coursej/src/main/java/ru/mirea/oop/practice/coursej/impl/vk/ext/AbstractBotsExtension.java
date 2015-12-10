package ru.mirea.oop.practice.coursej.impl.vk.ext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mirea.oop.practice.coursej.api.VkontakteApi;
import ru.mirea.oop.practice.coursej.api.ext.BotsExtension;
import ru.mirea.oop.practice.coursej.api.vk.entities.Contact;
import ru.mirea.oop.practice.coursej.impl.vk.VkontakteApiImpl;

import java.io.IOException;
import java.util.concurrent.Future;

abstract class AbstractBotsExtension implements BotsExtension {
    protected final String DEFAULT_USER_FIELDS = "sex," +
            "bdate," +
            "city," +
            "country," +
            "photo_50," +
            "photo_100," +
            "photo_200_orig," +
            "photo_200," +
            "photo_400_orig," +
            "photo_max," +
            "photo_max_orig," +
            "photo_id,online," +
            "online_mobile," +
            "domain,has_mobile," +
            "contacts,connections," +
            "site,education," +
            "universities," +
            "schools," +
            "can_post," +
            "can_see_all_posts," +
            "can_see_audio," +
            "can_write_private_message," +
            "status," +
            "last_seen," +
            "common_count," +
            "relation," +
            "relatives," +
            "counters," +
            "screen_name," +
            "maiden_name," +
            "timezone," +
            "occupation," +
            "activities," +
            "interests," +
            "music," +
            "movies," +
            "tv," +
            "books," +
            "games," +
            "about," +
            "quotes," +
            "personal," +
            "friend_status," +
            "military," +
            "career";
    private static final Logger logger = LoggerFactory.getLogger(AbstractBotsExtension.class);
    protected final VkontakteApi api;
    protected final String name;
    protected Contact owner;
    protected Future<?> started;

    private boolean isRunnings;
    private boolean isLoaded;

    protected AbstractBotsExtension(String name, VkontakteApi api) {
        this.api = api;
        this.name = name;
        this.isRunnings = false;
        this.isLoaded = false;
        this.owner = null;
    }

    protected AbstractBotsExtension(String name) throws Exception {
        this(name, VkontakteApiImpl.load());
    }

    @Override
    public final String name() {
        return name;
    }

    @Override
    public final boolean isRunning() {
        return isRunnings;
    }

    @Override
    public final boolean isLoaded() {
        return isLoaded;
    }

    @Override
    public final Future<?> start() {
        if (!isLoaded) {
            throw new RuntimeException("Расширение предварительно должно быть загружено. Вызван метод load()");
        }
        if (!isRunning()) {
            isRunnings = true;
            try {
                owner();
                logger.debug("Запустились под пользователем " + owner.firstName + " " + owner.lastName);
                started = doStart();
            } catch (Exception ex) {
                isRunnings = false;
                logger.error("Не смогли запустить обработчик", ex);
            }
        }
        return started;
    }

    @Override
    public final void stop() {
        isRunnings = false;
        try {
            doStop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized long owner() throws Exception {
        if (owner == null) {
            Contact[] contacts = api.getUsers().list("" + api.idOwner(), DEFAULT_USER_FIELDS, null);
            if (contacts == null || contacts.length == 0) {
                throw new IOException("Не могу получить собственного пользователя");
            }
            owner = contacts[0];
        }
        return owner.id;
    }

    @Override
    public final void load() {
        if (!isLoaded())
            try {
                isLoaded = init();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
    }

    @Override
    public boolean isService() {
        return false;
    }

    protected abstract Future<?> doStart() throws Exception;

    protected abstract void doStop() throws Exception;

    protected abstract boolean init() throws Exception;
}
