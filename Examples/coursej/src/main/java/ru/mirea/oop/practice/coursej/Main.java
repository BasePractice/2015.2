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

    //5095620 -  курсач

    public static void main(String[] args) throws Exception {
//      Authenticator.Token token = Authenticator.Token.parse(HttpUrl.parse("https://oauth.vk.com/blank.html?access_token=747d15a15a97cc4f1fcf02fbeb615ca930e8bde27093210c65940d99ea1d5b79d8a2420abcfe4b0d9de33&expires_in=86400&user_id=222154225"));
  //      Authenticator.Token.save(token, "/Users/aleksejpluhin/IdeaProjects/2015.2/Examples/coursej/src/main/resources/.accessToken");

        VkApi api = new VkApiImpl(5095620);
        //printOwner(api);
        Extension ext = new ServiceExtensionImpl(api);
        ext.load();
        ext.start();
        Thread.sleep(500000);
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
