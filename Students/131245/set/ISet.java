package ru.mirea.oop.practice.set;

/**
 * Created by aleksejpluhin on 22.09.15.
 */
public interface ISet<E> extends Iterable<E> {
    int size();

    boolean isEmpty();

    boolean contains(E element);

    void put(E element);

    void remove(E element);

    void clear();

}