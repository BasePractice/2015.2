package ru.mirea.oop.practice.set;

import java.util.Iterator;

public final class Algorithms {
    /** Вхождение */
    public static <E> boolean isIncluded(ISet<E> set, ISet<E> include) {
       int k = 0;
        Iterator<E> iterator = include.iterator();
        while (iterator.hasNext()) {
            E obj = iterator.next();
            if(set.contains(obj)) {
                k++;
            }
        }
        if(k == include.size()) {
            return true;
        }
        return false;

    }
    /** Равенство */
    public static <E> boolean isEquals(ISet<E> set, ISet<E> other) {
        Iterator<E> iterator = set.iterator();
        if(other.size() != set.size()) {
            return false;
        }
        while (iterator.hasNext()) {
            E obj = iterator.next();
            if(!other.contains(obj)) {
                return false;
            }
         }
        return true;
    }
    /** Пересечение */
    public static <E> boolean isIntersection(ISet<E> set, ISet<E> other) {

        Iterator<E> iterator = set.iterator();
        while (iterator.hasNext()) {
            E obj = iterator.next();
            if(other.contains(obj)) {
               return true;
            }
        }
        return false;
    }
    /** Объединение */
    public static <E> ISet<E> union(ISet<E> set, ISet<E> other) {
        ISet<E> set1 = new SetImpl<>();
        Iterator<E> iterator = set.iterator();
        while (iterator.hasNext()) {
            E obj = iterator.next();
            set1.put(obj);
        }
        Iterator<E> iterator1 = other.iterator();
        while (iterator1.hasNext()) {
            try {
                E obj = iterator1.next();
                set1.put(obj);
            }
            catch (Exception e) {
                continue;
            }
        }
        return set1;
    }
    /** Пересечение */
    public static <E> ISet<E> intersection(ISet<E> set, ISet<E> other) {
        ISet<E> set1 = new SetImpl<>();
        Iterator<E> iterator = set.iterator();
        while (iterator.hasNext()) {
            E obj = iterator.next();
            if(other.contains(obj)) {
               set1.put(obj);
            }
        }
        return set1;
    }
    /** Разность */
    public static <E> ISet<E> difference(ISet<E> set, ISet<E> other) {
        ISet<E> set1 = new SetImpl<>();
        Iterator<E> iterator = set.iterator();
        while (iterator.hasNext()) {
            E obj = iterator.next();
            if(!other.contains(obj)) {
                set1.put(obj);
            }
        }
        Iterator<E> iterator1 = other.iterator();
        while (iterator1.hasNext()) {
            E obj = iterator1.next();
            if(!set.contains(obj)) {
                set1.put(obj);
            }
        }
        return set1;
    }
    /** Произведение */
    public static <E> ISet<E> composition(ISet<E> set, ISet<E> other) {
        throw new RuntimeException("Not implement yet");
    }

    public static void main(String[] args) {
        ISet<A> set = new SetImpl<>();
        ISet<A> set1 = new SetImpl<>();
        A a1 = new A();
        A a2 = new A();
        A a3 = new A();
        A a4 = new A();
        set.put(a1);
        set.put(a2);
        set1.put(a1);
        set1.put(a1);
       ISet<A> set2 = new SetImpl<>();
       ISet<A> set3 = new SetImpl<>();
        set2 = difference(set, set1);
        set3 = union(set, set1);

        System.out.println(isEquals(set, set));


    }
    public static class A {
        public A() {
        }
    }
}