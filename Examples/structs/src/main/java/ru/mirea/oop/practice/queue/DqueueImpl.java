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
        final Node<E> l = first;
        final Node<E> newNode = new Node<>(null, element, l);
        first = newNode;
        if (l == null) {
            last = newNode;
        } else {
            l.prev = newNode;
        }
        size++;
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

    public static class A {
        private int n;

        public A(int n) {
            this.n = n;
        }

        public int getN() {
            return n;
        }
    }

    public static void main(String[] args) {
        A a = new A(1);
        A a1 = new A(2);
        A a2 = new A(3);
        A a3 = new A(4);
        A a4 = new A(5);
        A a5 = new A(6);
        IDqueue<A> iDqueue = new DqueueImpl<>();
        iDqueue.pushFront(a);
        iDqueue.pushFront(a1);
        iDqueue.pushFront(a2);
        iDqueue.pushFront(a3);
        iDqueue.pushFront(a4);
        iDqueue.pushFront(a5);
        System.out.println(iDqueue.popBack().getN());
        System.out.println(iDqueue.peekBack().getN());
        System.out.println(iDqueue.popFront().getN());
        System.out.println(iDqueue.peekFront().getN());
        iDqueue.pushBack(a3);
        System.out.println(iDqueue.peekBack().getN());
    }
}
