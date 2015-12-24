package ru.mirea.oop.practice.coursej.s131237;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mirea.oop.practice.coursej.api.vk.AudioApi;
import ru.mirea.oop.practice.coursej.api.vk.FriendsApi;
import ru.mirea.oop.practice.coursej.api.vk.UsersApi;
import ru.mirea.oop.practice.coursej.api.vk.entities.Audio;
import ru.mirea.oop.practice.coursej.api.vk.entities.Contact;
import ru.mirea.oop.practice.coursej.impl.vk.ext.ClientBotsExtension;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import static java.lang.Thread.sleep;

/**
 * -4?
 */
public final class VkAudioStats extends ClientBotsExtension {
    public static final Logger logger = LoggerFactory.getLogger(VkAudioStats.class);
    public final Map<Long, Contact> friends = new HashMap<>();
    private static final String FRIENDS_FIELDS = "nickname, " +
            "domain, " +
            "sex, " +
            "bdate, " +
            "city, " +
            "country, " +
            "timezone, " +
            "photo_50, " +
            "photo_100, " +
            "photo_200_orig, " +
            "has_mobile, " +
            "contacts, " +
            "education, " +
            "online, " +
            "relation, " +
            "last_seen, " +
            "status, " +
            "can_write_private_message, " +
            "can_see_all_posts, " +
            "can_post, " +
            "universities";
    private String modeChoice;
    private Integer tracksCount;
    private String key;
    private String friendToAnalizeId;

    public VkAudioStats() throws Exception {
        super("vk.services.VkAudioStats");
    }

    public String description() {
        return "Сбор статистики музыкальных предпочтений";
    }


    public void loadParams() {
        Properties prop = new Properties();
        try {
            prop.load(VkAudioStats.class.getResourceAsStream("/params"));
        } catch (Exception ex) {
            logger.info("Не удалось загрузить параметры");
        }

        modeChoice = prop.getProperty("modeChoice"); // all || exact
        tracksCount = new Integer(prop.getProperty("tracksCount")); // maximum 8000
        friendToAnalizeId = prop.getProperty("friendToAnalizeId");
        key = prop.getProperty("key", "KLHMNCTQ90GNZUKBV");
    }

    @Override
    protected void doClient() throws Exception {
        logger.info("Запущен сервис статистики аудиузаписей");
        loadParams();
        friends.clear();

        try {
            FriendsApi friendsApi = api.getFriends();
            UsersApi usersApi = api.getUsers();
            AudioApi audioApi = api.getAudios();

            Contact[] contacts = new Contact[0]; //Получение массива пользователей
            switch (modeChoice) {
                case "all":
                    contacts = friendsApi.list(null, null, null, null, FRIENDS_FIELDS);
                    break;
                case "exact":
                    contacts = usersApi.list(friendToAnalizeId, null, FRIENDS_FIELDS);
                    break;
                default:
                    break;
            }

            //Проходим по всем контактам
            for (Contact contact : contacts) {

                logger.info("=====================================================\n");
                logger.info(String.format("обработка треков %s\n", Contact.viewerString(contact)));

                friends.put(contact.id, contact);

                JsonParser parser = new JsonParser();
                Audio[] tracks = null;
                //Запрос всех трэков пользователя
                try {
                    tracks = audioApi.list(contact.id, null, null, null, null, tracksCount);
                }catch (Exception ex){
                    logger.error("Аудиозаписи пользователя недоступны");
                }
                HashMap<String, Float> audioStats = new HashMap<>();

                float sum = 0;

                if (tracks != null) {
                    for (Audio track : tracks) {
                        try {
                            //Получаем название группы
                            String artistName = track.artist;

                            logger.info(String.format("%s - %s", artistName, track.title));

                            String url = new StringBuilder("http://developer.echonest.com/api/v4/artist/profile?")
                                    .append("api_key=").append(key)
                                    .append("&name=").append(artistName.replace(' ', '+'))
                                    .append("&format=").append("json")
                                    .append("&bucket=").append("genre").toString();

                            JsonArray genres = null;
                            try {
                                JsonObject genreArrayObject = parser.parse(getJSON(url)).getAsJsonObject();
                                genres = genreArrayObject.getAsJsonObject("response").getAsJsonObject("artist").getAsJsonArray("genres");
                            } catch (Exception ex) {
                                logger.error("Артист не найден");
                            }

                            if (genres != null) {
                                float j = 0.7f;
                                int i = 0;
                                //Проход по всем жанрам
                                for (JsonElement genre : genres) {
                                    i++;
                                    if (i > 1 && i < 4) {
                                        j = 0.4f;
                                    } else if (i >= 4 && i <= 5) {
                                        j = 0.2f;
                                    } else if (i > 5) {
                                        j = 0.1f;
                                    }
                                    JsonObject genereObject = genre.getAsJsonObject();

                                    //Добавление в словарь
                                    if (audioStats.containsKey(genereObject.get("name").toString()))
                                        audioStats.put(genereObject.get("name").toString(), audioStats.get(genereObject.get("name").toString()) + j);
                                    else
                                        audioStats.put(genereObject.get("name").toString(), j);

                                    sum += j;
                                }
                            }
                            sleep(600); //Ограничение api developer.echonest.com

                        } catch (Exception ex) {
                            logger.error("Ошибка внешнего api", ex);
                        }
                    }

                    logger.info("=====================================================\n");
                    logger.info("Обработка трэков завершена\n");
                    logger.info("=====================================================\n");
                    logger.info("Статистика:\n");

                    for (Map.Entry<String, Float> entry : audioStats.entrySet()) {
                        if (sum != 0) {
                            entry.setValue((entry.getValue() / sum) * 100);
                        }
                    }

                    List<Map.Entry<String, Float>> genreList = new ArrayList<>(audioStats.entrySet());
                    Collections.sort(genreList, (e1, e2) -> e1.getValue().compareTo(e2.getValue())); //Сортировка жанров по значению (% от всех)
                    Collections.reverse(genreList);

                    //Вывод статистики в файл
                    for (Map.Entry<String, Float> entry : genreList) {
                        logger.info(String.format("%s - %.2f%%", entry.getKey(), entry.getValue()));
                    }
                }
            }
        } catch (Exception ex) {
            logger.error("Ошибка получения списка друзей", ex);
        }
    }

    public static String getJSON(String urle) {

        try {
            URL url = new URL(urle);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("Content-length", "0");
            con.setConnectTimeout(30000);

            con.connect();

            int resp = con.getResponseCode();
            if (resp == 200) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"))) {
                    String line;
                    StringBuilder sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                        sb.append(System.lineSeparator());
                    }
                    br.close();
                    return sb.toString();
                }
            } else {
                logger.info("RESP", "Ответ сервера: {}", resp);
            }

        } catch (Exception e) {
            logger.error("Ошибка получения ответа от developer.echonest.com");
        }

        return null;
    }
}

