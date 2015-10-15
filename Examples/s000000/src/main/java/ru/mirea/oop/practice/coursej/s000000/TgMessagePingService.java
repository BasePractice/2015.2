package ru.mirea.oop.practice.coursej.s000000;


import com.squareup.okhttp.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mirea.oop.practice.coursej.tg.entities.Message;
import ru.mirea.oop.practice.coursej.tg.entities.Update;
import ru.mirea.oop.practice.coursej.tg.ext.ServiceExtension;

import java.io.File;

public final class TgMessagePingService extends ServiceExtension {
    private static final Logger logger = LoggerFactory.getLogger(TgMessagePingService.class);

    public TgMessagePingService() {
        super("tg.services.EchoServer");
    }

    @Override
    protected void sendEvent(Update update) {
        Message message = update.getMessage();
        if (message != null) {
            try {
                String fileName = "/Users/pastor/Яндекс.Диск/10466053_10152747634924902_1628544003_n.mp4";
                Integer id = message.getFrom().id;
                String text = message.getText();
                File file = new File(fileName);
                if (file.exists()) {
                    Message document = client.sendDocument(id, "", MediaType.parse("video/mp4"), file);
                    System.out.println(document);
                }
                //client.sendMessage(id, text, null);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        logger.debug("Update: " + update.message);
    }

    @Override
    public String description() {
        return "Сервис пересылки сообщений \"Telegram\"";
    }

}
