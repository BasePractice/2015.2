package ru.mirea.oop.practice.queue;

import java.util.Iterator;

public final class CircleQueueImpl<E> implements IQueue<E> {

    private int size = 0;

    private Node<E> first;

    private Node<E> last;


    private final int capacity;

    public CircleQueueImpl(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public int size() {
        return size;

    }

    public void push(E element) {
        if (size < capacity) {


            final Node<E> l = last;
            final Node<E> newNode = new Node<>(element, first);
            last = newNode;
            if (l == null) {
                first = newNode;
            } else {
                l.next = newNode;
            }
            size++;
        } else {
            throw new QueueFullException();
        }

    }

    @Override
    public E pop() {

        final E current = first.item;
        first = first.next;
        last.next = first;
        if (first == null)
            last = null;
        --size;
        return current;

    }

    @Override
    public E peek() {
        return first.item;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        size = 0;
        first = null;
        last = null;
    }

    @Override
    public Iterator<E> iterator() {
        return new IteratorImpl(first);
    }

    private static final class Node<E> {
        final E item;
        Node<E> next;

        private Node(E element, Node<E> next) {
            this.item = element;
            this.next = next;
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

    public class QueueFullException extends RuntimeException {

        public QueueFullException() {
            super();
        }

        public QueueFullException(String message) {
            super(message);
        }

        public QueueFullException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
