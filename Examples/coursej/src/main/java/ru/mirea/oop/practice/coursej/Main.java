package ru.mirea.oop.practice.coursej;

import com.squareup.okhttp.OkHttpClient;
import retrofit.Call;
import ru.mirea.oop.practice.coursej.vk.Result;
import ru.mirea.oop.practice.coursej.vk.Users;
import ru.mirea.oop.practice.coursej.vk.entities.Contact;

public final class Main {
    private static final String DEFAULT_USER_FIELDS = "sex,bdate,city,country,photo_50,photo_100,photo_200_orig,photo_200,photo_400_orig,photo_max,photo_max_orig,photo_id,online,online_mobile,domain,has_mobile,contacts,connections,site,education,universities,schools,can_post,can_see_all_posts,can_see_audio,can_write_private_message,status,last_seen,common_count,relation,relatives,counters,screen_name,maiden_name,timezone,occupation,activities,interests,music,movies,tv,books,games,about,quotes,personal,friend_status,military,career";

    //5067217
    public static void main(String[] args) throws Exception {
        Authenticator authenticator = new Authenticator(5067217);
        OkHttpClient ok = ClientFactory.createOkClient();
        authenticator.authenticate(ok);
        Users users = ServiceCreator.createService(ok, Users.class, "https://api.vk.com/");
        Call<Result<Contact[]>> u = users.list(
                null,
                DEFAULT_USER_FIELDS,
                null);
        Contact[] contacts = Result.call(u);
        System.out.println(contacts[0]);
    }
}
