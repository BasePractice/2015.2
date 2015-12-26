package ru.mirea.oop.practice.coursej.s131238;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mirea.oop.practice.coursej.api.tg.entities.Message;
import ru.mirea.oop.practice.coursej.api.tg.entities.Update;
import ru.mirea.oop.practice.coursej.impl.tg.ext.ServiceBotsExtension;


public final class MagicBall extends ServiceBotsExtension {
    private static final Logger logger = LoggerFactory.getLogger(MagicBall.class);


    Answers answers = new Answers();
    public MagicBall() {
        super("tg.services.MagicEchoServer");
    }


    @Override
    protected void sendEvent(Update update) {

        Message message = update.message;
        if (message != null) {
            try {
                String text = message.text;
                Integer id = message.from.id;
                if (text.contains("?")){
                    client.get().sendMessage(id, answers.getRandomAnswer(), null);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public String description() {
        return "Сервис пересылки сообщений \"Telegram\"";
    }


}
