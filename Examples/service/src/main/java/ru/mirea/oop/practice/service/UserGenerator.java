package ru.mirea.oop.practice.service;

import com.google.common.hash.HashCode;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public final class UserGenerator {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        OkHttpClient client = new OkHttpClient();
        Gson gson = new GsonBuilder().create();
        Request getter = new Request.Builder().get().url("http://randus.ru/api.php").build();
        for (int i = 0; i < 10; ++i) {
            Call call = client.newCall(getter);
            Response execute = call.execute();
            if (execute.isSuccessful()) {
                User user = gson.fromJson(execute.body().charStream(), User.class);
                StringBuilder builder = new StringBuilder();
                builder.append("INSERT INTO 'Users' (uuid, last_name, first_name, middl_name, login, password, email, group_id, avatar_url)");
                builder.append(" VALUES(");
                builder.append("'").append(UUID.randomUUID().toString().toUpperCase()).append("',");
                builder.append("'").append(user.lastName).append("',");
                builder.append("'").append(user.firstName).append("',");
                builder.append("'").append(user.middlName).append("',");
                builder.append("'").append(user.login).append("',");
                md.reset();
                byte[] digest = md.digest(user.password.getBytes(Charset.forName("UTF-8")));
                builder.append("'").append(HashCode.fromBytes(digest).toString().toUpperCase()).append("',");
                builder.append("'").append("',");
                builder.append("1,");
                builder.append("'").append(user.userpic).append("')");
                System.out.println("-- Password: " + user.password);
                System.out.println(builder.toString());
            }
        }
    }

    @SuppressWarnings("unused")
    private static final class User {
        @SerializedName("lname")
        public String lastName;
        @SerializedName("fname")
        public String firstName;
        @SerializedName("patronymic")
        public String middlName;
        public String gender;
        @SerializedName("date")
        public String birthday;
        public String city;
        private String street;
        public String house;
        public String apartment;
        public String phone;
        public String login;
        public String password;
        public String userpic;
    }
}
