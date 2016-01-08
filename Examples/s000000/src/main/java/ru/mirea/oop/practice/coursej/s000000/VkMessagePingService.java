package ru.mirea.oop.practice.coursej.s000000;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mirea.oop.practice.coursej.api.vk.entities.Contact;
import ru.mirea.oop.practice.coursej.impl.vk.ext.ServiceBotsExtension;

public final class VkMessagePingService extends ServiceBotsExtension {
    private static final Logger logger = LoggerFactory.getLogger(VkMessagePingService.class);

    public VkMessagePingService() throws Exception {
        super("vk.services.EchoServer");
    }

    @Override
    protected void doEvent(Event event) {
        switch (event.type) {
            case MESSAGE_RECEIVE: {
                Message msg = (Message) event.object;
                Contact contact = msg.contact;
                if (msg.isOutbox()) {
                    logger.debug("Сообщение для " + Contact.viewerString(contact)
                            + ", не следует на него отвечать оно исходящее");
                    logger.debug("Текст сообщения: " + msg.text);
                    break;
                }
                logger.debug("Получили сообщение от " + Contact.viewerString(contact));
                sendMessage(contact, msg.text.isEmpty() ? "Ответ" : msg.text, msg.attachments);
                break;
            }
            default:
                logger.debug("" + (event.object == null ? event.type : event.type + "|" + event.object));
        }

    }

    @Override
    public String description() {
        return "Сервис пересылки сообщения \"Вконтакте\"";
    }
}
