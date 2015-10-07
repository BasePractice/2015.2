package ru.mirea.oop.practice.coursej;

import com.squareup.okhttp.OkHttpClient;
import ru.mirea.oop.practice.coursej.vk.Messages;
import ru.mirea.oop.practice.coursej.vk.Users;
import ru.mirea.oop.practice.coursej.vk.VkApi;

final class VkApiImpl implements VkApi {

    private final String url;

    private final OkHttpClient ok;
    private final Authenticator authenticator;

    VkApiImpl(String url) {
        this.url = url;
        this.ok = ClientFactory.createOkClient();
        this.authenticator = new Authenticator();
        try {
            this.authenticator.authenticate(ok);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    VkApiImpl() {
        this("https://api.vk.com/");
    }

    @Override
    public OkHttpClient getClient() {
        return ok;
    }

    @Override
    public Users getUsers() {
        return ServiceCreator.createService(ok, Users.class, url);
    }

    @Override
    public Messages getMessages() {
        return ServiceCreator.createService(ok, Messages.class, url);
    }
}
