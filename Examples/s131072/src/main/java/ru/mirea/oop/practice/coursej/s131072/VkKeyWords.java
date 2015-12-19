package ru.mirea.oop.practice.coursej.s131072;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mirea.oop.practice.coursej.api.vk.entities.Contact;
import ru.mirea.oop.practice.coursej.impl.vk.ext.ServiceBotsExtension;

import java.text.MessageFormat;

/**
 * -5?
 */
public final class VkKeyWords extends ServiceBotsExtension {
    private static final Logger logger = LoggerFactory.getLogger(VkKeyWords.class);

    public VkKeyWords() throws Exception {
        super("vk.services.VkKeyWords");
        Keywords.loadKeywords();
    }

    @Override
    protected void doEvent(Event event) {
        switch (event.type) {
            case MESSAGE_RECEIVE: {
                Message msg = (Message) event.object;
                Contact contact = msg.contact;
                if (msg.isOutbox()) {
                    logger.debug(MessageFormat.format("Сообщение для {0}, не следует на него отвечать, оно исходящее", Contact.viewerString(contact)));
                    logger.debug(MessageFormat.format("Текст сообщения: {0}", msg.text));
                    break;
                }
                logger.debug(MessageFormat.format("Получили сообщение от: {0}", Contact.viewerString(contact)));
                String answer = Parser.parserMessage(msg.text);
                sendMessage(contact, answer);
                break;
            }
            default:
                logger.debug(MessageFormat.format("{0}", (event.object == null ? event.type : event.type + "|" + event.object)));
        }

    }

    @Override
    public String description() {
        return "Ответ на ключевые слова";
    }
}