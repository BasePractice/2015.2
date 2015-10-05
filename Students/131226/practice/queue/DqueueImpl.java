package ru.mirea.oop.practice.queue;

import java.util.Iterator;

public final class DqueueImpl<E> implements IDqueue<E> {

    private int size = 0;

    private Node<E> first;

    private Node<E> last;

    @Override
    public int size() {
        return size;
    }

    @Override
    public void pushBack(E element) {
        final Node<E> l = last;
        final Node<E> newNode = new Node<>(l, element, null);
        last = newNode;
        if (l == null) {
            first = newNode;
        } else {
            l.next = newNode;
        }
        size++;
    }

    @Override
    public void pushFront(E element) {
        final Node<E> f = first;
        final Node<E> newFirst = new Node<>(f, element, null);
        first = newFirst;
        if (f == null) {
            last = newFirst;
        } else {
            f.prev = newFirst;
        }
    }

    @Override
    public E popBack() {

        final E current = last.item;
        last = last.prev;
        if (last == null) {
            first = null;
        }
        --size;
        return current;
    }

    @Override
    public E popFront() {
        final E current = first.item;
        first = first.next;
        if (first == null)
            last = null;
        --size;
        return current;
    }

    @Override
    public E peekBack() {
        return last.item;

    }


    @Override
    public E peekFront() {
        return first.item;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        for (Node<E> it = first; it != null; ) {
            Node<E> next = it.next;
            it.next = null;
            it.prev = null;
            it = next;
        }
        first = last = null;
        size = 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new IteratorImpl(first);
    }

    private static final class Node<E> {
        final E item;
        Node<E> next;
        Node<E> prev;

        private Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }

    private final class IteratorImpl implements Iterator<E> {

        private int nextIndex = 0;
        private Node<E> next;
        private Node<E> returned;

        private IteratorImpl(Node<E> next) {
            this.next = next;
        }

        @Override
        public boolean hasNext() {
            return nextIndex < size && next != null;
        }

        @Override
        public E next() {
            returned = next;
            next = next.next;
            nextIndex++;
            return returned.item;
        }

    }

}
