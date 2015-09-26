package ru.mirea.oop.practice.lists;

/**
 * Created by student on 08.09.2015.
 */
public class Main {
    public static void main(String[] args) {
        LinkedListImpl<String> mylist = new LinkedListImpl<String>();
        mylist.add("B");
        mylist.add("A");
        mylist.add("C");
        mylist.add("D");
        mylist.add("E");
        //Algorithms.sort(mylist);
        ILinkedList<String> list3 = Algorithms.sort(mylist);
        //ILinkedList<Integer> list4 = Algorithms.removeList(mylist, 2, 2);
        System.out.println(Algorithms.print(list3));
        //System.out.println(Algorithms.print(list3));
        //System.out.println(Algorithms.print(list4));
    }
}
