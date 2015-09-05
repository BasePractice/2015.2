package ru.mirea.oop.practice.queue;

public interface IDqueue<E> extends Iterable<E> {
    int size();

    void pushBack(E element);

    void pushFront(E element);

    E popBack();

    E popFront();

    E peekBack();

    E peekFront();

    boolean isEmpty();

    void clear();
}
