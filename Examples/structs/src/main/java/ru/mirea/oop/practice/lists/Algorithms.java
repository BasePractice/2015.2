package ru.mirea.oop.practice.lists;

import java.util.Iterator;
import java.lang.Comparable;

public final class Algorithms  {
    public static <E extends Comparable<E>> void sort(ILinkedList<E> list) {

        E obj1 = null;
        while (true) {
            int k = 0;
            Iterator<E> iterator = list.iterator();
            while (iterator.hasNext()) {
                E obj = obj1;
                obj1 = iterator.next();
                int i = compare(obj1, obj);
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

        return  linkedList;
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

        return  linkedList;
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

    public static <E extends Comparable<E>> int compare(final E name1, final E name2) {
        if (name1 == null) return name2 == null ? 0 : -1;
        if (name2 == null) return 1;
        return name1.compareTo(name2);
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

        ILinkedList<A> linkedList = new LinkedListImpl<A>();
        A a1 = new A(1);
        A a2 = new A(5);
        A a3 = new A(3);
        A a4 = new A(4);
        A a5 = new A(8);
        A a6 = new A(1);
        linkedList.add(a5);
        linkedList.add(a1);
        linkedList.add(a2);
        linkedList.add(a3);
        linkedList.add(a4);
        linkedList.add(a6);
        ILinkedList<A> linkedList1 = subList(linkedList, a2, 2);
        ILinkedList<A> linkedList3 = removeList(linkedList1, a2, 2);
        sort(linkedList);
        ILinkedList<A> linkedList2 = subList(linkedList, a2);

    }
    public static class A implements Comparable {
        private int num;

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public A(int num) {
            this.num = num;
        }

        @Override
        public int compareTo(Object o) {
            A a = (A) o;
            if(this.getNum() < a.getNum()) {
                return -1;
            }
            else if(this.getNum() > a.getNum()) {
                return 1;
            }
            else {
                return 0;
            }
        }
    }


}
