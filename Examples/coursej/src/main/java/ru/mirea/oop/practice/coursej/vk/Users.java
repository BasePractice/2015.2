package ru.mirea.oop.practice.coursej.vk;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;
import ru.mirea.oop.practice.coursej.vk.entities.Contact;

public interface Users {
    String DEFAULT_USER_FIELDS = "sex,bdate,city,country,photo_50,photo_100,photo_200_orig,photo_200,photo_400_orig,photo_max,photo_max_orig,photo_id,online,online_mobile,domain,has_mobile,contacts,connections,site,education,universities,schools,can_post,can_see_all_posts,can_see_audio,can_write_private_message,status,last_seen,common_count,relation,relatives,counters,screen_name,maiden_name,timezone,occupation,activities,interests,music,movies,tv,books,games,about,quotes,personal,friend_status,military,career";


    /**
     * {@link "https://vk.com/dev/users.get"}
     *
     * @param users    перечисленные через запятую идентификаторы пользователей или их короткие имена
     *                 (screen_name). По умолчанию — идентификатор текущего пользователя.
     *                 список строк, разделенных через запятую, количество элементов должно составлять
     *                 не более 1000
     * @param fields   список дополнительных полей профилей, которые необходимо вернуть.
     *                 См. @link{https://vk.com/dev/fields}.
     *                 Доступные значения: sex, bdate, city, country, photo_50, photo_100, photo_200_orig,
     *                 photo_200, photo_400_orig, photo_max, photo_max_orig, photo_id, online, online_mobile,
     *                 domain, has_mobile, contacts, connections, site, education, universities, schools,
     *                 can_post, can_see_all_posts, can_see_audio, can_write_private_message, status, last_seen,
     *                 common_count, relation, relatives, counters, screen_name, maiden_name, timezone, occupation,
     *                 activities, interests, music, movies, tv, books, games, about, quotes, personal,
     *                 friend_status, military, career
     *                 список строк, разделенных через запятую
     * @param nameCase падеж для склонения имени и фамилии пользователя.
     *                 Возможные значения:
     *                 именительный – nom,
     *                 родительный – gen,
     *                 дательный – dat,
     *                 винительный – acc,
     *                 творительный – ins,
     *                 предложный – abl.
     *                 По умолчанию nom.
     */
    @GET("/method/users.get")
    Call<Result<Contact[]>> list(@Query("user_ids") String users,
                                 @Query("fields") String fields,
                                 @Query("name_case") String nameCase);
}
