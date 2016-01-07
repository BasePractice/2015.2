package ru.mirea.oop.practice.coursej.impl.tw.ext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.*;

import java.util.concurrent.Future;

public abstract class ServiceBotsExtension extends AbstractBotsExtension implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(ServiceBotsExtension.class.getName());
    private final UserListener listener;
    private final Twitter twitter = TwitterFactory.getSingleton();
    protected final TwitterStream stream = TwitterStreamFactory.getSingleton();
    private volatile boolean isRunning;

    protected ServiceBotsExtension(String name) {
        super(name);
        this.isRunning = true;
        this.listener = new UserListener(this);
    }

    @Override
    public final boolean isService() {
        return true;
    }

    @Override
    public long owner() throws Exception {
        return stream.getId();
    }

    @Override
    public final void run() {
        logger.info("Запущен сервис оповещения");
        stream.addListener(listener);
        try {
            logger.debug("Запустились под пользователем {}", stream.getScreenName());
        } catch (TwitterException ex) {
            logger.error("Не мошу получить себя?", ex);
        }
        stream.user();
        while (isRunning) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                logger.error("Ошибка запроса к серверу", ex);
            }
        }
        stream.removeListener(listener);
        stream.shutdown();
        logger.info("Сервис оповещения остановлен");
    }

    @Override
    protected final Future<?> doStart() throws Exception {
        return executor.submit(this);
    }

    @Override
    protected final void doStop() throws Exception {
        isRunning = false;
    }

    @Override
    protected boolean init() throws Exception {
        login(stream, twitter);
        return true;
    }

    protected final void sendDirectMessage(long id, String text) throws TwitterException {
        twitter.sendDirectMessage(id, text);
    }

    private static final class UserListener extends UserStreamAdapter {
        private final ServiceBotsExtension ext;

        public UserListener(ServiceBotsExtension ext) {
            this.ext = ext;
        }

        @Override
        public void onStatus(Status status) {
            User user = status.getUser();
            try {
                if (user.getId() != ext.owner())
                    ext.statusEvent(status);
            } catch (TwitterException ex) {
                logger.error("Ошибка обработки статуса", ex);
            } catch (Exception ex) {
                logger.error("Ошибка получения себя?", ex);
            }
        }

        @Override
        public void onDirectMessage(DirectMessage message) {
            long id = message.getSenderId();
            try {
                if (ext.owner() != id)
                    ext.directMessage(message);
            } catch (TwitterException ex) {
                logger.error("Ошибка обработки сообщения", ex);
            } catch (Exception ex) {
                logger.error("Ошибка получения себя?", ex);
            }
        }

        @Override
        public final void onException(Exception ex) {
            logger.error("", ex);
        }
    }

    protected void directMessage(DirectMessage message) throws TwitterException {
    }

    protected void statusEvent(Status status) throws TwitterException {
    }
}
