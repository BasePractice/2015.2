package ru.mirea.oop.practice.coursej.s131235;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mirea.oop.practice.coursej.api.vk.entities.Contact;
import ru.mirea.oop.practice.coursej.impl.vk.ext.ServiceBotsExtension;

import java.io.IOException;

/**
 * -5
 */
public final class VkTranslator extends ServiceBotsExtension {
    private static final Logger logger = LoggerFactory.getLogger(VkTranslator.class);
    private boolean alreadySend = false;

    public VkTranslator() throws Exception {
        super("vk.services.VkTranslator");
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
        String textLangFirst;
        String textLangSecond;
        String textForTransl;
        String result = "";

        if (msg.text.equals("help")) {
            help = "Гайд по использованию бота> \n" +
                    "Отправьте боту сообщение> \n" +
                    " lang1 lang2 : text" +
                    "\n" +
                    "Где lang1 - язык с которого" +
                    "\n" +
                    "Где lang2 - язык на который надо перевести" +
                    "\n" +
                    "где text - текст который надо перевести" +
                    "\n" +
                    "вместо lang1 можно ввести **, тогда язык будет выбран автоматически" +
                    "\n" +
                    "список доступных языков: es, en, it, ru, ka, de ";
            int idMessage = sendMessage(contact, help);
            alreadySend = idMessage >= 0;
        }

        if (msg.text.contains(":")) {
            try {
                Parser useParser = new Parser(msg.text);
                textLangFirst = useParser.getLanguageFirst();
                textLangSecond = useParser.getLanguageSecond();
                textForTransl = useParser.getText();
                if (textLangFirst.equals("**")) {
                    result = Translator.translating(textLangSecond, textForTransl);
                } else {
                    String combined = textLangFirst +
                            "-" +
                            textLangSecond;
                    result = Translator.translating(combined, textForTransl);
                }
            } catch (IOException e) {
                logger.error(" Ошибка при обращении к Переводчику Яндекса. (возможно ключ устарел) ");
            } catch (Exception e) {
                logger.info("Невозможен разбор текста : \"{}\"\nтекст не соответствует требованиям ввода", msg.text);
            }
            int idMessage = sendMessage(contact, result);
            alreadySend = idMessage >= 0;
        }
    }
}
