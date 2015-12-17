package ru.mirea.oop.practice.coursej.s131225;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import okio.BufferedSink;
import okio.Okio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mirea.oop.practice.coursej.api.vk.AudioApi;
import ru.mirea.oop.practice.coursej.api.vk.entities.Audio;
import ru.mirea.oop.practice.coursej.impl.vk.ext.ClientBotsExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class VkMusicSync extends ClientBotsExtension {
    public static final String MUSIC_DIR = System.getProperty("user.home") + System.getProperty("file.separator") + ".Music";
    private static final Logger logger = LoggerFactory.getLogger(VkMusicSync.class);

    public VkMusicSync() throws Exception {
        super("vk.clients.MusicSync");
    }

    @Override
    protected void doClient() throws Exception {
        final AudioApi audios = api.getAudios();
        final File directory = new File(MUSIC_DIR);
        if (!directory.exists())
            logger.debug("Создаем директорию для хранения музыки: {}", directory.mkdirs());
        final List<Audio> list = new ArrayList<>(Arrays.asList(audios.list(null, null, null, null, null, null)));
        List<String> existFiles = getFileList();
        logger.debug(" У текщего пользователя {} аудиозаписей," +
                " на данном устройстве хранится {} аудиозаписей.",list.size(),existFiles.size());

        for (Audio audio : list) {  // грузим то, чего у нас нет
            String fileName = getFileName(audio);
            if (!existFiles.contains(fileName)) {
                download(audio);
            }
        }
        logger.debug("Посик \"лишних\" файлов...");
        List<String> deleted = deleteExcess(list, getFileList()); // удаляем лишнее
        logger.debug("удалено {} файлов. Синхронизация завершена!",deleted.size() );

    }

    private String download(Audio audio) {
        try {
            String fileName = getFileName(audio);
            File file = new File(MUSIC_DIR, fileName);
            if (!file.exists()) {
                logger.debug("Загрузка " + file.getName());
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(audio.url).build();
                Response response;
                response = client.newCall(request).execute();
                BufferedSink sink = Okio.buffer(Okio.sink(file));
                sink.writeAll(response.body().source());
                sink.close();
                return file.getName();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<String> deleteExcess(List<Audio> list, List<String> existFiles) {
        List<String> deleted = new ArrayList<>();
        for (String fileName : existFiles) {
            boolean isListed = false;
            for (Audio audio : list) {
                String fileName1 = getFileName(audio);
                if (fileName.equals(fileName1)) {
                    isListed = true;
                    break;
                }
            }
            if (!isListed) {
                File file = new File(MUSIC_DIR, fileName);
                file.delete();
                deleted.add(fileName);
            }
        }
        return deleted;
    }


    private List<String> getFileList() {
        List<String> files = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(MUSIC_DIR))) {
            for (Path file : stream) {
                if (!file.toFile().isDirectory()) {
                    files.add(file.getFileName().toString());
                }
            }
        } catch (IOException | DirectoryIteratorException e) {
            e.printStackTrace();
        }
        return files;
    }

    private static String getFileName(Audio audio) {
        String fileName = audio.artist + " - " + audio.title + ".mp3";
        fileName = fileName.replaceAll("\\?|<|>|:|\\\\|\\||\\*|/|\"", "");
        fileName = fileName.replaceAll("&amp;", "&");
        return fileName;
    }

    @Override
    public String description() {
        return "Сохранение музыки текущего пользователя";
    }
}
