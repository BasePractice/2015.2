package ru.mirea.oop.practice.lists;

import java.util.Iterator;

final class LinkedListImpl<E> implements ILinkedList<E> {

    private int size = 0;

    private Node<E> first;

    private Node<E> last;

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0 || first == null;
    }

    @Override
    public boolean contains(E element) {
        for (Node<E> it = first; it != null; it = it.next) {
            if (element.equals(it.item)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void add(E element) {
        final Node<E> l = last;
        final Node<E> newNode = new Node<E>(l, element, null);
        last = newNode;
        if (l == null) {
            first = newNode;
        } else {
            l.next = newNode;
        }
        size++;
    }


    @Override
    public void remove(E element) {
        for (Node<E> it = first; it != null; it = it.next) {
            if (element.equals(it.item)) {
                remove(it);
            }
        }
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

    private E remove(Node<E> x) {
        final E element = x.item;
        final Node<E> next = x.next;
        final Node<E> prev = x.prev;

        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
            x.prev = null;
        }

        if (next == null) {
            last = prev;
        } else {
            next.prev = prev;
            x.next = null;
        }
        size--;
        return element;
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
