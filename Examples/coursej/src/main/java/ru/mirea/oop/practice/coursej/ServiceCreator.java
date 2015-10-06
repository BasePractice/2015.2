package ru.mirea.oop.practice.coursej;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;
import retrofit.Retrofit;

final class ServiceCreator {
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting().create();

    private ServiceCreator() {
    }

    public static <S> S createService(OkHttpClient ok, Class<S> serviceClass, String baseUrl) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(CustomConverterFactory.create(gson))
                .client(ok);
        Retrofit adapter = builder.build();
        return adapter.create(serviceClass);
    }
}
