package ru.mirea.oop.practice.coursej.s131226;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mirea.oop.practice.coursej.api.vk.MessagesApi;
import ru.mirea.oop.practice.coursej.api.vk.entities.Contact;
import ru.mirea.oop.practice.coursej.impl.vk.ext.ServiceBotsExtension;
import ru.mirea.oop.practice.coursej.s131226.parser.ParserApi;
import ru.mirea.oop.practice.coursej.s131226.parser.ParserApiImpl;

public final class VkShopMonitor extends ServiceBotsExtension {
    public static final String HELP_MESSAGE = "Здесь скоро появится инструкция по работе с ботом, но пока ее нет =(";
    private static final Logger logger = LoggerFactory.getLogger(VkShopMonitor.class);
    private final MessagesApi msgApi;
    private final ParserApi parser;

    public VkShopMonitor() throws Exception {
        super("vk.services.ShopMonitor");
        this.msgApi = api.getMessages();
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
                    case "бот краткий отчет": {
                        break;
                    }
                    case "бот полный отчет": {
                        try {
                            outgoingMessage.setAttachment(parser.getReport());
                            outgoingMessage.setText("Полный отчет");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                    case "бот изменения": {
                        break;
                    }
                    case "бот обнови БД": {
                        outgoingMessage.setText("начинаю обновление БД, это займет некоторое время \n" +
                                "дождитесь уведомления о завершении");
                        outgoingMessage.send(msgApi);
                        try {
                            parser.updateDB();
                            outgoingMessage.setText("Обновление прошло нормально");
                        } catch (InterruptedException e) {
                            outgoingMessage.setText("База не обновилась =(");
                        }
                        break;
                    }
                    case "бот состояние базы": {
                        outgoingMessage.setText(parser.getDBState());
                        break;
                    }
                    default: {
                        outgoingMessage.setText("Неизвестная команда. Для получения справки используйте бот help.");
                        break;
                    }
                }
                Integer idMessage = outgoingMessage.send(msgApi);
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
