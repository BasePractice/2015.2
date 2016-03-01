package ru.mirea.oop.practice.coursej.s131332;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mirea.oop.practice.coursej.api.tg.entities.Message;
import ru.mirea.oop.practice.coursej.api.tg.entities.Update;
import ru.mirea.oop.practice.coursej.impl.tg.ext.ServiceBotsExtension;

import java.io.FileNotFoundException;
import java.io.IOException;


public final class TgData extends ServiceBotsExtension {
    private static final Logger logger = LoggerFactory.getLogger(TgData.class);
    private final String help = "Доступные комманды:\n/get Фамилия_писателя,\n/add Фамилия_писателя=Информация,\n/delete Фамилия_писателя,\n/update Фамилия_писателя=Информация\n";

    public TgData() {
        super("tg.services.DataEchoServer");
        DataBase.getParser();
    }

    @Override
    protected void sendEvent(Update update) {
        Message message = update.message;
        if (message != null) {
            try {
                logger.debug("Update: " + update.message);
                Integer id = message.from.id;
                String request = Parser.getRequest(message.text);
                if (request.equals("/help")) {
                    client.get().sendMessage(id, help, null);
                }

                if (request.equals("/get")) {
                    String answer = DataBase.getAnswer(message.text);
                    client.get().sendMessage(id, answer, null);
                }
                if (request.equals("/add")) {
                    String namePlusInfo = Parser.getNamePlusInfo(message.text);
                    DataBase.writeParser(namePlusInfo);
                    client.get().sendMessage(id, "Добавлено", null);
                }
                if (request.equals("/delete")) {
                    String requests = Parser.getNamePlusInfo(message.text);
                    DataBase.deleteParser(requests);
                    client.get().sendMessage(id, "Удалено", null);
                }
                if (request.equals("/update")) {
                    String requests = Parser.getNamePlusInfo(message.text);
                    DataBase.refreshParser(requests);
                    client.get().sendMessage(id, "Обновлено", null);
                }
            } catch (FileNotFoundException ex) {
                logger.error("Файл не найден", ex);
            } catch (IOException ex) {
                logger.error("Ошибка чтения файла", ex);
            }
        }
    }


    @Override
    public String description() {
        return "Сервис пересылки сообщений \"Telegram\"";
    }

}