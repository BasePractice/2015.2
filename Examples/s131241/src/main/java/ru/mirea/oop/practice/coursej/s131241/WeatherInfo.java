package ru.mirea.oop.practice.coursej.s131241;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mirea.oop.practice.coursej.api.vk.MessagesApi;
import ru.mirea.oop.practice.coursej.api.vk.entities.Contact;
import ru.mirea.oop.practice.coursej.impl.vk.ext.ServiceBotsExtension;
import ru.mirea.oop.practice.coursej.s131241.impl.OpenWeatherMapApiImpl;

import java.io.IOException;

public class WeatherInfo extends ServiceBotsExtension {
    public static final String DESCRIPTION = "Прогноз погоды на 7 дней по огромному количеству городов";
    private static final Logger logger = LoggerFactory.getLogger(WeatherInfo.class);
    private final OpenWeatherMapApi weatherApi;
    private final MessagesApi msgApi;

    public WeatherInfo() throws Exception {
        super("vk.services.WeatherInfo");
        this.weatherApi = new OpenWeatherMapApiImpl();
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
                String answer;
                try {
                    answer = weatherApi.getForecast(msg.text).getInlineForecast();
                } catch (Exception e) {
                    answer = "Не удалось получить прогноз :( \n Проверьте имя города и попробуйте снова.";
                }

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
//            default:
//                logger.debug("" + (event.object == null ? event.type : event.type + "|" + event.object));
        }

    }


    @Override
    public String description() {
        return DESCRIPTION;
    }

}
