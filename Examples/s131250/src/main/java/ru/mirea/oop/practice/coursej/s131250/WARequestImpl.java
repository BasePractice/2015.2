package ru.mirea.oop.practice.coursej.s131250;

import com.squareup.okhttp.ResponseBody;
import retrofit.Call;
import retrofit.Response;

import java.io.IOException;

final class WARequestImpl {
    static ResponseBody doWARequest(WARequest service, String input, String appid) {
        Call<ResponseBody> resp = service.doWARequest(input, appid, "Step-by-step solution");
        try {
            Response<ResponseBody> r = resp.execute();
            if (r.isSuccess()) {
                return r.body();
            } else {
                System.out.println(resp.execute().errorBody());
                return resp.execute().errorBody();
            }
        } catch (IOException e) {
            System.out.println("WARequestImpl ERROR");
            e.printStackTrace();
            return null;
        }
    }
}
