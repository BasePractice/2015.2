package ru.mirea.oop.practice.coursej.s131237;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mirea.oop.practice.coursej.Configuration;
import ru.mirea.oop.practice.coursej.api.vk.AudioApi;
import ru.mirea.oop.practice.coursej.api.vk.FriendsApi;
import ru.mirea.oop.practice.coursej.api.vk.UsersApi;
import ru.mirea.oop.practice.coursej.api.vk.entities.Audio;
import ru.mirea.oop.practice.coursej.api.vk.entities.Contact;
import ru.mirea.oop.practice.coursej.impl.vk.ext.ClientBotsExtension;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.lang.Thread.sleep;


/**
 * Created by Shams on 10.12.2015.
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
    private String userId;

    public VkAudioStats() throws Exception {
        super("vk.services.VkAudioStats");
    }


    @Override
    public String description() {
        return "Сбор статистики музыкальных предпочтений";
    }


    public void loadParams() {
        Properties props = new Properties();
        InputStream is;

        try {
            File f = new File("server.properties");
            is = new FileInputStream( f );
        }
        catch ( Exception e ) { is = null; }

        try {
            if ( is == null ) {
                is = getClass().getResourceAsStream("server.properties");
            }
            props.load( is );
        }
        catch ( Exception e ) { }

        modeChoice  = props.getProperty("modeChoice", "exact"); // all || exact
        tracksCount = new Integer(props.getProperty("tracksCount", "30")); // maximum 8000
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


            //Scanner s = new Scanner(System.in);

            //logger.info("\nEnter \"all\" to handle all friends\nor \"exact\" to handle exact user\n");

            Contact[] contacts = new Contact[0]; //Получение массива пользователей
            switch (modeChoice) {
                case "all":
                    contacts = friendsApi.list(null, null, null, null, FRIENDS_FIELDS);
                    break;
                case "exact":
                    contacts = usersApi.list(null, null, FRIENDS_FIELDS);
                    break;
                default:
                    break;
            }

            //int tracksCount = s.nextInt();


            DateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy_HHmmss");
            Date date = new Date();


            String path = "C:" + File.separator + "VkMusic" + File.separator + "log" + dateFormat.format(date) + ".txt";
            File f = new File(path);
            f.getParentFile().mkdirs();
            try {
                f.createNewFile();
            } catch (IOException ex) {
                logger.error("Ошибка создания файла", ex);
            }

            //Проходим по всем контактам
            for (Contact contact : contacts) {

                logger.info("\n=====================================================\n\n");
                logger.info("обработка треков %s\n\n", Contact.viewerString(contact));

                friends.put(contact.id, contact);

                URIBuilder builder = new URIBuilder();
                JsonParser parser = new JsonParser();

                //Запрос всех трэков пользователя
                Audio[] tracks = audioApi.list(null, null, null, null, null, tracksCount);

                HashMap<String, Float> audioStats = new HashMap<>();

                float sum = 0;

                if (tracks != null) {
                    for (Audio track : tracks) {
                        try {
                            //Получаем название группы
                            String artistName = track.artist;

                            logger.info(artistName + " - " + track.title);
                            builder = new URIBuilder();

                            //Использование api developer.echonest.com
                            builder.setScheme("http").setHost("developer.echonest.com").setPath("/api/v4/artist/profile")
                                    .setParameter("api_key", "KLHMNCTQ90GNZUKBV") //ключ для усорения работы
                                    .setParameter("name", artistName)
                                    .setParameter("format", "json")
                                    .setParameter("bucket", "genre");

                            JsonArray genres = null;
                            try {
                                JsonObject genreArrayObject = parser.parse(getJSON(builder.toString())).getAsJsonObject();
                                genres = genreArrayObject.getAsJsonObject("response").getAsJsonObject("artist").getAsJsonArray("genres");
                            }
                            catch(Exception ex){
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
                        logger.info(String.format("%s - %.2f%%\n", entry.getKey(), entry.getValue()));
                    }
                } else {
                    logger.info("Аудиозаписи пользователя недоступны");
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
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));

                String line;
                StringBuilder sb = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                    sb.append(System.lineSeparator());
                }
                br.close();

                return sb.toString();
            } else {
                logger.info("RESP", "Ответ сервера: " + resp);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}

