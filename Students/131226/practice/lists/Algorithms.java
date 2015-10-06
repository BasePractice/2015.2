package ru.mirea.oop.practice.lists;

import java.util.Iterator;

public final class Algorithms {
    public static <E extends Comparable<E>> void sort(ILinkedList<E> list) {

        E obj1 = null;
        while (true) {
            int k = 0;
            Iterator<E> iterator = list.iterator();
            while (iterator.hasNext()) {
                E obj = obj1;
                obj1 = iterator.next();
                int i = 0;
                if (obj != null) {
                    i = obj1.compareTo(obj);
                }
                if (i == -1) {
                    list.remove(obj);
                    list.add(obj);
                    k++;
                    obj1 = null;
                    break;
                }
            }
            if (k == 0) {
                break;
            }
        }
    }

    public static <E> ILinkedList<E> subList(ILinkedList<E> list, E offset) {

        ILinkedList<E> linkedList = new LinkedListImpl<E>();
        Iterator<E> iterator = list.iterator();
        int k = 0;
        while (iterator.hasNext()) {
            E obj = iterator.next();
            if (obj.equals(offset) || k != 0) {

                linkedList.add(obj);
                k++;
            }
        }

        return linkedList;
    }


    public static <E> ILinkedList<E> subList(ILinkedList<E> list, E offset, int size) {
        ILinkedList<E> linkedList = new LinkedListImpl<E>();
        Iterator<E> iterator = list.iterator();
        int k = 0;
        while (iterator.hasNext()) {
            E obj = iterator.next();
            if (obj.equals(offset) || (k != 0 && k < size)) {

                linkedList.add(obj);
                k++;
            }
        }

        return linkedList;
    }


    public static <E> ILinkedList<E> addList(ILinkedList<E> list, ILinkedList<E> other) {
        for (E elem : other)
            list.add(elem);
        return list;
    }

    public static <E> ILinkedList<E> removeList(ILinkedList<E> list, E offset, int size) {

        Iterator<E> iterator = list.iterator();
        int k = 0;
        while (iterator.hasNext()) {
            E obj = iterator.next();
            if (obj.equals(offset) || (k != 0 && k < size)) {
                list.remove(obj);
                k++;

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

}
