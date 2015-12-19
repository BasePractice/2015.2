package ru.mirea.oop.practice.coursej.s131250;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit.Retrofit;

import java.net.URLEncoder;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

final class WARequestAction {
    private static final Logger logger = LoggerFactory.getLogger(WARequestAction.class);
    private static String appid = loadApplicationId();

    private static String loadApplicationId() {
        try {
            Properties prop = new Properties();
            prop.load(WARequestAction.class.getResourceAsStream("/appid.properties"));
            return prop.getProperty("appid");
        } catch (Exception e) {
            System.out.println(".appid load error");
            return null;
        }
    }

    public static ResponseBody getResponseFromWA(String input) {
        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(40, TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(40, TimeUnit.SECONDS);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.wolframalpha.com/")
                .client(okHttpClient)
                .build();
        WARequest service = retrofit.create(WARequest.class);
        try {
            logger.debug("Отправляем WA запрос " + URLEncoder.encode(input, "UTF-8"));
            return WARequestImpl.doWARequest(service, URLEncoder.encode(input, "UTF-8"), appid);
        } catch (Exception e) {
            //TODO: Дописать
            logger.error("", e);
        }
        return null;
    }
}
