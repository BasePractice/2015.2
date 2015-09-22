package ru.mirea.oop.practice.lists;

import java.util.Iterator;

public final class Algorithms {
    public static <E> void sort(ILinkedList<E> list) {
        LinkedListImpl sublist = new LinkedListImpl<E>();
        





    }

    public static <E> ILinkedList<E> subList(ILinkedList<E> list, E offset) {
        Iterator<E> iterator = list.iterator();
        LinkedListImpl sublist = null;
        boolean isFound = false;
        E item = null;
        sublist = new LinkedListImpl<E>();
            while (!isFound && iterator.hasNext()){
               item = iterator.next();
                   if(item.equals(offset)) {
                       isFound = true;
                       while (iterator.hasNext()) {
                           item = iterator.next();
                           sublist.add(item);
                       }
                   }
        }
        return sublist;
    }


    public static <E> String print(ILinkedList<E> list) {
        Iterator<E> iterator = list.iterator();
        E item;
        String output = "";
        while (iterator.hasNext()){
            item = iterator.next();
            output += "[" + item.toString() + "]";
        }
        return output;
    }



    public static <E> ILinkedList<E> subList(ILinkedList<E> list, E offset, int size) {
        Iterator<E> iterator = list.iterator();
        LinkedListImpl sublist = null;
        boolean isFound = false;
        E item = null;
        sublist = new LinkedListImpl<E>();
        while (!isFound && iterator.hasNext()) {
            item = iterator.next();
            if (item.equals(offset)) {
                isFound = true;
                for (int i = 1; i <= size; i++) {
                    item = iterator.next();
                    sublist.add(item);
                }
            }
        }
        return sublist;
    }

    public static <E> ILinkedList<E> addList(ILinkedList<E> list, ILinkedList<E> other) {
        for (E elem : other)
            list.add(elem);
        return list;
    }

    public static <E> ILinkedList<E> removeList(ILinkedList<E> list, E offset, int size) {
        Iterator<E> iterator = list.iterator();
        LinkedListImpl sublist = null;
        E item = null;
        sublist = new LinkedListImpl<E>();
        while (iterator.hasNext()) {
            item = iterator.next();
            sublist.add(item);
            if (item.equals(offset)) break;
        }
        return sublist;
    }

    public static <E> boolean compareList(ILinkedList<E> list, ILinkedList<E> other) {
        if (list.size() != other.size())
            return false;
        return false;
    }

    public static <E extends Comparable<E>, R> R foldl(ILinkedList<E> list, R accum, LinkedListFold<R, E> func) {
        for (E element : list) {
            accum = func.execute(accum, element);
        }
        return accum;
    }

    public interface LinkedListFold<R, E> {
        R execute(R accum, E element);
    }
}
