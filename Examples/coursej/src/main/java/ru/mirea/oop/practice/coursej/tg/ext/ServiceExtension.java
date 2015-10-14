package ru.mirea.oop.practice.coursej.tg.ext;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mirea.oop.practice.coursej.ClientFactory;
import ru.mirea.oop.practice.coursej.Configuration;
import ru.mirea.oop.practice.coursej.ext.Extension;
import ru.mirea.oop.practice.coursej.tg.BotClient;
import ru.mirea.oop.practice.coursej.tg.entities.Update;
import ru.mirea.oop.practice.coursej.tg.entities.User;

import java.io.IOException;
import java.util.concurrent.Future;

public abstract class ServiceExtension implements Extension, Runnable {
    private static final Logger logger = LoggerFactory.getLogger(ServiceExtension.class);
    protected final BotClient client;
    private final String name;
    private volatile boolean isRunning = false;
    protected User owner;

    protected ServiceExtension(String name) {
        this.client = new BotClient(Configuration.loadKeyFrom(".telegram"), ClientFactory.createOkClient());
        this.name = name;
    }

    @Override
    public final String name() {
        return name;
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
                lastUpdate = update.id + 1;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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

    private Integer lastUpdate;

    private Update[] waitUpdates() {
        try {
            return client.getUpdates(lastUpdate, null, 1);
        } catch (Exception ex) {
            logger.error("Ошибка при получении обновления", ex);
            return new Update[0];
        }
    }
}
