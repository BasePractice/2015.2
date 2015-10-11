package ru.mirea.oop.practice.coursej.ext;

import retrofit.Call;
import ru.mirea.oop.practice.coursej.VkApiImpl;
import ru.mirea.oop.practice.coursej.vk.Result;
import ru.mirea.oop.practice.coursej.vk.Users;
import ru.mirea.oop.practice.coursej.vk.VkApi;
import ru.mirea.oop.practice.coursej.vk.entities.Contact;

import java.io.IOException;

abstract class AbstractExtension implements Extension {
    protected final VkApi api;
    protected Contact owner;

    private boolean isRunnings;
    private boolean isLoaded;

    protected AbstractExtension(VkApi api) {
        this.api = api;
        this.isRunnings = false;
        this.isLoaded = false;
        this.owner = null;
    }

    protected AbstractExtension() throws Exception {
        this(VkApiImpl.load());
    }

    @Override
    public final boolean isRunning() {
        return isRunnings;
    }

    @Override
    public final boolean isLoaded() {
        return isLoaded;
    }

    @Override
    public final void start() {
        if (!isLoaded) {
            throw new RuntimeException("Расширение предварительно должно быть загружено. Вызван метод load()");
        }
        if (!isRunning()) {
            isRunnings = true;
            try {
                Contact owner = owner();
                System.out.println("Запустились под пользователем " + owner.firstName + " " + owner.lastName);
                doStart();
            } catch (Exception e) {
                isRunnings = false;
                e.printStackTrace();
            }
        }
    }

    @Override
    public final void stop() {
        isRunnings = false;
        try {
            doStop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized Contact owner() throws IOException {
        if (owner == null) {
            Call<Result<Contact[]>> callable = api.getUsers().list("" + api.idOwner(), Users.DEFAULT_USER_FIELDS, null);
            Contact[] contacts = Result.call(callable);
            if (contacts == null || contacts.length == 0) {
                throw new IOException("Не могу получить собственного пользователя");
            }
            owner = contacts[0];
        }
        return owner;
    }

    @Override
    public final void load() {
        if (!isLoaded())
            isLoaded = init();
    }

    @Override
    public boolean isService() {
        return false;
    }

    protected abstract void doStart() throws Exception;

    protected abstract void doStop() throws Exception;

    protected abstract boolean init();
}
