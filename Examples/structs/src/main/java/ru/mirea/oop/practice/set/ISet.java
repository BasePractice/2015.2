package ru.mirea.oop.practice.set;

public interface ISet<E> extends Iterable<E> {
    int size();

    boolean isEmpty();

    boolean contains(E element);

    void put(E element);

    void remove(E element);

    void clear();
}
