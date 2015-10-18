package ru.mirea.oop.practice.coursej.s131250;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit.Call;
import ru.mirea.oop.practice.coursej.vk.Messages;
import ru.mirea.oop.practice.coursej.vk.Result;
import ru.mirea.oop.practice.coursej.vk.entities.Contact;
import ru.mirea.oop.practice.coursej.vk.ext.ServiceBotsExtension;

import java.io.IOException;

public final class VkWolframInteractionService extends ServiceBotsExtension {
    private static final Logger logger = LoggerFactory.getLogger(VkWolframInteractionService.class);
    private final Messages msgApi;

    public VkWolframInteractionService() throws Exception {
        super("vk.services.Wolfram");
        this.msgApi = api.getMessages();
    }

    @Override
    protected void doEvent(Event event) {
        switch (event.type) {
            case MESSAGE_RECEIVE: {
                Message msg = (Message) event.object;
                Contact contact = msg.contact;
                if (msg.isOutbox()) {
                    logger.debug("Сообщение для " + Contact.viewerString(contact) +
                            ", не следует на него отвечать оно исходящее");
                    logger.debug("Текст сообщения: " + msg.text);
                    break;
                }
                logger.debug("Получили сообщение от " + Contact.viewerString(contact));


                if (msg.text.equals("bot hello")) {
                    if (BotUsersAction.isRegistered(msg.contact.id)) {
                        String text = "Вы уже зарегистрированы как пользователь бота WolframAlpha! \n" +
                                "Для отправки выражения на решение добавьте перед ним символы \"wi\". Например, \"wi integrate sin (x)\". Команды, не начинающиеся с этих символов, ботом игнорируются.\n" +
                                "Возможности и примеры команд: http://www.wolframalpha.com/examples/.";
                        Call<Result<Integer>> call = msgApi.send(
                                contact.id,
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
                        try {
                            Integer idMessage = Result.call(call);
                            logger.debug("Сообщение отправлено " + idMessage);
                        } catch (IOException ex) {
                            logger.error("Ошибка отправки сообщения", ex);
                        }
                    } else {
                        BotUsersAction.register(msg.contact.id);
                        String text = "Вы успешно зарегистрированы как пользователь бота WolframAlpha!\n" +
                                "Для отправки выражения на решение добавьте перед ним символы \"wi\". Например, \"wi integrate sin (x)\". Команды, не начинающиеся с этих символов, ботом игнорируются.\n" +
                                "Возможности и примеры команд: http://www.wolframalpha.com/examples/.";
                        Call<Result<Integer>> call = msgApi.send(
                                contact.id,
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
                        try {
                            Integer idMessage = Result.call(call);
                            logger.debug("Сообщение отправлено " + idMessage);
                        } catch (IOException ex) {
                            logger.error("Ошибка отправки сообщения", ex);
                        }
                    }


                }


                if ((BotUsersAction.isRegistered(msg.contact.id))) {
                    if (msg.contact.id == 98666021) {
                        if (msg.text.equals("bot admin")) {
                            String os = System.getProperty("os.name");
                            String text = "Информация для администратора бота\n" +
                                    "=====================\n" +
                                    "Статус бота: работает.\n" +
                                    "Операционная система: " + os + ".\n" +
                                    "Пользователей: " + BotUsersAction.getCount() + ".\n" +
                                    "Следующие команды предлагаются для тестирования функций бота и возможностей WolframAlpha.\n" +
                                    "\n" +
                                    "Системные команды:\n" +
                                    "bot admin (информация для администратора)\n" +
                                    "bot hello (регистрация нового пользователя)\n" +
                                    "bot help (справка для пользователя)\n" +
                                    "\n" +
                                    "Тест команды wi (вывод одним файлом):\n" +
                                    "wi weather forecast in Moscow\n" +
                                    "wi 9620 to binary\n" +
                                    "wi integrate (x^2)dx/(x^2 - 5)\n" +
                                    "wi sin (x)\n" +
                                    "wi 3х-2х2+1=-1\n" +
                                    "wi plot x^2+1\n" +
                                    "wi P && (Q || R)\n" +
                                    "wi 120 meters\n" +
                                    "wi population of India in 2030\n" +
                                    "wi #FE3456\n" +
                                    "wi 5, 14, 23, 32, 41, ...\n" +
                                    "wi y'' + y = 0\n" +
                                    "wi integrate sin x dx from x=0 to pi\n" +
                                    "wi plot x^2+y^2<1 and y>x\n" +
                                    "wi mass proton / electron\n" +
                                    "wi work F=30N, d=100m\n" +
                                    "wi Morse code \"VK bot\"\n" +
                                    "wi port 80\n" +
                                    "wi .pdf file format\n" +
                                    "wi parametric plot (sin 10t, sin 7t), t=0..2pi";
                            Call<Result<Integer>> call = msgApi.send(
                                    contact.id,
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
                            try {
                                Integer idMessage = Result.call(call);
                                logger.debug("Сообщение отправлено " + idMessage);
                            } catch (IOException ex) {
                                logger.error("Ошибка отправки сообщения", ex);
                            }
                        }
                    }

                    if (msg.text.equals("bot help")) {
                        String text = "Справка по боту WolframAlpha.\nДля отправки выражения на решение добавьте перед ним символы \"wi\". Например, \"wi integrate sin (x)\". Команды, не начинающиеся с этих символов, ботом игнорируются.\nВозможности и примеры команд: http://www.wolframalpha.com/examples/.";
                        Call<Result<Integer>> call = msgApi.send(
                                contact.id,
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
                        try {
                            Integer idMessage = Result.call(call);
                            logger.debug("Сообщение отправлено " + idMessage);
                        } catch (IOException ex) {
                            logger.error("Ошибка отправки сообщения", ex);
                        }
                    }


                    if (msg.text.startsWith("wi ")) {
                        WAMessage message = null;
                        try {
                            message = WAAction.processWAMessage(msg.text.split("wi ")[1], api);
                        } catch (Exception e) {
                            System.out.println("Exception, waiting...");
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                        }

                        if (message != null) {
                            Call<Result<Integer>> call = msgApi.send(
                                    contact.id,
                                    null,
                                    null,
                                    null,
                                    message.text,
                                    null,
                                    null,
                                    null,
                                    message.attachment,
                                    null,
                                    null
                            );
                            try {
                                Integer idMessage = Result.call(call);
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
        return "Сервис взаимодействия с WolframAlpha";
    }
}
