package ru.mirea.oop.practice.coursej.s131250;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mirea.oop.practice.coursej.api.vk.AccountApi;
import ru.mirea.oop.practice.coursej.api.vk.entities.Contact;
import ru.mirea.oop.practice.coursej.impl.vk.ext.ServiceBotsExtension;

import java.io.IOException;

/**
 * -5
 */
public final class VkWolframInteractionService extends ServiceBotsExtension {
    private static final Logger logger = LoggerFactory.getLogger(VkWolframInteractionService.class);
    private final AccountApi accApi;
    private static boolean isOnline = false;

    public VkWolframInteractionService() throws Exception {
        super("vk.services.Wolfram");
        this.accApi = api.getAccounts();
    }

    @Override
    protected void doEvent(Event event) {
        if (!isOnline) {
            try {
                accApi.setOnline(0);
                logger.debug("Бот вышел в Online");
                isOnline = true;
            } catch (IOException ex) {
                logger.error("Ошибка выхода в Online", ex);
            }
        }
        switch (event.type) {
            case MESSAGE_RECEIVE: {
                Message msg = (Message) event.object;
                Contact contact = msg.contact;
                if (msg.isOutbox()) {
                    logger.debug("Сообщение для " + Contact.viewerString(contact) +
                            ", не следует на него отвечать оно исходящее");
                    logger.debug("Текст сообщения: " + msg.text);
                    break;
                }
                logger.debug("Получили сообщение от " + Contact.viewerString(contact));

                if (msg.text.startsWith("wi ")) {
                    WAMessage message = null;
                    try {
                        messages.setActivity(msg.contact.id, "typing");
                    } catch (IOException e) {
                        logger.error("Ошибка отправки статуса \"typing\"", e);
                    }
                    try {
                        WAAction currentMessage = new WAAction(api);
                        message = currentMessage.getWAMessage(msg.text.split("wi ")[1]);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (message != null) {
                        sendMessage(contact, message.text, message.attachment);
                    }
                }
                break;
            }
            default:
                logger.debug("" + (event.object == null ? event.type : event.type + "|" + event.object));
        }
    }

    @Override
    public String description() {
        return "Сервис взаимодействия с WolframAlpha";
    }
}
