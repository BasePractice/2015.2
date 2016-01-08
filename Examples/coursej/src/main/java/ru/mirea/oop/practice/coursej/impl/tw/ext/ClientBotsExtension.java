package ru.mirea.oop.practice.coursej.impl.tw.ext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mirea.oop.practice.coursej.api.Token;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.api.UsersResources;

public abstract class ClientBotsExtension extends AbstractBotsExtension {
    private static final Logger logger = LoggerFactory.getLogger(ClientBotsExtension.class.getName());
    protected final Twitter twitter;
    protected volatile User owner;

    protected ClientBotsExtension(String name) {
        super(name);
        this.twitter = TwitterFactory.getSingleton();
    }

    @Override
    public synchronized long owner() throws Exception {
        if (owner == null) {
            Token token = getToken();
            UsersResources users = twitter.users();
            owner = users.showUser(token.idUser);
        }
        return owner.getId();
    }

    protected boolean init() throws Exception {
        login(twitter);
        return true;
    }
}
