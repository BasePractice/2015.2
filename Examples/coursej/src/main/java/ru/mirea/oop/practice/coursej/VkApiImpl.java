package ru.mirea.oop.practice.coursej;

import com.squareup.okhttp.OkHttpClient;
import ru.mirea.oop.practice.coursej.vk.*;

import java.lang.reflect.Constructor;
import java.util.Properties;

public final class VkApiImpl implements VkApi {

    private final String url;

    private final OkHttpClient ok;
    private final Authenticator authenticator;

    private VkApiImpl(String url) throws Exception {
        this.url = url;
        this.ok = ClientFactory.createOkClient();
        this.authenticator = new Authenticator();
        this.authenticator.authenticate(ok);
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

    @Override
    public Photos getPhotos() {
        return ServiceCreator.createService(ok, Photos.class, url);
    }

    @Override
    public Friends getFriends() {
        return ServiceCreator.createService(ok, Friends.class, url);
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
