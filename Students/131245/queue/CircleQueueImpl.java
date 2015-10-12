package ru.mirea.oop.practice.queue;

import java.util.Iterator;

public final class CircleQueueImpl<E> implements IQueue<E> {

    private final int capacity;
    private int size = 0;

    private Node<E> first;

    private Node<E> last;

    public CircleQueueImpl(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void push(E element) {
        if(size == capacity) {
            System.out.println("capacity limit");
            return;
        }
        final Node<E> l = last;
        final Node<E> newNode = new Node<>(last, element, first);
        last = newNode;
        if(l != null) {
            this.first.prev = last;
        }
        if (l == null) {
            first = newNode;
        } else {
            l.next = newNode;
        }
        size++;

    }

    @Override
    public E pop() {
        final E current = first.item;
        first = first.next;
        first.prev = last;
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
        return size == 0 ? true : false;
    }

    @Override
    public void clear() {
        last = null;
        for (Node<E> it = first; it != null; ) {
            Node<E> next = it.next;
            it.next = null;
            it = next;
        }
        first = null;
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
        IQueue<A> iDqueue = new CircleQueueImpl<>(6);
        System.out.println(iDqueue.isEmpty());
        iDqueue.push(a);
        iDqueue.push(a1);
        iDqueue.push(a2);
        iDqueue.push(a3);
        iDqueue.push(a4);
        iDqueue.push(a5);
        System.out.println(iDqueue.pop().getN());
        System.out.println(iDqueue.pop().getN());
        iDqueue.clear();
        System.out.println(iDqueue.size());
        iDqueue.push(a1);
        System.out.println("");

    }
}

