package ru.mirea.oop.practice.coursej.impl;

import ru.mirea.oop.practice.coursej.api.Provider;

import java.lang.reflect.Constructor;

public final class ProviderImpl<T> implements Provider<T> {
    private final Class<T> klass;
    private volatile T created;

    public ProviderImpl(Class<T> klass) {
        this.klass = klass;
    }

    @Override
    public T get() {
        if (created == null) {
            synchronized (ProviderImpl.class) {
                if (created == null) {
                    try {
                        Constructor<T> constructor = klass.getDeclaredConstructor();
                        constructor.setAccessible(true);
                        created = constructor.newInstance();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
        return created;
    }

    @Override
    public void put(T value) {
        synchronized (ProviderImpl.class) {
            created = value;
        }
    }
}
