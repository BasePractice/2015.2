package ru.mirea.oop.practice.coursej.s131231;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import okio.BufferedSink;
import okio.Okio;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mirea.oop.practice.coursej.api.vk.VideoApi;
import ru.mirea.oop.practice.coursej.api.vk.entities.Video;
import ru.mirea.oop.practice.coursej.impl.vk.ext.ClientBotsExtension;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * -5?
 */
public final class VkVideoSync extends ClientBotsExtension {
    public static final String VIDEO_DIR = System.getProperty("user.home") + File.separator + ".video";
    private static final Logger logger = LoggerFactory.getLogger(VkVideoSync.class);

    public VkVideoSync() throws Exception {
        super("vk.clients.VideoSync");
    }

    @Override
    protected void doClient() throws Exception {
        final VideoApi videos = api.getVideos();
        final File directory = new File(VIDEO_DIR);
        if (!directory.exists())
            logger.debug("Создаем директорию для хранения видео: {}", directory.mkdirs());
        final List<Video> list = new ArrayList<>(Arrays.asList(videos.list(owner.id, null, null, null, null, 0)));
        List<String> existFiles = getFileList();
        logger.debug(" У текщего пользователя {} видеозаписей," +
                " на данном устройстве хранится {} видеозаписей.", list.size(), existFiles.size());

        for (Video video : list) {
            String fileName = getFileName(video);
            if (!existFiles.contains(fileName)) {
                download(video);
            }
        }
        logger.debug("Посик \"лишних\" файлов...");
        List<String> deleted = deleteExcess(list, getFileList());
        logger.debug("удалено {} файлов. Синхронизация завершена!", deleted.size());

    }


    private String download(Video video) {


        String url = getUrl(video);
        if (url != null) {
            try {
                String fileName = getFileName(video);
                File file = new File(VIDEO_DIR, fileName);
                if (!file.exists()) {
                    logger.debug("Загрузка {} ", file.getName());
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(url).build();
                    Response response;
                    response = client.newCall(request).execute();
                    try (BufferedSink sink = Okio.buffer(Okio.sink(file))) {
                        sink.writeAll(response.body().source());
                    } catch (FileNotFoundException e) {
                        logger.error("ошибка записи файла {}", file.getName());
                    }
                    return file.getName();
                }
            } catch (IOException e) {
                logger.error("Ошибка при загрузке {} по URL {}", getFileName(video), url);
            }
        } else if (video.player != null) {
            logger.info("Видео {} находится на стороннем сервере." +
                            " Попытайтесь загрузить его самостоятельно, ссылка на видео {}",
                    unescapeHtml(video.title), video.player);
        } else {
            logger.error("Загрузка видео невозможно, ссылка не получена");
        }

        return null;
    }

    private String getUrl(Video video) {
        List<String> links = new ArrayList<>();
        try {

            org.jsoup.nodes.Document document = Jsoup.connect("https://m.vk.com/video" + video.idOwner + "_" + video.id).timeout(15000).get();
            Elements elements = document.select("source");
            for (Element element : elements) {
                String src = element.attr("src");
                String url = src.replaceAll("\\.mp4\\?extra.*", ".mp4");
                links.add(url);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String link : links) {
            if (link.contains(".1080.mp4")) {
                return link;
            }
        }
        for (String link : links) {
            if (link.contains(".720.mp4")) {
                return link;
            }
        }
        for (String link : links) {
            if (link.contains(".480.mp4")) {
                return link;
            }
        }
        for (String link : links) {
            if (link.contains(".360.mp4")) {
                return link;
            }
        }
        for (String link : links) {
            if (link.contains(".240.mp4")) {
                return link;
            }
        }

        return null;
    }

    private List<String> deleteExcess(List<Video> list, List<String> existFiles) {
        List<String> deleted = new ArrayList<>();
        for (String fileName : existFiles) {
            boolean isListed = false;
            for (Video video : list) {
                String listedFileName = getFileName(video);
                if (fileName.equals(listedFileName)) {
                    isListed = true;
                    break;
                }
            }
            if (!isListed) {
                File file = new File(VIDEO_DIR, fileName);
                file.delete();
                deleted.add(fileName);
            }
        }
        return deleted;
    }


    private List<String> getFileList() {
        List<String> files = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(VIDEO_DIR))) {
            for (Path file : stream) {
                if (!file.toFile().isDirectory()) {
                    files.add(file.getFileName().toString());
                }
            }
        } catch (IOException e) {
            logger.error("Ошибка  при получчении списка файлов. Существует ли директория {}", VIDEO_DIR);
        }
        return files;
    }

    private static String getFileName(Video video) {
        String fileName = video.title + ".mp4";
        fileName = fileName.replaceAll("\\?|<|>|:|\\\\|\\||\\*|/|\"", "");
        fileName = unescapeHtml(fileName);
        return fileName;
    }

    private static String unescapeHtml(String html) { // изврат, зато без добавления лишних библиотек
        return Jsoup.clean(html, Whitelist.basic()).replaceAll("&amp;", "&");
    }

    @Override
    public String description() {
        return "Сохранение музыки текущего пользователя";
    }
}
