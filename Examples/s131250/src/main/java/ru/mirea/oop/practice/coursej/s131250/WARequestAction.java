package ru.mirea.oop.practice.coursej.s131250;


import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.ResponseBody;
import org.apache.commons.httpclient.util.URIUtil;
import retrofit.Retrofit;
import ru.mirea.oop.practice.coursej.Configuration;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class WARequestAction {
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
            System.exit(0);
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.wolframalpha.com/")
                .client(okHttpClient)
                .build();
        WARequest service = retrofit.create(WARequest.class);
        try {
            System.out.println("REQUEST TO WA FOR INPUT " + URIUtil.encodeAll(input));
            return WARequestImpl.doWARequest(service, URIUtil.encodeAll(input), appid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
