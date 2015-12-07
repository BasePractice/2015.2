package ru.mirea.oop.practice.coursej.s131239;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mirea.oop.practice.coursej.api.vk.MessagesApi;
import ru.mirea.oop.practice.coursej.api.vk.entities.Contact;
import ru.mirea.oop.practice.coursej.impl.vk.ext.ServiceBotsExtension;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class Converter extends ServiceBotsExtension {
    private static final Logger logger = LoggerFactory.getLogger(Converter.class);
    private final MessagesApi msgApi;

    public Converter() throws Exception {
        super("vk.services.Converter");
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

                if (msg.text.split(" ")[0].matches("[-+]?\\d+") && msg.text.split(" ").length == 2) {
                    int num = Integer.parseInt(msg.text.split(" ")[0]);
                    String predesc = msg.text.split(" ")[1];
                    HashMap<String, HashMap<String, Double>> map = new HashMap<String, HashMap<String, Double>>();
                    String desc = predesc.replace("сантиметры", "см").replace("сантиметров", "см").
                    replace("сантиметра", "см").replace("сантиметр", "см").replace("баров", "бар").
                    replace("бары", "бар");
                            map.put("см", new HashMap<String, Double>());
                    map.get("см").put("мм", 10.0);
                    map.get("см").put("м", 0.01);
                    map.get("см").put("дм", 0.1);
                    map.get("см").put("км", 0.00001);
                    map.get("см").put("фут", 0.0328084);

                    map.put("бар", new HashMap<String, Double>());
                    map.get("бар").put("паскаль", 100000.0);
                    map.get("бар").put("атм", 0.9869);

                    if (map.containsKey(desc)) {
                        for (Map.Entry<String, Double> stringDoubleEntry : map.get(desc).entrySet()) {
                            try {
                                Integer idMessage = msgApi.send(
                                        contact.id,
                                        null,
                                        null,
                                        null,
                                        num + " " + desc + " = " + stringDoubleEntry.getValue() * num + " " + stringDoubleEntry.getKey(),
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
                        }
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
        return "Сервис пересылки сообщения \"Вконтакте\"";
    }
}
