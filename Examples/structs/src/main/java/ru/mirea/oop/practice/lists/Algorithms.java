package ru.mirea.oop.practice.lists;

import java.util.Iterator;

public final class Algorithms {
    public static <E> void sort(ILinkedList<E> list) {
        throw new RuntimeException(" ");
    }

    public static <E> ILinkedList<E> subList(ILinkedList<E> list, E offset) {
        Iterator<E> iterator = list.iterator();
        LinkedListImpl sublist = null;
        boolean isFound = false;
        E item = null;
            while (!isFound && iterator.hasNext()){
               item = iterator.next();
                   if(item.equals(offset)) {
                       isFound = true;
                       while (iterator.hasNext()) {
                           sublist = new LinkedListImpl<E>();
                           sublist.add(iterator.next());
                       }
                   }
        }
        return sublist;
    }


    public static void<E> Print(ILinkedList<E> list) {
        Iterator<E> iterator = list.iterator();
        LinkedListImpl sublist = null;
        boolean isFound = false;
        E item = null;
        while (iterator.hasNext()){
            item = iterator.next();
                while (iterator.hasNext()) {
                    sublist = new LinkedListImpl<E>();
                    sublist.add(iterator.next());
                }
            }
        }
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
        throw new RuntimeException("Not implement yet");
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
