package ru.mirea.oop.practice.coursej.s131235;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.net.ssl.HttpsURLConnection;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

/**
 * Created by TopKek on 12.12.2015.
 */
public class Translator {
    private String textForTransl;
    private String langForTransl;


    public Translator(String langForTransl, String textForTransl) {
        this.textForTransl = textForTransl;
        this.langForTransl = langForTransl;

    }


    public static String translating(String lang, String text) throws IOException {
        String key = "trnsl.1.1.20151208T095415Z.967d846ac9275be4.62e517b3d9054079fa6aa57d02f15b1a0e1fc9ea";
        String urlStr = "https://translate.yandex.net/api/v1.5/tr.json/translate?key=" + key;

        URL urlObject = new URL(urlStr);
        HttpsURLConnection connection = (HttpsURLConnection) urlObject.openConnection();
        connection.setDoOutput(true);
        DataOutputStream outputStr = new DataOutputStream(connection.getOutputStream());
        outputStr.writeBytes("text=" + URLEncoder.encode(text, "UTF-8") + "&lang=" + lang);
        InputStream inpStr = connection.getInputStream();

        Scanner scan = new Scanner(inpStr);
        String json = scan.nextLine();

        JsonElement jsonPars = new JsonParser().parse(json);
        JsonObject jsObj = jsonPars.getAsJsonObject();
        String perevod = jsObj.get("text").getAsString();

        String secondString = new String(perevod.getBytes("windows-1251"), "UTF-8");

        if (text.equals(secondString)) {
            return "перевод не будет осуществлен";
        }
        return secondString;
    }
}
