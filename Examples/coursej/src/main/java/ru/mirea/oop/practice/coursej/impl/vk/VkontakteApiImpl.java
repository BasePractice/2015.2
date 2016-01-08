package ru.mirea.oop.practice.coursej.impl.vk;

import com.squareup.okhttp.OkHttpClient;
import ru.mirea.oop.practice.coursej.api.VkontakteApi;
import ru.mirea.oop.practice.coursej.api.vk.*;
import ru.mirea.oop.practice.coursej.impl.ClientFactory;
import ru.mirea.oop.practice.coursej.impl.ServiceCreator;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public final class VkontakteApiImpl implements VkontakteApi {
    private static final Map<Class<?>, Object> impl = new ConcurrentHashMap<>();
    private final String url;

    private final OkHttpClient client;
    private final VkAuthenticator vkAuthenticator;

    private VkontakteApiImpl(String url) throws Exception {
        this.url = url;
        this.client = ClientFactory.createOkClient();
        this.vkAuthenticator = new VkAuthenticator();
    }

    private VkontakteApiImpl() throws Exception {
        this("https://api.vk.com/");
    }

    @Override
    public UsersApi getUsers() throws Exception {
        return getImpl(UsersApiImpl.class, Users.class);
    }

    @Override
    public MessagesApi getMessages() throws Exception {
        return getImpl(MessagesApiImpl.class, Messages.class);
    }

    @Override
    public PhotosApi getPhotos() throws Exception {
        return getImpl(PhotosApiImpl.class, Photos.class);
    }

    @Override
    public AccountApi getAccounts() throws Exception {
        return getImpl(AccountApiImpl.class, Account.class);
    }

    @Override
    public FriendsApi getFriends() throws Exception {
        return getImpl(FriendsApiImpl.class, Friends.class);
    }

    @Override
    public DocumentsApi getDocuments() throws Exception {
        return getImpl(DocumentsApiImpl.class, Documents.class);
    }

    @Override
    public AudioApi getAudios() throws Exception {
        return getImpl(AudioApiImpl.class, Audios.class);
    }

    @Override
    public VideoApi getVideos() throws Exception {
        return getImpl(VideoApiImpl.class, Videos.class);
    }

    @Override
    public long idOwner() {
        return vkAuthenticator.idOwner();
    }

    //FIXME: Сделать конкурентным
    @SuppressWarnings("unchecked")
    private <E extends ExternalCall, T> E getImpl(Class<E> klass, Class<T> inter) throws Exception {
        if (!impl.containsKey(klass)) {
            Constructor<E> constructor = klass.getDeclaredConstructor(inter);
            T service = service(inter);
            E created = constructor.newInstance(service);
            impl.put(klass, created);
        }
        return (E) impl.get(klass);
    }

    private <E> E service(Class<E> klass) {
        return ServiceCreator.createService(client, klass, url);
    }

    @Override
    public void start() throws Exception {
        this.vkAuthenticator.authenticate(client);
    }

    @Override
    public OkHttpClient createClient() {
        return client;
    }

    @SuppressWarnings("unchecked")
    public static synchronized VkontakteApi load() throws Exception {
        Properties prop = new Properties();
        ClassLoader loader = VkontakteApi.class.getClassLoader();
        try {
            prop.load(loader.getResourceAsStream("impl.properties"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            Class<VkontakteApi> klass = (Class<VkontakteApi>)
                    loader.loadClass(prop.getProperty("vk.api.class", "ru.mirea.oop.practice.coursej.VkApiImpl"));
            Constructor<VkontakteApi> constructor = klass.getConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (Exception ex) {
            return new VkontakteApiImpl();
        }
    }
}
