package ru.mirea.oop.practice.coursej.s131235;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

/**
 * Created by TopKek on 12.12.2015.
 */
public class Translator {

   
    public static String translating(String lang, String text) throws IOException {
        String key = "trnsl.1.1.20151208T095415Z.967d846ac9275be4.62e517b3d9054079fa6aa57d02f15b1a0e1fc9ea";
        String urlForConnection = "https://translate.yandex.net/api/v1.5/tr.json/translate?key=" + key;

        URL urlObject = new URL(urlForConnection);
        HttpsURLConnection connection = (HttpsURLConnection) urlObject.openConnection();
        connection.setDoOutput(true);
        String json;

        try (DataOutputStream outputData = new DataOutputStream(connection.getOutputStream())) {
            outputData.writeBytes("text=" + URLEncoder.encode(text, "UTF-8") + "&lang=" + lang);
        }

        try (InputStream inputData = connection.getInputStream()){
            Scanner scan = new Scanner(inputData);
            json = scan.nextLine();
        }

        JsonElement jsonParsing = new JsonParser().parse(json);
        JsonObject jsObject = jsonParsing.getAsJsonObject();
        String translated = jsObject.get("text").getAsString();

        String secondString = new String(translated.getBytes("windows-1251"), "UTF-8");

        if (text.equals(secondString)) {
            return "перевод не будет осуществлен";
        }
        return secondString;
    }
}
