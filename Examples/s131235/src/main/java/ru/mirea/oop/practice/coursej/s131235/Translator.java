package ru.mirea.oop.practice.coursej.s131235;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Properties;
import java.util.Scanner;

final class Translator {
    private static final Logger logger = LoggerFactory.getLogger(Translator.class);
    private static final YaInformation ya = YaInformation.load();

    private Translator() {
    }

    public static String translating(String langFirst, String text) throws IOException {
        if (ya == null)
            return text;
        URL urlObject = new URL(ya.url);
        HttpsURLConnection connection = (HttpsURLConnection) urlObject.openConnection();
        connection.setDoOutput(true);
        String json;

        try (DataOutputStream outputData = new DataOutputStream(connection.getOutputStream())) {
            outputData.writeBytes("text=" + URLEncoder.encode(text, "UTF-8") + "&lang=" + langFirst);
        }

        try (InputStream inputData = connection.getInputStream()) {
            Scanner scan = new Scanner(inputData);
            json = scan.nextLine();
        }

        JsonElement jsonParsing = new JsonParser().parse(json);
        JsonObject jsObject = jsonParsing.getAsJsonObject();
        String translated = jsObject.get("text").getAsString();
        if (text.equals(translated)) {
            return "перевод не будет осуществлен";
        }
        return translated;
    }

    private static final class YaInformation {
        private final String url;

        private YaInformation(String key, String query) {
            this.url = query + key;
        }

        private static YaInformation load() {
            try (InputStream stream = Translator.class.getResourceAsStream("/ya.properties")) {
                Properties p = new Properties();
                p.load(stream);
                return new YaInformation(p.getProperty("key"), p.getProperty("query"));
            } catch (IOException ex) {
                logger.error("Не могу считать параметры", ex);
                return null;
            }
        }
    }
}

