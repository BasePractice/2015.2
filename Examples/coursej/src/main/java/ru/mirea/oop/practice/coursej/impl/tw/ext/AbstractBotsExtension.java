package ru.mirea.oop.practice.coursej.impl.tw.ext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mirea.oop.practice.coursej.api.Getter;
import ru.mirea.oop.practice.coursej.api.Token;
import ru.mirea.oop.practice.coursej.api.ext.BotsExtension;
import twitter4j.auth.AccessToken;
import twitter4j.auth.OAuthSupport;

import java.util.concurrent.Future;

public abstract class AbstractBotsExtension implements BotsExtension {
    private static final Logger logger = LoggerFactory.getLogger(AbstractBotsExtension.class.getName());
    private final String name;
    protected final Getter getter;
    protected Future<?> started;

    private boolean isRunnings;
    private boolean isLoaded;

    protected AbstractBotsExtension(String name) {
        this.name = name;
        this.getter = new Getter.DefaultGetter("tw");
    }

    @Override
    public final String name() {
        return name;
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
    public final Future<?> start() {
        if (!isLoaded) {
            throw new RuntimeException("Расширение предварительно должно быть загружено. Вызван метод load()");
        }
        if (!isRunning()) {
            isRunnings = true;
            try {
                owner();
                started = doStart();
            } catch (Exception ex) {
                isRunnings = false;
                logger.error("Не смогли запустить обработчик", ex);
            }
        }
        return started;
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
    public final void load() {
        if (!isLoaded())
            try {
                isLoaded = init();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
    }

    protected final void login(OAuthSupport... supports) {
        final Token token = getToken();
        final AccessToken accessToken = new AccessToken(token.accessToken, token.accessSecret);
        for (OAuthSupport support : supports) {
            support.setOAuthConsumer(token.consumerKey, token.consumerSecret);
            support.setOAuthAccessToken(accessToken);
        }
    }

    protected final Token getToken() {
        return getter.getToken().get();
    }

    @Override
    public boolean isService() {
        return false;
    }

    protected abstract Future<?> doStart() throws Exception;

    protected abstract void doStop() throws Exception;

    protected abstract boolean init() throws Exception;
}
