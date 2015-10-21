package ru.mirea.oop.practice.coursej.api.vk;


import ru.mirea.oop.practice.coursej.api.vk.entities.Contact;

import java.io.IOException;

public interface FriendsApi extends ExternalCall {
    Contact[] list(Integer idUser,
                   Integer idList,
                   Integer count,
                   Integer offset,
                   String fields) throws IOException;
}
