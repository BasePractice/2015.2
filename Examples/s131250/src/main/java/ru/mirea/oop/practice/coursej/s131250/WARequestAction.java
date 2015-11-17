package ru.mirea.oop.practice.coursej.s131250;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.ResponseBody;
import retrofit.Retrofit;
import ru.mirea.oop.practice.coursej.Configuration;

import java.net.URLEncoder;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

final class WARequestAction {
    private static final Logger logger = LoggerFactory.getLogger(WARequestAction.class);
    private static String appid = "";

    public static ResponseBody getResponsefromWA(String input) {
        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(40, TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(40, TimeUnit.SECONDS);
        try {
            Properties prop = new Properties();
            prop.load(Configuration.loadFrom(".WAID"));
            appid = prop.getProperty("APPID");
        } catch (Exception e) {
            System.out.println(".WAID load error");
            //FIXME: Убивание процесса не лучший вариант
            return null;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.wolframalpha.com/")
                .client(okHttpClient)
                .build();
        WARequest service = retrofit.create(WARequest.class);
        try {
            logger.debug("Отправляем WA запрос " + URLEncoder.encode(input, "UTF-8"));
            return WARequestImpl.doWARequest(service, URLEncoder.encode(input, "UTF-8"), appid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
