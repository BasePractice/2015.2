package ru.mirea.oop.practice.coursej.starter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mirea.oop.practice.coursej.ext.Extension;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public final class Starter {
    private static final Logger logger = LoggerFactory.getLogger(Starter.class);

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        Set<Future<?>> futures = new LinkedHashSet<>();
        ServiceLoader<Extension> loader = ServiceLoader.load(Extension.class);
        for (Extension extension : loader) {
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
            futures.add(future);
        }

        for (Future<?> future : futures) {
            future.get();
        }
        futures.clear();
        Extension.executor.shutdown();
    }
}
