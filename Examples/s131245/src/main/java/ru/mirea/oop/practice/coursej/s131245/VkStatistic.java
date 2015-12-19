package ru.mirea.oop.practice.coursej.s131245;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mirea.oop.practice.coursej.api.vk.entities.Contact;
import ru.mirea.oop.practice.coursej.impl.vk.ext.ServiceBotsExtension;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/*
 Класс VkStatistic - основной класс модуля, отвечающий за сбор статистики, передача запроса на файл от
 пользователя другому классу, формирование ответа пользователю с вложенным файлом.
 */

/**
 * -5?
 */
public final class VkStatistic extends ServiceBotsExtension {
    private static final Logger logger = LoggerFactory.getLogger(VkStatistic.class);
    private final Map<Long, List<Session>> mapSession = new HashMap<>();
    private boolean alreadySend = false;
    private static final ThreadLocal<DateTimeFormatter> threadFormat = new ThreadLocal<>();
    private final DateScheduled dateScheduled = new DateScheduled();

    {
        mapSession.clear();
    }

    public VkStatistic() throws Exception {
        super("vk.services.VkStatistic");
    }

    //FIXME: Если DateTimeFormatter thread-safe - нужен ли class ThreadLocal?
    private static DateTimeFormatter getFormat() {
        DateTimeFormatter format = threadFormat.get();
        if (format == null) {
            format = DateTimeFormatter.ofPattern("HH:mm");
            threadFormat.set(format);
        }
        return format;
    }


    @Override
    protected void doEvent(Event event) {
        //Первоночальное заполнение статистки друзей, запись пользоватлей, находящихся на сайте во время запуска программы
        if (mapSession.isEmpty()) {
            firstPutOfFriends();
        }
        if (dateScheduled.isScheduled()) {
            Parser parser = new Parser("bot get: всех");
            Attachment attachment = new Attachment(mapSession, friends, api, parser);
            String attachmentName = null;
            try {
                attachmentName = attachment.getAttachmentName();
            } catch (Exception e) {
                e.printStackTrace();
            }
            sendMessage(owner.id, "отправка по расписанию ", attachmentName);
        }


        switch (event.type) {
            //Создание новой сессии для пользователя вошедшего на сайт
            case FRIEND_ONLINE: {
                eventOnline(event);
                break;
            }
            //Закрытие сессии пользователя
            case FRIEND_OFFLINE: {
                eventOffline(event);
                break;
            }
            //Получение запроса от на получение статистики.
            case MESSAGE_RECEIVE: {
                eventMessageReceive(event);
                break;
            }
            /*
             Сервис "Вконтакте" не представляет точной инофрмации обо всех пользователях, иногда уведомления о входе/выходе пользователя
             не приходят и поэтому  при Timeout будет происходить проход по всем пользователям на проверку каких-либо несостыковок
             */
            case TIMEOUT: {
                eventTimeout();
                break;
            }
            default:
                logger.debug("" + (event.object == null ? event.type : event.type + "|" + event.object));
                break;
        }


    }


    @Override
    public String description() {
        return "Сервис статистики пользователей \"Вконтакте\"";
    }

    void firstPutOfFriends() {
        updateFriends();
        for (Map.Entry<Long, Contact> friend : friends.entrySet()) {
            List<Session> arrayList = new ArrayList<>();
            if (friend.getValue().online == 1) {
                Session session = new Session(LocalDateTime.now());
                arrayList.add(session);
                mapSession.put(friend.getValue().id, arrayList);
                logger.debug(friend.getValue().lastName + " - онлайн");
            } else {
                mapSession.put(friend.getValue().id, arrayList);
                logger.debug(friend.getValue().lastName + " - оффлайн");
            }
        }

    }

