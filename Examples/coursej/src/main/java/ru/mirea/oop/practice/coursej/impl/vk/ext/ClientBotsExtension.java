package ru.mirea.oop.practice.coursej.impl.vk.ext;

import ru.mirea.oop.practice.coursej.api.vk.FriendsApi;
import ru.mirea.oop.practice.coursej.api.vk.entities.Contact;

import java.util.concurrent.Future;

public abstract class ClientBotsExtension extends AbstractBotsExtension implements Runnable {
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
        try {
            doClient();
        } catch (Exception ex) {
            logger.error("Ошибка в работе клиента", ex);
        }
        logger.info("Клиент остановлен");
    }

    protected abstract void doClient() throws Exception;
}
