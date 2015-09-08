package ru.mirea.oop.practice.lists;

/**
 * Created by student on 08.09.2015.
 */
public class Main {
    public static void main(String[] args) {
        LinkedListImpl<Integer> mylist = new LinkedListImpl<Integer>();
        mylist.add(1);
        mylist.add(2);
        mylist.add(3);
        mylist.add(4);
        ILinkedList<Integer> list2 =  Algorithms.subList(mylist, 2);
        System.out.println(list2.toString());
    }
}
