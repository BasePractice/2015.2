package ru.mirea.oop.practice.coursej.ext;

import ru.mirea.oop.practice.coursej.vk.VkApi;

abstract class AbstractExtension implements Extension {
    protected final VkApi api;

    private boolean isRunnings;
    private boolean isLoaded;

    protected AbstractExtension(VkApi api) {
        this.api = api;
        this.isRunnings = false;
        this.isLoaded = false;
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
    public final void start() {
        if (!isRunning()) {
            isRunnings = true;
            try {
                doStart();
            } catch (Exception e) {
                isRunnings = false;
                e.printStackTrace();
            }
        }
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
            isLoaded = init();
    }

    @Override
    public boolean isService() {
        return false;
    }

    protected abstract void doStart() throws Exception;

    protected abstract void doStop() throws Exception;

    protected abstract boolean init();

}
