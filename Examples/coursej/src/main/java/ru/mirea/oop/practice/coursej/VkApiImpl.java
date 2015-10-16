package ru.mirea.oop.practice.coursej;

import com.squareup.okhttp.OkHttpClient;
import ru.mirea.oop.practice.coursej.vk.*;

import java.lang.reflect.Constructor;
import java.util.Properties;

public final class VkApiImpl implements VkApi {

    private final String url;

    private final OkHttpClient client;
    private final Authenticator authenticator;

    private VkApiImpl(String url) throws Exception {
        this.url = url;
        this.client = ClientFactory.createOkClient();
        this.authenticator = new Authenticator();
    }

    private VkApiImpl() throws Exception {
        this("https://api.vk.com/");
    }

    @Override
    public long idOwner() {
        return authenticator.idOwner();
    }

    @Override
    public OkHttpClient getClient() {
        return client;
    }

    @Override
    public Users getUsers() {
        return ServiceCreator.createService(client, Users.class, url);
    }

    @Override
    public Messages getMessages() {
        return ServiceCreator.createService(client, Messages.class, url);
    }

    @Override
    public Photos getPhotos() {
        return ServiceCreator.createService(client, Photos.class, url);
    }

    @Override
    public Account getAccounts() {
        return ServiceCreator.createService(client, Account.class, url);
    }

    @Override
    public Friends getFriends() {
        return ServiceCreator.createService(client, Friends.class, url);
    }

    @Override
    public void start() throws Exception {
        this.authenticator.authenticate(client);
    }

    public static synchronized VkApi load() throws Exception {
        Properties prop = new Properties();
        ClassLoader loader = VkApiImpl.class.getClassLoader();
        try {
            prop.load(loader.getResourceAsStream("impl.properties"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            Class<?> klass = loader.loadClass(prop.getProperty("vk.api.class", "ru.mirea.oop.practice.coursej.VkApiImpl"));
            Constructor<?> constructor = klass.getConstructor();
            constructor.setAccessible(true);
            return (VkApi) constructor.newInstance();
        } catch (Exception ex) {
            return new VkApiImpl();
        }
    }
}
