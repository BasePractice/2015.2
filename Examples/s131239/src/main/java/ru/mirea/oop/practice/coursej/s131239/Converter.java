package ru.mirea.oop.practice.coursej.s131239;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mirea.oop.practice.coursej.api.vk.entities.Contact;
import ru.mirea.oop.practice.coursej.impl.vk.ext.ServiceBotsExtension;

import java.util.HashMap;
import java.util.Map;

public final class Converter extends ServiceBotsExtension {
    private static final Logger logger = LoggerFactory.getLogger(Converter.class);
    HashMap<String, HashMap<String, Double>> map;
    private boolean isMapLoaded = false;

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
                    logger.debug(MessageFormat.format("Сообщение для {0}, не следует на него отвечать, оно исходящее", Contact.viewerString(contact)));
                    logger.debug(MessageFormat.format("Текст сообщения: {0}", msg.text));
                    break;
                }
                logger.debug(MessageFormat.format("Получили сообщение от: {0}", Contact.viewerString(contact)));

                if (!isMapLoaded) {
                    map = new HashMap<String, HashMap<String, Double>>();
                    BufferedReader reader;
                    String line;
                    try {
                        reader = new BufferedReader(new FileReader(new File(Configuration.getFileName("values.txt"))));
                        while ((line = reader.readLine()) != null) {
                            if (line.contains("/")) {
                                String[] strings = line.split("/");
                                if (map.containsKey(strings[0])) {
                                    map.get(strings[0]).put(strings[1], Double.parseDouble(strings[2]));
                                } else {
                                    map.put(strings[0], new HashMap<String, Double>());
                                    map.get(strings[0]).put(strings[1], Double.parseDouble(strings[2]));
                                }
                            }
                        }
                    } catch (FileNotFoundException e) {
                        logger.error("Не найден файл с базой конвертаций: ", e);
                    } catch (IOException e) {
                        logger.error("Ошибка чтения файла с базой конвертаций: ", e);
                    }
                    isMapLoaded = true;
                }

                if (msg.text.split(" ")[0].matches("[-+]?\\d+") && msg.text.split(" ").length == 2) {
                    int num = Integer.parseInt(msg.text.split(" ")[0]);
                    String predesc = msg.text.split(" ")[1];
                    String desc = predesc.replace("сантиметры", "см").replace("сантиметров", "см").
                            replace("сантиметра", "см").replace("сантиметр", "см").replace("баров", "бар").
                            replace("бары", "бар").replace("фута", "фут").replace("футов", "фут").
                            replace("микрона", "микрон").replace("микронов", "микрон").replace("дюйма", "дюйм").
                            replace("дюймов", "дюйм").replace("паскаля", "паскаль").replace("паскалей", "паскаль");

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
        return "Сервис конвертации величин \"Вконтакте\"";
    }
}