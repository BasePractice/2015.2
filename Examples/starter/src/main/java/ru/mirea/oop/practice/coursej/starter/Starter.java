package ru.mirea.oop.practice.coursej.starter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mirea.oop.practice.coursej.ext.BotsExtension;
import ru.mirea.oop.practice.coursej.ext.MazeExtension;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public final class Starter {
    private static final Logger logger = LoggerFactory.getLogger(Starter.class);

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        Set<Future<?>> futures = new LinkedHashSet<>();
        Set<String> enabled = loadEnabled();
        startMazeExtensions(enabled);
        startBotsExtensions(futures, enabled);

        for (Future<?> future : futures) {
            future.get();
        }
        futures.clear();
        BotsExtension.executor.shutdown();
    }

    private static void startMazeExtensions(Set<String> enabled) throws IOException {
        ServiceLoader<MazeExtension> loader = ServiceLoader.load(MazeExtension.class);
        for (MazeExtension extension : loader) {
            if (!enabled.contains(extension.name())) {
                logger.debug("Сервис: \"" + extension.name() + "\" пропущен");
                continue;
            }
            logger.debug("Запусксам: " + extension.name());
            MazeExtension.Maze maze = extension.generateMaze(1000, 1000);
            BufferedImage image = extension.createImage(maze);
            File file = new File(extension.name() + ".png");
            ImageIO.write(image, "PNG", file);
            logger.debug("Сохраняем: " + file.getAbsolutePath());
        }
    }

    private static void startBotsExtensions(Set<Future<?>> futures, Set<String> enabled) {
        ServiceLoader<BotsExtension> loader = ServiceLoader.load(BotsExtension.class);
        for (BotsExtension extension : loader) {
            if (!enabled.contains(extension.name())) {
                logger.debug("Сервис: \"" + extension.name() + "\" пропущен");
                continue;
            }
            extension.load();
            if (!extension.isLoaded()) {
                logger.debug("Сервис: \"" + extension.name() + "\" не удалось загрузить");
                continue;
            }
            Future<?> future = extension.start();
            if (!extension.isRunning() && future.isDone()) {
                logger.debug("Сервис: \"" + extension.name() + "\" не удалось запустить");
                continue;
            }
            logger.debug("Сервис: \"" + extension.name() + "\" запустился");
            futures.add(future);
        }
    }

    private static Set<String> loadEnabled() {
        Set<String> result = new HashSet<>();
        Properties prop = new Properties();
        try {
            prop.load(Starter.class.getResourceAsStream("/services.properties"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        result.addAll(prop.entrySet().stream().filter(p -> Boolean.parseBoolean(p.getValue().toString()))
                .map(p -> p.getKey().toString()).collect(Collectors.toList()));

        return result;
    }
}
