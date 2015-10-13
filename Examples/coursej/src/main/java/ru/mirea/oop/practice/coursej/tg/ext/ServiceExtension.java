package ru.mirea.oop.practice.coursej.tg.ext;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mirea.oop.practice.coursej.ClientFactory;
import ru.mirea.oop.practice.coursej.ext.Extension;
import ru.mirea.oop.practice.coursej.tg.BotClient;
import ru.mirea.oop.practice.coursej.tg.entities.Update;
import ru.mirea.oop.practice.coursej.tg.entities.User;

import java.io.IOException;
import java.util.concurrent.Future;

public abstract class ServiceExtension implements Extension, Runnable {
    private static final Logger logger = LoggerFactory.getLogger(ServiceExtension.class);
    private final BotClient client;
    private volatile boolean isRunning = false;
    protected User owner;

    protected ServiceExtension() {
        this.client = new BotClient("", ClientFactory.createOkClient());
    }

    @Override
    public long owner() throws IOException {
        if (owner != null)
            return owner.id;
        fetchOwner();
        if (owner != null)
            return owner.id;
        return -1;
    }

    @Override
    public final boolean isService() {
        return true;
    }

    @Override
    public final boolean isRunning() {
        return isRunning;
    }

    @Override
    public boolean isLoaded() {
        return true;
    }

    @Override
    public void stop() {
        isRunning = false;
    }

    @Override
    public Future<?> start() {
        return executor.submit(this);
    }

    @Override
    public void load() {
    }

    @Override
    public void run() {
        isRunning = true;
        fetchOwner();
        if (owner != null)
            logger.debug("Запустились под пользователем " + owner.username);
        while (isRunning()) {
            Update[] updates = waitUpdates();
            for (Update update : updates) {
                sendEvent(update);
            }
        }
    }

    protected abstract void sendEvent(Update update);

    private void fetchOwner() {
        try {
            owner = client.getMe();
        } catch (Exception ex) {
            logger.error("", ex);
        }
    }

    private Update[] waitUpdates() {
        try {
            return client.getUpdates();
        } catch (Exception ex) {
            logger.error("Ошибка при получении обновления", ex);
            return new Update[0];
        }
    }
}
