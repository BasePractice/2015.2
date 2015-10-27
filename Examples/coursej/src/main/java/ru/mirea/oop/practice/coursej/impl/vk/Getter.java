package ru.mirea.oop.practice.coursej.impl.vk;

import ru.mirea.oop.practice.coursej.api.Provider;

public interface Getter {

    Provider<Token> getToken();

    Provider<Credentials> getCredentials();
}
