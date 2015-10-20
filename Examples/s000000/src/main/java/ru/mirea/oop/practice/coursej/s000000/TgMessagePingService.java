package ru.mirea.oop.practice.coursej.s000000;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mirea.oop.practice.coursej.api.tg.entities.Message;
import ru.mirea.oop.practice.coursej.api.tg.entities.Update;
import ru.mirea.oop.practice.coursej.impl.tg.ext.ServiceBotsExtension;

import java.io.InputStream;

public final class TgMessagePingService extends ServiceBotsExtension {
    private static final Logger logger = LoggerFactory.getLogger(TgMessagePingService.class);

    public TgMessagePingService() {
        super("tg.services.EchoServer");
    }

    @Override
    protected void sendEvent(Update update) {
        Message message = update.getMessage();
        if (message != null) {
            try {
                InputStream stream = TgMessagePingService.class.getResourceAsStream("/photos/safe_image.png");
                Integer id = message.from.id;
                if (stream != null) {
                    Message document = client.sendPhoto(id, message.text, "safe_image.png", stream);
                    logger.debug("Send message: " + document);
                }
                //client.sendMessage(id, text, null);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        //logger.debug("Update: " + update.message);
    }

    @Override
    public String description() {
        return "Сервис пересылки сообщений \"Telegram\"";
    }

}
