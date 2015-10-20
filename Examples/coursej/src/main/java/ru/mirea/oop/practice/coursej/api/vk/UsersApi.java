package ru.mirea.oop.practice.coursej.api.vk;

import ru.mirea.oop.practice.coursej.api.vk.entities.Contact;

import java.io.IOException;

public interface UsersApi {
    Contact[] list(String users,
                   String fields,
                   String nameCase) throws IOException;
}
