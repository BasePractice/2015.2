package ru.mirea.oop.practice.coursej.s131239;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mirea.oop.practice.coursej.api.vk.entities.Contact;
import ru.mirea.oop.practice.coursej.impl.vk.ext.ServiceBotsExtension;

import java.util.HashMap;
import java.util.Map;

/**
 * -3?
 */
public final class Converter extends ServiceBotsExtension {
    private static final Logger logger = LoggerFactory.getLogger(Converter.class);

    public Converter() throws Exception {
        super("vk.services.Converter");
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
                    map.get("см").put("микрон", 10000.0);
                    map.get("см").put("нм", 10000000.0);
                    map.get("см").put("A", 100000000.0);
                    map.get("см").put("верста", 0.00000937382827);
                    map.get("см").put("межевая верста", 0.000004687);
                    map.get("см").put("косая сажень", 0.004032);
                    map.get("см").put("сажень", 0.004687);
                    map.get("см").put("маховая сажень", 0.005682);
                    map.get("см").put("аршин", 0.01406);
                    map.get("см").put("локоть", 0.02381);
                    map.get("см").put("пядь", 0.05624);
                    map.get("см").put("вершок", 0.225);
                    map.get("см").put("дюйм", 0.3937);
                    map.get("см").put("линия", 3.937);


                    map.put("бар", new HashMap<String, Double>());
                    map.get("бар").put("паскаль", 100000.0);
                    map.get("бар").put("кПа", 100.0);
                    map.get("бар").put("гПа", 1000.0);
                    map.get("бар").put("МПа", 0.1);
                    map.get("бар").put("атм", 0.9869);
                    map.get("бар").put("милибар", 1000.0);
                    map.get("бар").put("торр", 750.1);
                    map.get("бар").put("psi", 14.5);
                    map.get("бар").put("ksi", 0.0145);
                    map.get("бар").put("osi", 232.1);
                    StringBuilder msgToSend = new StringBuilder();
                    if (map.containsKey(desc)) {
                        for (Map.Entry<String, Double> stringDoubleEntry : map.get(desc).entrySet()) {
                            msgToSend.append(num).append(" ").append(desc).append(" = ").append(stringDoubleEntry.getValue() * num).append(" ").append(stringDoubleEntry.getKey()).append("\n");
                        }
                        sendMessage(contact, msgToSend.toString());
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