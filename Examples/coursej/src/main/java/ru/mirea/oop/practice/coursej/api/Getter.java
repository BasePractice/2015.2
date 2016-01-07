package ru.mirea.oop.practice.coursej.api;

public interface Getter {

    Provider<Token> getToken();

    Provider<Credentials> getCredentials();

    final class DefaultGetter implements Getter {
        private final String prefix;

        public DefaultGetter(String prefix) {
            this.prefix = prefix;
        }

        @Override
        public Provider<Token> getToken() {
            return new BasicProvider<>(Token.class, prefix);
        }

        @Override
        public Provider<ru.mirea.oop.practice.coursej.api.Credentials> getCredentials() {
            return new BasicProvider<>(ru.mirea.oop.practice.coursej.api.Credentials.class, prefix);
        }
    }
}
