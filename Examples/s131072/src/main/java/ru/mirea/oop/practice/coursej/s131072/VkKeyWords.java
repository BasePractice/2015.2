package ru.mirea.oop.practice.coursej.s131072;
        import org.slf4j.Logger;
        import org.slf4j.LoggerFactory;
        import ru.mirea.oop.practice.coursej.api.vk.MessagesApi;
        import ru.mirea.oop.practice.coursej.api.vk.entities.Contact;
        import ru.mirea.oop.practice.coursej.impl.vk.ext.ServiceBotsExtension;

        import java.io.IOException;


/**
 * Created by Paul on 02.12.2015.
 */
public class VkKeyWords extends ServiceBotsExtension {

    private static final Logger logger = LoggerFactory.getLogger(VkKeyWords.class);

    private final MessagesApi msgApi;


    public VkKeyWords() throws Exception {
        super("vk.services.VkKeyWords");
        this.msgApi = api.getMessages();
        Keywords.loadKeywords();
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
                String answer = Parser.parserMessage(msg.text);
                try {
                    Integer idMessage = msgApi.send(
                            contact.id,
                            null,
                            null,
                            null,
                            answer,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null
                    );
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
        return "Ответ на ключевые слова";
    }
}