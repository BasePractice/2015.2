package ru.mirea.oop.practice.coursej.s000000;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mirea.oop.practice.coursej.tg.entities.Update;
import ru.mirea.oop.practice.coursej.tg.ext.ServiceExtension;

public final class TgMessagePingService extends ServiceExtension {
    private static final Logger logger = LoggerFactory.getLogger(TgMessagePingService.class);

    public TgMessagePingService() {
        super("tg.services.EchoServer");
    }

    @Override
    protected void sendEvent(Update update) {
        logger.debug("Update: " + update.message);
    }

    @Override
    public String description() {
        return "Сервис пересылки сообщений \"Telegram\"";
    }

}
