package ru.mirea.oop.practice.coursej.s131253;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mirea.oop.practice.coursej.api.vk.MessagesApi;
import ru.mirea.oop.practice.coursej.api.vk.entities.Contact;
import ru.mirea.oop.practice.coursej.impl.vk.ext.ServiceBotsExtension;

import java.io.IOException;


/**
 * Created by Александр on 27.11.2015.
 */

public class VkBot extends ServiceBotsExtension {

    private static final Logger logger = LoggerFactory.getLogger(VkBot.class);
    private Game game = new Game();
    private Question justSended; //Последний отправленный вопрос
    private boolean isTestEnded = true; //Завершёна ли викторина. По умолчанию, пока игра не началась, завершёна.
    private static final int QUESTIONS_COUNT = 8; //Из этого кол-ва вопросов будет состоять викторина.

    @Override
    public String description() {
        return "Сервис для проведения викторины/теста с пользователями";
    }

    public VkBot() throws Exception {
        super("vk.services.VkBot");
    }

    @Override
    protected void doEvent(Event event) {
        System.out.println(event.type.toString());

        switch (event.type) {
            case MESSAGE_RECEIVE: {  //Весь цикл бота протекает при событиях одного типа - входящих сообщениях

                Message msg = (Message) event.object;

                if (msg.isOutbox() || msg.contact.id == owner.id) {
                    System.out.println("ИСХОДЯЩЕЕ");
                    System.out.println("Текст сообщения: " + msg.text);
                    break;
                }

                Contact contact = msg.contact;

                if (msg.text.toLowerCase().equals("инфо")) {
                    sendMessage(contact.id, "Привет, я бот-викторина! \n Чтобы сыграть, напишите *Начать викторину*. \n" +
                            "Вам будет задано " + QUESTIONS_COUNT + " вопросов. \n В ответах регистр (нижний/ВЕрХниЙ) не имеет значения." +
                            " \n В вопросах-тестах присылайте номер правильного варианта." );
                    break;
                }

                if (msg.text.toLowerCase().equals("начать викторину") && !isTestEnded()) {
                    sendMessage(msg.contact.id, "Sorry! Кто-то уже играет в викторину. \n Попробуйте позже.");
                }

                // System.out.println("ПОЛУЧЕНО " + msg.text);

                Question random = game.RandomQuestion();

                if (game.getCountOfSended() == QUESTIONS_COUNT && !isTestEnded())  {
                    if (justSended.getAnswer().toLowerCase().equals(msg.text.toLowerCase()) && contact.id == game.getIdPlayer()) {
                        game.scorePlus();
                        sendMessage(contact.id, "Правильно! \n");
                    }

                    if (!justSended.getAnswer().toLowerCase().equals(msg.text.toLowerCase()) && contact.id == game.getIdPlayer()) {
                        sendMessage(contact.id, "Ошибка! Правильный ответ: "  + justSended.getAnswer() + "\n");
                    }

                    long testTime = System.currentTimeMillis() / 1000L - game.getStartTime();

                    if (game.getScore() == QUESTIONS_COUNT) {

                        sendMessage(contact.id, "Тест завершён. \n Поздравляю, вы ответили правильно на все вопросы"
                                        + "\n Ваше время: " + testTime + " секунд. \n " +
                                        "Чтобы начать новую игру, напишите *Начать викторину*");
                        isTestEnded=true;

                    } else {
                        sendMessage(contact.id, "Тест завершён. \n Кол-во правильных ответов: "
                                + game.getScore() + "/"+ QUESTIONS_COUNT+" \n Ваше время: " + testTime + " секунд. \n" +
                                " Чтобы начать новую игру, напишите *Начать викторину*");
                        isTestEnded=true;
                    }

                    game.setQuestionsFalse();
                    break;
                }


                if ((msg.text.toLowerCase().equals("начать викторину")) && isTestEnded()) {
                    game = new Game();
                    game.setStarted(true);
                    isTestEnded=false;
                    game.putPlayerId(contact.id);
                    random = game.RandomQuestion();
                    sendMessage(contact.id, "Игра началась, отсчёт времени запущен! \n \n" +
                            "Итак, первый вопрос: \n" + random.getText());
                    justSended = random;
                    game.currentQuest().setSended(true);
                    game.plusCount();

                } else if (contact.id == game.getIdPlayer() && game.isStarted() && !isTestEnded()) {

                    if (justSended.getAnswer().toLowerCase().equals(msg.text.toLowerCase()) && contact.id == game.getIdPlayer()) {
                        game.scorePlus();
                        sendMessage(contact.id,"Правильно! \n Следующий вопрос: \n \n " + random.getText());
                        justSended = random;
                        game.currentQuest().setSended(true);
                        game.plusCount();

                    } else if (contact.id == game.getIdPlayer() && game.isStarted() && !isTestEnded()) {
                        sendMessage(contact.id, "Ошибка! Правильный ответ: " + justSended.getAnswer() +
                                "\n \n Следующий вопрос: \n " + random.getText());
                        justSended = random;
                        game.currentQuest().setSended(true);
                        game.plusCount();
                    }
                }
                break;
            }


            default:
                logger.debug("" + (event.object == null ? event.type : event.type + "|" + event.object));
                break;
        }
    }

    public boolean isTestEnded () {
        return isTestEnded;
    }

    public void sendMessage(long id, String text) {  //Метод отправки сообщения text пользователю id.
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
        } catch (IOException ex) {
            logger.error("Ошибка отправки сообщения", ex);
        }
    }
}
