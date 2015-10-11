package ru.mirea.oop.practice.queue;

import java.util.Iterator;

public final class CircleQueueImpl<E> implements IQueue<E> {

    private final int capacity;

    public CircleQueueImpl(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public int size() {
        throw new RuntimeException("Not implement yet");
    }

    @Override
    public void push(E element) {
        throw new RuntimeException("Not implement yet");
    }

    @Override
    public E pop() {
        throw new RuntimeException("Not implement yet");
    }

    @Override
    public E peek() {
        throw new RuntimeException("Not implement yet");
    }

    @Override
    public boolean isEmpty() {
        throw new RuntimeException("Not implement yet");
    }

    @Override
    public void clear() {
        throw new RuntimeException("Not implement yet");
    }

    @Override
    public Iterator<E> iterator() {
        throw new RuntimeException("Not implement yet");
    }
}