    public void eventOnline(Event event) {
        //Пропускает пользователей, которые стали друзьями или перестали ими быть
        UserOnline userOnline;
        Long key;
        try {
            userOnline = (UserOnline) event.object;
            key = userOnline.getContact().id;
        } catch (Exception e) {
            logger.error("Лишний пользователь");
            return;
        }
        if (!mapSession.containsKey(key)) {
            return;
        }


        Session session = new Session(LocalDateTime.now());
        if (mapSession.get(key).isEmpty()) {
            mapSession.get(key).add(session);
        } else {
            try {
                int index = mapSession.get(key).size() - 1;
                if (mapSession.get(key).get(index).getEnd() == null) {
                    mapSession.get(key).remove(index);
                    mapSession.get(key).add(session);
                } else {
                    throw new Exception();
                }
            } catch (Exception e) {
                mapSession.get(key).add(session);
            }

        }
        logger.debug(event.object + " вошел в " + getFormat().format(mapSession.get(key).get(mapSession.get(key).size() - 1).getBegin()));

    }

    public void eventOffline(Event event) {
        //Пропускает пользователей, которые стали друзьями или перестали ими быть
        UserOffline userOffline;
        Long key;
        try {
            userOffline = (UserOffline) event.object;
            key = userOffline.getContact().id;
        } catch (Exception e) {
            logger.error("Лишний пользователь");
            return;
        }
        if (!mapSession.containsKey(key)) {
            return;
        }

        int index = mapSession.get(key).size() - 1;
        //Данная проверка необходима, если первоначальная инициализация вызвана кейсом Offline.
        if (index == -1) {
            return;
        }
        String name = ((UserOffline) event.object).getContact().firstName + " " + ((UserOffline) event.object).getContact().lastName;
        if (mapSession.get(key).get(index).getBegin() == null) {
            return;
        }
        mapSession.get(key).get(index).setEnd(LocalDateTime.now());
        logger.debug(name + " вышел в " + getFormat().format(mapSession.get(key).get(mapSession.get(key).size() - 1).getEnd()));
    }

    public void eventMessageReceive(Event event) {
        if (alreadySend) {
            alreadySend = false;
            return;
        }


        Message msg = (Message) event.object;
        Contact contact = msg.contact;
        String text;
        Attachment attachment = null;
        String attachmentName = null;
        if (contact.id == owner.id) {
            if (msg.text.contains("bot get")) {
                text = "Ошибка ввода";
                try {
                    Parser parser = new Parser(msg.text);
                    text = parser.getMsg();
                    attachment = new Attachment(mapSession, friends, api, parser);
                    attachmentName = attachment.getAttachmentName();
                } catch (Exception e) {
                    logger.error("Ошибка получения документа");
                }
            } else if (msg.text.equals("help")) {
                text = "Запрос состоит имеет вид\n1)bot get: Иван Иванов\n2)bog get: всех\n3)bot get: Иванов Иван, Иванова Ивана 10/04/2015\n";
            } else {
                return;
            }
            sendMessage(contact.id, text, attachmentName);
            if (attachment != null) {
                attachment.deleteFile(attachmentName);
            }
        }
    }

    void eventTimeout() {
        updateFriends();
        for (Map.Entry<Long, Contact> current : friends.entrySet()) {
            Long key = current.getValue().id;
            if (!mapSession.containsKey(key)) {
                continue;
            }
            if (!mapSession.get(key).isEmpty()) {
                int index = mapSession.get(key).size() - 1;
                Session lastSession = mapSession.get(key).get(index);
                if ((lastSession.getEnd() == null) && current.getValue().online == 0) {
                    lastSession.setEnd(LocalDateTime.now());
                } else if ((lastSession.getBegin() == null) && current.getValue().online == 1) {
                    Session session = new Session(LocalDateTime.now());
                    mapSession.get(key).add(session);
                }
            }

        }

    }

    void sendMessage(long id, String text, String attachmentName) {
        try {
            Integer idMessage = messages.send(
                    id,
                    null,
                    null,
                    null,
                    text,
                    null,
                    null,
                    null,
                    attachmentName,
                    null,
                    null

            );
            logger.debug("Сообщение отправлено {}", idMessage);
            alreadySend = true;
        } catch (IOException ex) {
            logger.error("Ошибка отправки сообщения", ex);
        } catch (Exception e) {
            logger.debug("Ошибка attachment");
            e.printStackTrace();
        }
    }


}






