package ru.mirea.oop.practice.coursej.impl.vk.ext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Future;

public abstract class ClientBotsExtension extends AbstractBotsExtension implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(ClientBotsExtension.class.getName());
    protected ClientBotsExtension(String name) throws Exception {
        super(name);
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
        updateFriends();
        try {
            doClient();
        } catch (Exception ex) {
            logger.error("Ошибка в работе клиента", ex);
        }
        logger.info("Клиент остановлен");
    }

    protected abstract void doClient() throws Exception;
}
