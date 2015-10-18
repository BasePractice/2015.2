package ru.mirea.oop.practice.coursej.vk.ext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit.Call;
import ru.mirea.oop.practice.coursej.VkApiImpl;
import ru.mirea.oop.practice.coursej.ext.BotsExtension;
import ru.mirea.oop.practice.coursej.vk.Result;
import ru.mirea.oop.practice.coursej.vk.Users;
import ru.mirea.oop.practice.coursej.vk.VkApi;
import ru.mirea.oop.practice.coursej.vk.entities.Contact;

import java.io.IOException;
import java.util.concurrent.Future;

abstract class AbstractBotsExtension implements BotsExtension {
    private static final Logger logger = LoggerFactory.getLogger(AbstractBotsExtension.class);
    protected final VkApi api;
    protected final String name;
    protected Contact owner;
    protected Future<?> started;

    private boolean isRunnings;
    private boolean isLoaded;

    protected AbstractBotsExtension(String name, VkApi api) {
        this.api = api;
        this.name = name;
        this.isRunnings = false;
        this.isLoaded = false;
        this.owner = null;
    }

    protected AbstractBotsExtension(String name) throws Exception {
        this(name, VkApiImpl.load());
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
    public synchronized long owner() throws IOException {
        if (owner == null) {
            Call<Result<Contact[]>> callable = api.getUsers().list("" + api.idOwner(), Users.DEFAULT_USER_FIELDS, null);
            Contact[] contacts = Result.call(callable);
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
