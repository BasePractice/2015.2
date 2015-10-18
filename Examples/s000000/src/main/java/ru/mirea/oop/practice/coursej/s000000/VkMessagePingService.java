package ru.mirea.oop.practice.coursej.s000000;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit.Call;
import ru.mirea.oop.practice.coursej.vk.ext.ServiceBotsExtension;
import ru.mirea.oop.practice.coursej.vk.Messages;
import ru.mirea.oop.practice.coursej.vk.Result;
import ru.mirea.oop.practice.coursej.vk.entities.Contact;

import java.io.IOException;

public final class VkMessagePingService extends ServiceBotsExtension {
    private static final Logger logger = LoggerFactory.getLogger(VkMessagePingService.class);
    private final Messages msgApi;

    public VkMessagePingService() throws Exception {
        super("vk.services.EchoServer");
        this.msgApi = api.getMessages();
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
                Call<Result<Integer>> call = msgApi.send(
                        contact.id,
                        null,
                        null,
                        null,
                        msg.text,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                );
                try {
                    Integer idMessage = Result.call(call);
                    logger.debug("Сообщение отправлено " + idMessage);
                } catch (IOException ex) {
                    logger.error("Ошибка отправки сообщения", ex);
                }
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
