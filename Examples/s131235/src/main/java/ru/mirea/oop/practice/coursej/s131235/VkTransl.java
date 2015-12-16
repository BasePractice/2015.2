package ru.mirea.oop.practice.coursej.s131235;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mirea.oop.practice.coursej.api.vk.entities.Contact;
import ru.mirea.oop.practice.coursej.impl.vk.ext.ServiceBotsExtension;

import java.io.IOException;

/**
 * Created by TopKek on 12.12.2015.
 */
public class VkTransl extends ServiceBotsExtension {
    private static final Logger logger = LoggerFactory.getLogger(VkTransl.class);
    private boolean alreadySend = false;

    public VkTransl() throws Exception {
        super("vk.services.VkTransl");
    }

    @Override
    protected void doEvent(Event event) {
        switch (event.type) {
            case MESSAGE_RECEIVE: {

                eventMessageReceive(event);
                break;
            }

        }
    }


    @Override
    public String description() {
        return "Сервис для перевода текста и слов \"Вконтакте\"";
    }


    public void eventMessageReceive(Event event) {

        if (alreadySend) {
            alreadySend = false;
            return;
        }

        Message msg = (Message) event.object;
        Contact contact = msg.contact;
        String help;
        String textLang;
        String textForTransl;
        String result = "";

        if (msg.text.equals("help")) {

            help = "Гайд по использованию бота> \n" +
                    "Отправьте боту сообщение> \n" +
                    "бот переведи на ** : ###" +
                    "\n" +
                    "Где ** - язык на который надо перевести" +
                    "\n" +
                    "где ### - текст который надо перевести" +
                    "\n" +
                    "список доступных языков: es, en, it, ru, ka, de ";
            sendMessage(contact.id, help);

        }

        if (msg.text.contains("бот переведи на") || msg.text.contains("Бот переведи на"))  {
            try {
                Parser useParser = new Parser(msg.text);
                textLang = useParser.getLanguage();
                textForTransl = useParser.getText();

                Translator transl = new Translator(textLang, textForTransl);
                result = transl.translating(textLang, textForTransl);
            }
            catch (IOException e) {
                System.err.println(" Ошибка при обращении к Переводчику Яндекса. (возможно ключ устарел) ");
            }
            catch (Exception e) {
                System.err.println("Ошибка Парсинга текста : \""+ msg.text +"\"" + "\n" + "текст не соответствует требованиям ввода");
            }
            sendMessage(contact.id, result);

        }
    }


    public void sendMessage(long id, String text) {
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
                    null,
                    null,
                    null

            );
            logger.debug("Сообщение отправлено " + idMessage);

            alreadySend = true;

        } catch (IOException ex) {
            logger.error("Ошибка отправки сообщения", ex);
        }


    }
}
