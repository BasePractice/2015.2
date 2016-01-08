package ru.mirea.oop.practice.coursej.s131330;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mirea.oop.practice.coursej.api.tg.entities.Message;
import ru.mirea.oop.practice.coursej.api.tg.entities.Update;
import ru.mirea.oop.practice.coursej.impl.tg.ext.ServiceBotsExtension;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;

public final class TgImageReply extends ServiceBotsExtension {
    private static final Logger logger = LoggerFactory.getLogger(TgImageReply.class);
    private OkHttpClient requestClient = new OkHttpClient();

    private final String descriptionBot = "Приветствую!\n\nЯ обычный бот, который присылает тебе забавные картинки, когда ты пишешь любое слово.\nТолько не налегай: я устаю после 100 запросов :(\n\nПоехали!";
    private final String startBot = "Доступные комманды:\n\n/help - выводит справку по боту,\n/start - список доступных комманд.\n\nЕсли я сейчас нахожусь в чате, то обращайтесь ко мне по такому шаблону:\n\n  */ (ВАШ ЗАПРОС)* ";


    public TgImageReply() {
        super("tg.services.TgImageReply");
    }

    @Override
    protected void sendEvent(Update update) {


        Message message = update.message;
        if (message != null) {
            try {
                logger.debug("Update: " + update.message);

                Integer id = message.chat.id;
                String text = message.text;
                logger.debug("Text: " + message.text);

                if (message.newUserInChat != null) {
                    client.get().sendMessage(id, startBot, null);
                }

                if (text.equals("/help") || text.equals("/help@ImageDealerBot")){
                    client.get().sendMessage(id, descriptionBot, null);
                    return;
                }
                if (text.equals("/start") || text.equals("/start@ImageDealerBot")){
                    client.get().sendMessage(id, startBot, null);
                    return;
                }

                try (InputStream stream = getImage(text)) {
                    if (stream != null) {
                        Message document = client.get().sendPhoto(id, "", "safe_image.png", stream);
                        logger.debug("Send message: " + document);
                    }
                }
            } catch (IOException ioExp) {
                ioExp.printStackTrace();
            } catch (NullPointerException npeExp) {
                logger.debug("Один из параметром Message (принятый или отправленный) равен null");
            }
        }
    }

    InputStream getImage(String req)throws IOException{

        String apiGoogleKey = "";
        String apiGoogleEngineCode = "";
        String urlGoogle = "";

        try {
            Properties prop = new Properties();
            prop.load(TgImageReply.class.getResourceAsStream("/keys&url.txt"));
            apiGoogleKey = prop.getProperty("apiGoogleKey");
            apiGoogleEngineCode = prop.getProperty("apiGoogleEngineCode");
            urlGoogle = prop.getProperty("urlGoogle");
        } catch (NullPointerException fnfeExp){
            logger.error("Не найден файл с ключами", fnfeExp);
        } catch (IOException ioExp) {
            logger.error("Ошибка чтения файла с ключами", ioExp);
        }

        Random random = new Random();
        String url = urlGoogle + "searchType=image&num=1"
                + "&start=" + (random.nextInt(100) + 1) //побольше разброс
                + "&key=" + apiGoogleKey + "&cx=" + apiGoogleEngineCode
                + "&q=" + req;

        Request request = new Request.Builder()
                .url(url)
                .build();

        //Парсинг
        Response response = requestClient.newCall(request).execute();

        JsonParser parser = new com.google.gson.JsonParser();
        try {
            if (response.isSuccessful()) {
                JsonObject root = (JsonObject) parser.parse(response.body().string()); //корень
                JsonArray items = (JsonArray) root.get("items"); //массив
                JsonObject image = (JsonObject) items.get(0); //строчка с картинкой
                String link = image.get("link").getAsString();//линка

                logger.debug("Link: " + link);
                request = new Request.Builder()
                        .url(link)
                        .build();
            } else {
                logger.debug("Первый запрос не был выполнен");
                return null;
            }
            response = requestClient.newCall(request).execute();
            if(response.isSuccessful()) {
                return response.body().byteStream();
            } else {
                logger.debug("Второй запрос не был выполнен");
                return null;
            }
        } catch (IOException ioExp) {
            logger.debug("Error:");
            ioExp.printStackTrace();
            return null;
        }
    }

    @Override
    public String description() {
        return "Сервис вывода картинок на соответствующий запрос \"Telegram\"";
    }

}
