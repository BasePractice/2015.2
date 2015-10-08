package ru.mirea.oop.practice.coursej;

import retrofit.Call;
import ru.mirea.oop.practice.coursej.ext.Extension;
import ru.mirea.oop.practice.coursej.ext.ServiceExtension;
import ru.mirea.oop.practice.coursej.vk.Result;
import ru.mirea.oop.practice.coursej.vk.Users;
import ru.mirea.oop.practice.coursej.vk.VkApi;
import ru.mirea.oop.practice.coursej.vk.entities.Contact;

public final class Main {
    private static final String DEFAULT_USER_FIELDS = "sex,bdate,city,country,photo_50,photo_100,photo_200_orig,photo_200,photo_400_orig,photo_max,photo_max_orig,photo_id,online,online_mobile,domain,has_mobile,contacts,connections,site,education,universities,schools,can_post,can_see_all_posts,can_see_audio,can_write_private_message,status,last_seen,common_count,relation,relatives,counters,screen_name,maiden_name,timezone,occupation,activities,interests,music,movies,tv,books,games,about,quotes,personal,friend_status,military,career";

    //5067217
    public static void main(String[] args) throws Exception {
        VkApi api = new VkApiImpl();
        printOwner(api);
        Extension ext = new ServiceExtensionImpl(api);
        ext.load();
        ext.start();
        Thread.sleep(600000);
        ext.stop();
    }

    private static void printOwner(VkApi api) throws java.io.IOException {
        Users users = api.getUsers();
        Call<Result<Contact[]>> u = users.list(
                null,
                DEFAULT_USER_FIELDS,
                null);
        Contact[] contacts = Result.call(u);
        System.out.println(contacts[0]);
    }

    private static final class ServiceExtensionImpl extends ServiceExtension {

        private ServiceExtensionImpl(VkApi api) {
            super(api);
        }

        @Override
        protected void doEvent(Event event) {
            System.out.println(event);
        }

        @Override
        protected boolean init() {
            return true;
        }
    }
}
