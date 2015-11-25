package ru.mirea.oop.practice.lists;

public final class Algorithms {
    public static <E> void sort(ILinkedList<E> list) {
        throw new RuntimeException("Not implement yet");
    }

    public static <E> ILinkedList<E> subList(ILinkedList<E> list, E offset) {
        //throw new RuntimeException("Not implement yet");
        ILinkedList<E> result = new LinkedListImpl<>();
        if (list instanceof LinkedListImpl) {
            LinkedListImpl<E> impl = (LinkedListImpl<E>) list;
            LinkedListImpl.Node<E> node = impl.indexOf(offset);
            if (node == null)
                return result;
            for (LinkedListImpl.Node<E> next = node; next != null; next = next.next) {
                result.add(node.item);
            }
        }
        return list;
    }

    public static <E> ILinkedList<E> subList(ILinkedList<E> list, E offset, int size) {
        throw new RuntimeException("Not implement yet");
    }

    public static <E> ILinkedList<E> addList(ILinkedList<E> list, ILinkedList<E> other) {
        for (E elem : other)
            list.add(elem);
        return list;
    }

    public static <E> ILinkedList<E> removeList(ILinkedList<E> list, E offset, int size) {
        //throw new RuntimeException("Not implement yet");
        ILinkedList<E> result = new LinkedListImpl<>();
        if (list instanceof LinkedListImpl) {
            LinkedListImpl<E> impl = (LinkedListImpl<E>) list;
            LinkedListImpl.Node<E> node = impl.indexOf(offset);
            if (node == null)
                return result;
            for (LinkedListImpl.Node<E> next = node; next != null && size > 0; next = next.prev, --size) {
                result.add(node.item);
            }
        }
        return list;
    }

    public static <E> boolean compareList(ILinkedList<E> list, ILinkedList<E> other) {
        if (list.size() != other.size())
            return false;
        return false;
    }

    public interface LinkedListFold<R, E> {
        R execute(R accum, E element);
    }

    public static <E extends Comparable<E>, R> R foldl(ILinkedList<E> list, R accum, LinkedListFold<R, E> func) {
        for (E element : list) {
            accum = func.execute(accum, element);
        }
        return accum;
    }

    public static void main(String[] args) {

    }

}
