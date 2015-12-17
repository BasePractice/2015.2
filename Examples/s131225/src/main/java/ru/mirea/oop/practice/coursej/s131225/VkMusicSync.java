package ru.mirea.oop.practice.coursej.s131225;

import ru.mirea.oop.practice.coursej.api.vk.AudioApi;
import ru.mirea.oop.practice.coursej.api.vk.entities.Audio;
import ru.mirea.oop.practice.coursej.impl.vk.ext.ClientBotsExtension;

public final class VkMusicSync extends ClientBotsExtension {
    public VkMusicSync() throws Exception {
        super("vk.clients.MusicSync");
    }

    @Override
    protected void doClient() throws Exception {
        final AudioApi audios = api.getAudios();
        final Audio[] list =
                audios.list(null, null, null, null, null, null);
        for (Audio audio : list) {
            System.out.println(audio.title);
        }
    }

    @Override
    public String description() {
        return "Сохранение музыки текущего пользователя";
    }
}
