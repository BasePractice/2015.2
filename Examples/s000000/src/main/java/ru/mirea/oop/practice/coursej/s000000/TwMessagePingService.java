package ru.mirea.oop.practice.coursej.s000000;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mirea.oop.practice.coursej.impl.tw.ext.ServiceBotsExtension;
import twitter4j.DirectMessage;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.User;

public final class TwMessagePingService extends ServiceBotsExtension {
    private static final Logger logger = LoggerFactory.getLogger(TwMessagePingService.class.getName());

    public TwMessagePingService() {
        super("tw.services.EchoServer");
    }

    @Override
    public String description() {
        return "Сервис пересылки сообщений \"Twitter\"";
    }

    @Override
    protected void statusEvent(Status status) throws TwitterException {
        logger.debug("@{} : {}", status.getUser().getScreenName(), status.getText());
    }

    @Override
    protected void directMessage(DirectMessage message) throws TwitterException {
        User sender = message.getSender();
        logger.debug("@{}: {}", sender.getScreenName(), message.getText());
        sendDirectMessage(sender.getId(), message.getText());
    }
}
