package ru.mirea.oop.practice.coursej.s131253;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mirea.oop.practice.coursej.api.vk.entities.Contact;
import ru.mirea.oop.practice.coursej.impl.vk.ext.ServiceBotsExtension;

import java.text.MessageFormat;

/**
 * -4
 */
public final class VkQuiz extends ServiceBotsExtension {

    private static final Logger logger = LoggerFactory.getLogger(VkQuiz.class);
    private final Game game = new Game();
    private Question justSanded; //Последний отправленный вопрос
    private boolean isTestEnded = true; //Завершёна ли викторина. По умолчанию, пока игра не началась, завершёна.
    private final int questionsCount = game.getQuestionsCount();

    public String description() {
        return "Сервис для проведения викторины/теста с пользователями";
    }

    public VkQuiz() throws Exception {
        super("vk.services.VkQuiz");
    }

    @Override
    protected void doEvent(Event event) {
        System.out.println(event.type.toString());

        switch (event.type) {
            case MESSAGE_RECEIVE: {  //Весь цикл бота протекает при событиях одного типа - входящих сообщениях

                Message msg = (Message) event.object;
                Contact contact = msg.contact;

                if (msg.isOutbox()) {
                    logger.debug(MessageFormat.format("Сообщение для {0}, не следует на него отвечать, оно исходящее", Contact.viewerString(contact)));
                    logger.debug(MessageFormat.format("Текст сообщения: {0}", msg.text));
                    break;
                }
                logger.debug(MessageFormat.format("Получили сообщение от: {0}", Contact.viewerString(contact)));

                if (msg.text.toLowerCase().equals("инфо") || msg.text.toLowerCase().equals("help")) {
                    sendMessage(contact, MessageFormat.format("Привет, я бот викторина! \n Чтобы сыграть, напишите «Начать викторину». \n Вам будет задано {0} вопросов. \n В ответах регистр (нижний/ВЕрХниЙ) не имеет значения. \n В вопросах-тестах присылайте номер правильного варианта.", questionsCount));
                    break;
                }

                if (msg.text.toLowerCase().equals("начать викторину") && !isTestEnded()) {
                    sendMessage(msg.contact, "Sorry! Кто-то уже играет в викторину. \n Попробуйте позже.");
                }

                if (game.getCountOfSanded() == questionsCount && !isTestEnded()) {
                    if (justSanded.getAnswer().toLowerCase().equals(msg.text.toLowerCase()) && contact.id == game.getIdPlayer()) {
                        game.scorePlus();
                        sendMessage(contact, "Правильно! \n");
                    }

                    if (!justSanded.getAnswer().toLowerCase().equals(msg.text.toLowerCase()) && contact.id == game.getIdPlayer()) {
                        sendMessage(contact, MessageFormat.format("Ошибка! Правильный ответ: {0}", justSanded.getAnswer()));
                    }

                    long testTime = System.currentTimeMillis() / 1000L - game.getStartTime();

                    if (game.getScore() == questionsCount) {
                        sendMessage(contact, MessageFormat.format("Тест завершён. \n Поздравляю, вы ответили правильно на все вопросы \n Ваше время: {0} секунд. \n Чтобы начать новую игру, напишите «Начать викторину»", testTime));
                        isTestEnded = true;
                    } else {
                        sendMessage(contact, MessageFormat.format("Тест завершён. \n Кол-во правильных ответов: {0}/{1} \n Ваше время: {2} секунд. \n Чтобы начать новую игру, напишите «Начать викторину»", game.getScore(), questionsCount, testTime));
                        isTestEnded = true;
                    }
                    game.setQuestionsFalse();
                    break;
                }

                if ((msg.text.toLowerCase().equals("начать викторину")) && isTestEnded()) {
                    game.restart();
                    game.setStarted(true);
                    isTestEnded = false;
                    game.putPlayerId(contact.id);
                    Question random = game.RandomQuestion();
                    sendMessage(contact, "Игра началась, отсчёт времени запущен! \n \n" +
                            "Итак, первый вопрос: \n" + random.getText());
                    justSanded = random;
                    game.currentQuest().setSanded(true);
                    game.plusCount();
                } else if (contact.id == game.getIdPlayer() && game.isStarted() && !isTestEnded()) {
                    if (justSanded.getAnswer().toLowerCase().equals(msg.text.toLowerCase()) && contact.id == game.getIdPlayer()) {
                        Question random = game.RandomQuestion();
                        game.scorePlus();
                        sendMessage(contact, MessageFormat.format("Правильно! \n Следующий вопрос: \n \n {0}", random.getText()));
                        justSanded = random;
                        game.currentQuest().setSanded(true);
                        game.plusCount();

                    } else if (contact.id == game.getIdPlayer() && game.isStarted() && !isTestEnded()) {
                        Question random = game.RandomQuestion();
                        sendMessage(contact, MessageFormat.format("Ошибка! Правильный ответ: {0} \n \n Следующий вопрос: \n {1}", justSanded.getAnswer(), random.getText()));
                        justSanded = random;
                        game.currentQuest().setSanded(true);
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

    private boolean isTestEnded() {
        return isTestEnded;
    }
}
