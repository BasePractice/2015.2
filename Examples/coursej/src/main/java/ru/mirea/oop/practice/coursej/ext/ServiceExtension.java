package ru.mirea.oop.practice.coursej.ext;

import ru.mirea.oop.practice.coursej.vk.VkApi;

public abstract class ServiceExtension extends AbstractExtension {

    private static final int DEFAULT_TIMEOUT = 1000;
    private static final Event timeoutEvent = new Event(EventType.TIMEOUT);

    protected ServiceExtension(VkApi api) {
        super(api);
    }

    @Override
    public final boolean isService() {
        return true;
    }

    @Override
    protected final void doStart() throws Exception {
        /** Event loop, Server long poll */
    }

    @Override
    protected final void doStop() throws Exception {
        /** Stop server */
    }

    protected abstract void doEvent(Event event);

    private void raisTimeout() {
        doEvent(timeoutEvent);
    }

    protected int timeout() {
        return DEFAULT_TIMEOUT;
    }

    protected enum EventType {
        TIMEOUT
    }

    protected static final class Event {
        protected final EventType type;

        private Event(EventType type) {
            this.type = type;
        }
    }
}
