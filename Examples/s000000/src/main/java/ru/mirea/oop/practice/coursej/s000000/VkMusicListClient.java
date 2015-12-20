package ru.mirea.oop.practice.coursej.s000000;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mirea.oop.practice.coursej.api.vk.AudioApi;
import ru.mirea.oop.practice.coursej.api.vk.entities.Audio;
import ru.mirea.oop.practice.coursej.impl.vk.ext.ClientBotsExtension;

public final class VkMusicListClient extends ClientBotsExtension {
    private static final Logger logger = LoggerFactory.getLogger(VkMusicListClient.class);

    public VkMusicListClient() throws Exception {
        super("vk.clients.MusicList");
    }

    @Override
    protected void doClient() throws Exception {
        final AudioApi audios = api.getAudios();
        final Audio[] list =
                audios.list(owner.id, null, null, null, null, null);
        for (Audio audio : list) {
            logger.debug("Аудиозапись: {}", audio.title);
        }
    }

    @Override
    public String description() {
        return "Список музыки текущаего пользователя";
    }
}
