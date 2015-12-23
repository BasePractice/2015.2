package ru.mirea.oop.practice.coursej.s131238;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mirea.oop.practice.coursej.api.tg.entities.Message;
import ru.mirea.oop.practice.coursej.api.tg.entities.Update;
import ru.mirea.oop.practice.coursej.impl.tg.ext.ServiceBotsExtension;

import java.io.InputStream;
import java.util.Random;

public final class MagicBall extends ServiceBotsExtension {
    private static final Logger logger = LoggerFactory.getLogger(MagicBall.class);
    static final String[] answers = {
            "Насколько я вижу да",
            "Спроси позже",
            "Лучше сейчас не говорить тебе",
            "Не могу сейчас сказать",
            "Соберись с мыслями и спроси снова",
            "Не рассчитывай на это",
            "Я так не думаю",
            "Я сомневаюсь насчёт этого",
            "Это бесспорно",
            "Да это так",
            "Может быть",
            "Наиболее вероятно",
            "Мои источники говорят нет",
            "Мои источники говорят да",
            "НЕТ!",
            "Перспектива хорошая",
            "Перспектива не очень хорошая",
            "Будущее туманно спроси позже",
            "Знаки указывают что да",
            "Знаки указывают что нет",
            "Извини, нет",
            "Очень сомневаюсь",
            "Без сомнения",
            "ДА!",
            "Определённо да",
            "Ты можешь надеяться на это"
    };
    Random random = new Random();
    public MagicBall() {
        super("tg.services.MagicEchoServer");
    }

    //Вот этот метод вызывается при получении ботом нового сообщения
    //Как это реализовано, не важно, этот делал препод(или кто-то)
    @Override
    protected void sendEvent(Update update) {
        //Ему передается некоторый объект update, который содержит в себе само новое сообщение и еще что-тоо
        //Собственно получаем сам объект этого сообщения
        Message message = update.message;
        if (message != null) {
            try {
                //Получаем текст присланного сообщения
                String text = message.text;
                //Получаем идентификатор человека, который прислал нам сообщение
                Integer id = message.from.id;
                //Если в присланном сообщении есть символ "?"
                if (text.contains("?")){
                    //Отправляем собщение человеку с id равному id того, кто нам прислал сообщение
                    //С текстом равным рандомному элементу из массива
                    //Почему client.get() потому что у нас есть объект, который методом get
                    //возвращает нашего бота, с помощью которого мы и отправляем сообщения
                    client.get().sendMessage(id, answers[random.nextInt(answers.length)], null);
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
