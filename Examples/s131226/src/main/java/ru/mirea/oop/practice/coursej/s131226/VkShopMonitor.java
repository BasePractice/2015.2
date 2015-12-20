package ru.mirea.oop.practice.coursej.s131226;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mirea.oop.practice.coursej.api.vk.MessagesApi;
import ru.mirea.oop.practice.coursej.api.vk.entities.Contact;
import ru.mirea.oop.practice.coursej.impl.vk.ext.ServiceBotsExtension;
import ru.mirea.oop.practice.coursej.s131226.impl.ParserApiImpl;

/**
 * -5?
 */
public final class VkShopMonitor extends ServiceBotsExtension {
    public static final String HELP_MESSAGE =
            "На данный  момент бот знает команды:\n" +
                    "бот help\n" +
                    "бот полный отчет\n" +
                    "бот краткий отчет\n" +
                    "бот обнови БД\n" +
                    "бот состояние БД";
    private static final Logger logger = LoggerFactory.getLogger(VkShopMonitor.class);
    private final ParserApi parser;

    public VkShopMonitor() throws Exception {
        super("vk.services.ShopMonitor");
        this.parser = new ParserApiImpl();
    }

    @Override
    protected void doEvent(Event event) {
        if (event.type == EventType.MESSAGE_RECEIVE) {
            Message msg = (Message) event.object;
            Contact contact = msg.contact;
            if (msg.text.startsWith("бот") && !msg.isOutbox()) {
                OutgoingMessage outgoingMessage = new OutgoingMessage(contact.id, api);
                logger.debug("Обращение к боту от " + Contact.viewerString(contact) + ", текст запроса: " + msg.text);
                switch (msg.text) {
                    case "бот help": {
                        outgoingMessage.setText(HELP_MESSAGE);
                        break;
                    }

                    case "бот полный отчет": {
                        outgoingMessage.setAttachment(parser.getReport());
                        outgoingMessage.setText("Полный отчет");
                        break;
                    }
                    case "бот краткий отчет": {
                        outgoingMessage.setText(parser.getChanges());
                        break;
                    }

                    case "бот обнови БД": {
                        outgoingMessage.setText("начинаю обновление БД, это займет некоторое время \n" +
                                "дождитесь уведомления о завершении");
                        outgoingMessage.send(messages);
                        parser.updateDB();
                        outgoingMessage.setText("Обновление прошло нормально");
                        break;
                    }
                    case "бот состояние БД": {
                        outgoingMessage.setText(parser.getDBState());
                        break;
                    }
                    default: {
                        outgoingMessage.setText("Неизвестная команда. Для получения справки используйте бот help.");
                        break;
                    }
                }
                Integer idMessage = outgoingMessage.send(messages);
                switch (idMessage) {
                    case 0: {
                        logger.debug("Ошибка отправки сообщения");
                        break;
                    }
                    default: {
                        logger.debug("Сообщение отправлено пользователю " + Contact.viewerString(contact) + ", текст сообщения: " + outgoingMessage.getText() + ", вложения: " + outgoingMessage.getAttachment());
                        break;
                    }
                }

            }
        }
    }


    @Override
    public String description() {
        return "Мониторинг интернет магазинов";
    }
}
