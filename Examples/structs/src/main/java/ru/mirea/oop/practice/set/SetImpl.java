package ru.mirea.oop.practice.set;

import java.util.Iterator;

@SuppressWarnings("unchecked")
public final class SetImpl<E> implements ISet<E> {
    private int size;
    private final Node<E>[] table;

    public SetImpl(int capacity) {
        table = (Node<E>[]) new Node[capacity];
    }

    public SetImpl() {
        this(256);
    }

    private int indexOf(E element) {
        if (element == null)
            return 0;
        return (table.length - 1) & hash(element);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(E element) {
        int i = indexOf(element);
        int hash = hash(element);
        Node<E> it;
        if ((it = table[i]) == null)
            return false;
        if (it.hash == hash && element != null && element.equals(it.item))
            return true;
        while (it != null) {
            if (it.next == null) {
                break;
            }
            if (it.hash == hash && element != null && element.equals(it.item))
                return true;
            it = it.next;
        }
        return false;
    }

    @Override
    public void put(E element) {
        int i = indexOf(element);
        int hash = hash(element);
        Node<E> it;
        if ((it = table[i]) == null) {
            table[i] = new Node<>(null, element, null);
            size++;
        } else {
            Node<E> exists = null;
            if (it.hash == hash && element != null && element.equals(it.item)) {
                exists = it;
            } else {
                while (it != null) {
                    if ((exists = it.next) == null) {
                        it.next = new Node<>(it, element, null);
                        break;
                    }
                    if (exists.hash == hash && element != null && element.equals(exists.item))
                        break;
                    it = it.next;
                }
            }
            if (exists == null) {
                size++;
            }
        }
    }

    private static <E> int hash(E element) {
        return element.hashCode();
    }

    @Override
    public void remove(E element) {
        int i = indexOf(element);
        int hash = hash(element);
        Node<E> it = table[i];
        if (it != null) {
            if (it.hash == hash && element != null && element.equals(it.item)) {
                table[i] = it.next;
                --size;
            } else {
                Node<E> next = it;
                while (next != null) {
                    if (it.hash == hash && element != null && element.equals(it.item)) {
                        Node<E> itNext = it.next;
                        Node<E> itPrev = it.prev;
                        itPrev.next = itNext;
                        if (itNext != null)
                            itNext.prev = itPrev;
                        --size;
                        break;
                    }
                    next = next.next;
                }
            }
        }
    }

    @Override
    public void clear() {
        for (int i = 0; i < table.length; ++i)
            table[i] = null;
        size = 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new IteratorImpl2(table);
    }

    private static final class Node<E> {
        final E item;
        final int hash;
        Node<E> next;
        Node<E> prev;

        private Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
            this.hash = hash(element);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ISet) {
            throw new RuntimeException("Not implement yet");
        }
        return super.equals(obj);
    }

    private final class IteratorImpl2 implements Iterator<E> {
        private final Node<E> [] table;
        private int nextIndex;
        private Iterator<E> next;

        public IteratorImpl2(Node<E>[] table) {
            this.table = table;
            this.nextIndex = 0;
            next = new IteratorImpl(table[0]);
        }

        @Override
        public boolean hasNext() {
            if (nextIndex >= table.length)
                return false;
            if (next.hasNext())
                return true;
            while (nextIndex < table.length) {
                next = new IteratorImpl(table[nextIndex]);
                if (next.hasNext())
                    return true;
                nextIndex++;
            }
            return nextIndex < table.length;
        }

        @Override
        public E next() {
            return next.next();
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
